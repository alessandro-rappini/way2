package com.example.alessandrorappini.way.Misurazioni.Misurazioni.NetWork;

import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.NetWork.NetWorkCheif;
import com.example.alessandrorappini.way.Server.Setpath;
import com.example.alessandrorappini.way.Utilities.Utilities;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo.jsonParser;

/**
 * Created by Alessandro Rappini on 03/12/2016.
 */

public class NetWorkAlgo {

    public static String edificio , rpSelezionato , so , nomeDevice , precisione;
    public static String media , varianza;

    public static void invia(NetWorkCheif netWorkCheif) {
        edificio = AggiungiMisurazioni.edificio;
        rpSelezionato =  AggiungiMisurazioni.rpSelezionato;
        so = Utilities.so();
        nomeDevice = Utilities.getDeviceName();
        precisione = String.valueOf(AggiungiMisurazioni.precisione) ;
        media = String.valueOf(netWorkCheif.getMediaNetWork());
        varianza = String.valueOf(netWorkCheif.getVarianzaNetWork());
        new inserisciMisurazioniNetWork ().execute();
    }

    private static class inserisciMisurazioniNetWork extends AsyncTask<String, String, String> {

        public inserisciMisurazioniNetWork() {
        }


        protected String doInBackground(String... args) {
            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", edificio));
            params.add(new BasicNameValuePair("rp", rpSelezionato));
            params.add(new BasicNameValuePair("media", media));
            params.add(new BasicNameValuePair("varianza", varianza));
            params.add(new BasicNameValuePair("so", so));
            params.add(new BasicNameValuePair("nomeDevice", nomeDevice));
            params.add(new BasicNameValuePair("precisione", precisione));

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"inserimentoNetWork.php";

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
            Log.i("finito" , "finito");
        }

    }

}
