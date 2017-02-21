package com.example.alessandrorappini.way.ChiamateLocalizzazione;

import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Server.Setpath;
import com.example.alessandrorappini.way.Utilities.OggBoth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo.jsonParser;

/**
 * Created by Alessandro Rappini on 16/02/2017.
 */

public class ChiamataLocalizzazioneFrequenzeBoth {
    static boolean attessaWifi = true , attessaBlue = true ;

    static LinkedList listaOggettiBest ;

    static String nomeR;
    static int mediaNetWorkR , lungR;
    static LinkedList ssidR , bssidR , rssidMediaR , deviceBluetoothR , rssiBluetoothR ;

    static JSONArray rpRispWifi = null , rpRispBlue = null ,rpRispNetWork = null ;
    static HashMap<String, Double> myMapWifi , myMapBlue ;

    public   ChiamataLocalizzazioneFrequenzeBoth(String nome , LinkedList ssid , LinkedList bssid , LinkedList rssidMedia ,LinkedList deviceBluetooth ,LinkedList rssiBluetooth , int mediaNetWork , int lung){
        nomeR = nome;
        ssidR =ssid;
        bssidR = bssid;
        rssidMediaR =rssidMedia;
        deviceBluetoothR = deviceBluetooth;
        rssiBluetoothR = rssiBluetooth;
        mediaNetWorkR = mediaNetWork ;
        lungR = lung;
        avvia();
    }

    private void avvia() {
        new controllaMisurazioniFrequenzeWifiBoth().execute();
        new controllaMisurazioniFrequenzeBluetoothBoth().execute();
        //new controllaMisurazioniFrequenzeNeTWorkBoth().execute();
    }

    private static void chaimaLaFine() {
        if(attessaWifi == false && attessaBlue == false ){
            listaOggettiBest = new LinkedList();
            /* calcolo il quello con l'errore più basso  sia wifi che bluetooth*/
            //WIFI
            Log.i("info","inizio a chimare wifi");
            calcolaIlMiglioreWifi();

        }
    }

    private static void calcoloIlMiglioreNelVettore() {
        String best = null;
        String algoritmo = null;
        Double registro = 0.0;
        for (int i=0 ; i < listaOggettiBest.size(); i++){
            OggBoth appoggio = (OggBoth) listaOggettiBest.get(i);
            if (i==0){
                best = (String) appoggio.getRp();
                registro = appoggio.getPotenza();
                algoritmo = appoggio.getAlgortimo();
            }else {
                if (registro > appoggio.getPotenza()){
                    best = (String) appoggio.getRp();
                    registro = appoggio.getPotenza();
                    algoritmo = appoggio.getAlgortimo();
                }
            }
        }
        OggBoth miglire = new OggBoth(best , registro , algoritmo);
        PrincipaleLocalizzati.inserisciHasMapFrequenzeBoth(miglire);
    }

    private static void calcolaIlMiglioreBlue() {
        LinkedList prov = new LinkedList();
        String best = null;
        Double registro = 0.0;
        for ( Object key : myMapBlue.keySet() ) {
            prov.add(key);
        }
        for (int i=0 ; i < myMapBlue.size(); i++){
            Double intProvvisorio = (Double) myMapBlue.get(prov.get(i));
            if (i==0){
                best = (String) prov.get(i);
                registro = intProvvisorio;
            }else {
                if (registro < intProvvisorio){
                    best = (String) prov.get(i);
                    registro = intProvvisorio;
                }
            }
        }
        OggBoth oggBoth = new OggBoth(best , registro , "Blue");
        listaOggettiBest.add(oggBoth);
        calcoloIlMiglioreNelVettore();
    }

