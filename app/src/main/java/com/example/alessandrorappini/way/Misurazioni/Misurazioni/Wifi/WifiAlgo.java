package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi;

import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;
import com.example.alessandrorappini.way.Server.JSONParser;
import com.example.alessandrorappini.way.Server.Setpath;
import com.example.alessandrorappini.way.Utilities.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alessandro Rappini on 27/10/2016.
 */

public class WifiAlgo {
    public  static LinkedList <WifiObj> totale;
    static int position ;
    private static String edificio , rpSelezionato , so , nomeDevice , precisione;
    public  static JSONParser jsonParser = new JSONParser();


    public static void inizia(WifiCheif cheifWifi) {
        totale = new LinkedList();
        for (int i =0 ; i<cheifWifi.getLunghezza() ; i++ ){
            LinkedList l = cheifWifi.getLista(i);
            apri(l , i);
        }
        calcolaMediaVarianza();

    }

    private static void apri(LinkedList wifiObj , int posizione) {
        for (int i=0 ; i<wifiObj.size() ; i++){
            if (posizione == 0){
                WifiObj  l = (WifiObj) wifiObj.get(i);
                totale.add(l);
            }else {
                WifiObj  l = (WifiObj) wifiObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    totale.add(l);
                } else {
                    WifiObj  appoggio = totale.get(position);
                    appoggio.inserisciRssi(l.getRssi());
                }
            }
        }
    }


    private static boolean controlla(WifiObj l) {
        boolean esiste = false;
        String bssid = l.getBssid();
        for (int i=0 ; i<totale.size() ; i++) {
            WifiObj appoggio = totale.get(i);
            if (appoggio.getBssid().equals(bssid)) {
                esiste = true;
                position = i;
            }
        }
        return esiste;
    }

    private static void calcolaMediaVarianza() {
        for (int i=0 ; i<totale.size() ; i++) {
            WifiObj appoggio = totale.get(i);
            appoggio.eseguiCalcoliRssi();
        }

        edificio = AggiungiMisurazioni.edificio;
        rpSelezionato =  AggiungiMisurazioni.rpSelezionato;
        so = Utilities.so();
        nomeDevice = Utilities.getDeviceName();
        precisione = String.valueOf(AggiungiMisurazioni.precisione) ;

       // new inserisciMisurazioniWifi().execute();
    }
    ///////////////////WORK IN PROGRESS///////////////////////////////////
    ///////////////////WORK IN PROGRESS///////////////////////////////////
    ////////////////// WORK IN PROGRESS //////////////////////////////////
    ///////////////////WORK IN PROGRESS///////////////////////////////////
    ///////////////////WORK IN PROGRESS///////////////////////////////////
    private static class inserisciMisurazioniWifi extends AsyncTask<String, String, String> {


        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nome", "nome"));

            // creo il path
            Setpath setpath =new Setpath();
            String path = setpath.getPath();
            String url = path+"inserimentoEdificio.php";

            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("info","INSERITO");
                } else   {
                    Log.i("info","Record già presnte");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void doInBackground(){

        }

        protected void onPostExecute(String file_url) {

        }


    }

}
