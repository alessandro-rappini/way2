package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;
import com.example.alessandrorappini.way.Server.Setpath;
import com.example.alessandrorappini.way.Utilities.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo.jsonParser;

/**
 * Created by Alessandro Rappini on 25/11/2016.
 */

public class BluetoothAlgo {

    static LinkedList totaleB;
    static int position ;
    public static String edificio , rpSelezionato , so , nomeDevice , precisione;
    public static int controllo = 0;

    public static void inizia(BluetoothCheif cheifBluetooth) {
        totaleB = new LinkedList();
        int lunghezza = cheifBluetooth.getLunghezza();
        for (int i=0 ; i < lunghezza ; i++){
            LinkedList appoggio = cheifBluetooth.getLista(i);
            apriB(appoggio , i);
        }

        calcolaMediaVarianza();
    }


    private static void apriB(LinkedList bluetoothObj , int posizione) {

        for (int i=0 ; i<bluetoothObj.size() ; i++){
            if (posizione == 0){
                BluetoothObj l = (BluetoothObj) bluetoothObj.get(i);
                totaleB.add(l);
            }else {
                BluetoothObj  l = (BluetoothObj) bluetoothObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    totaleB.add(l);
                } else {
                    BluetoothObj  appoggio = (BluetoothObj) totaleB.get(position);
                    appoggio.inserisciRssiMedia(l.getRssi());
                }
            }
        }
    }

    private static boolean controlla(BluetoothObj l) {
        boolean esiste = false;
        String bssid = l.getDevice();
        for (int i=0 ; i<totaleB.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleB.get(i);
            if (appoggio.getDevice().equals(bssid)) {
                esiste = true;
                position = i;
            }
        }
        return esiste;
    }

    private static void calcolaMediaVarianza() {
        edificio = AggiungiMisurazioni.edificio;
        rpSelezionato =  AggiungiMisurazioni.rpSelezionato;
        so = Utilities.so();
        nomeDevice = Utilities.getDeviceName();
        precisione = String.valueOf(AggiungiMisurazioni.precisione) ;

        for (int i=0 ; i<totaleB.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleB.get(i);
            appoggio.eseguiCalcoliRssi();
            String rssidMedia = String.valueOf(appoggio.getRssiMedia()) ;
            String rssidVarianza = String.valueOf(appoggio.getRssiVarianza()) ;
            new inserisciMisurazioniBluetooth(appoggio.getDevice() , rssidMedia , rssidVarianza).execute();
        }
    }

    private static class inserisciMisurazioniBluetooth extends AsyncTask<String, String, String> {
        String device , rssidMedia , rssidVarianza;
        public inserisciMisurazioniBluetooth( String device,  String rssidMrdia , String rssidVarianza) {
            this.device = device;
            this.rssidMedia = rssidMrdia;
            this.rssidVarianza = rssidVarianza;
        }


        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", edificio));
            params.add(new BasicNameValuePair("rp", rpSelezionato));
            params.add(new BasicNameValuePair("device", device));
            params.add(new BasicNameValuePair("rssidMedia", rssidMedia));
            params.add(new BasicNameValuePair("rssidVarianza", rssidVarianza));
            params.add(new BasicNameValuePair("so", so));
            params.add(new BasicNameValuePair("nomeDevice", nomeDevice));
            params.add(new BasicNameValuePair("precisione", precisione));

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"inserimentoBluetooth.php";

            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.i("ris","risultato");
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("info","INSERITO");
                } else {
                    Log.i("info","ERRORE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void doInBackground(){
            Log.i("invio" , "invio");
        }

        protected void onPostExecute(String file_url) {
            finito();
        }

        public synchronized void finito(){
            controllo = controllo + 1;
            if(controllo == totaleB.size()){
                fine();
            }
        }
    }

    private static void fine() {
        Log.i("********","********");
        Log.i("finito","ABBIAMO INSERTITO TUTTO");
        Log.i("********","********");
        Toast tea = Toast.makeText(AggiungiMisurazioni.con, "Reference point inserito correttamente", Toast.LENGTH_LONG);
        tea.show();
    }

}