    private static void calcolaIlMiglioreWifi() {
        LinkedList prov = new LinkedList();
        String best = null;
        Double registro = 0.0;
        for ( Object key : myMapWifi.keySet() ) {
             prov.add(key);
        }
        for (int i=0 ; i < myMapWifi.size(); i++){
            Double intProvvisorio = (Double) myMapWifi.get(prov.get(i));
            if (i==0){
                best = (String) prov.get(i);
                registro = intProvvisorio;
            }else {
                if (registro < intProvvisorio){
                    best = (String) prov.get(i);
                    registro = intProvvisorio;
                }
            }
        }
        OggBoth oggBoth = new OggBoth(best , registro , "wifi");
        listaOggettiBest.add(oggBoth);
        calcolaIlMiglioreBlue();

    }


    private static class controllaMisurazioniFrequenzeWifiBoth extends AsyncTask<String, String, String> {
        public controllaMisurazioniFrequenzeWifiBoth( ) {

        }
        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //nome palazzina
            params.add(new BasicNameValuePair("nome", nomeR));
            for (int i = 0; i < lungR; i++) {
                //ssid //bssid // rssidMedia //rssidVarianza
                Log.i("typeBssid" , (String) bssidR.get(i));
                params.add(new BasicNameValuePair("typeBssid[]", (String) bssidR.get(i)));
                Log.i("typeRssidMedia" , (String) rssidMediaR.get(i));
                params.add(new BasicNameValuePair("typeRssidMedia[]", (String) rssidMediaR.get(i)));
            }
            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"mach/machFrequenzeWifiBoth.php";
            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("info" , "risposta");
                    rpRispWifi = json.getJSONArray("arrayCompresso");
                    myMapWifi = new HashMap<String, Double>();
                    for (int i = 0; i < rpRispWifi.length(); i = i+2 ) {
                        String nomerp = rpRispWifi.get(i).toString();
                        double value = Double.parseDouble(rpRispWifi.get(i+1).toString());
                        Log.i("nome" , nomerp);
                        Log.i("value" , value+"");
                        myMapWifi.put(nomerp,value);
                    }
                } else {
                    Log.i("info","ERRORE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void doInBackground(){
            Log.i("work" , "work");
        }

        protected void onPostExecute(String file_url) {
            attessaWifi = false;
            chaimaLaFine();
        }

    }

    private static class controllaMisurazioniFrequenzeBluetoothBoth extends AsyncTask<String, String, String> {
        public controllaMisurazioniFrequenzeBluetoothBoth( ) {

        }
        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //nome palazzina
            params.add(new BasicNameValuePair("nome", nomeR));

            //for (int i = 0; i < lungR; i++) {
            for (int i = 0; i < deviceBluetoothR.size(); i++) {

                //ssid //bssid // rssidMedia //rssidVarianza
                Log.i("typeDevice" , (String) deviceBluetoothR.get(i));
                params.add(new BasicNameValuePair("typeDevice[]", (String) deviceBluetoothR.get(i)));
                Log.i("typeRssidMedia" , (String) rssiBluetoothR.get(i));
                params.add(new BasicNameValuePair("typeRssidMedia[]", (String) rssiBluetoothR.get(i)));
            }

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"mach/machFrequenzeBluetoothBoth.php";

            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.i("ris","risultato");
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("---------","-------");
                    Log.i("info" , "risposta");
                    rpRispBlue = json.getJSONArray("arrayCompresso");
                    myMapBlue = new HashMap<String, Double>();
                    for (int i = 0; i < rpRispBlue.length(); i = i+2 ) {
                        String nomerp = rpRispBlue.get(i).toString();
                        double value = Double.parseDouble(rpRispBlue.get(i+1).toString());
                        Log.i("nome" , nomerp);
                        Log.i("value" , value+"");
                        myMapBlue.put(nomerp,value);
                    }
                } else {
                    Log.i("info","ERRORE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void doInBackground(){
            Log.i("work" , "work");
        }

        protected void onPostExecute(String file_url) {
            //mando l'hasMap a Principlae Localizzati
            Log.i("info" , "abbiamo finito col blue");
            attessaBlue = false;
            chaimaLaFine();
        }
    }

}
