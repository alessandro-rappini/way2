package com.example.alessandrorappini.way.ChiamateLocalizzazione;

import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Server.Setpath;

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
 * Created by Alessandro Rappini on 08/02/2017.
 */

public class ChiamataLocalizzazioneBluetooth {
    static LinkedList deviceBluetoothR , rssiBluetoothR;
    static String nomeR;
    static JSONArray rpRisp = null;
    static HashMap<String, Integer> myMapBlue ;
    public  ChiamataLocalizzazioneBluetooth (LinkedList deviceBluetooth, LinkedList rssiBluetooth, String nome){
        deviceBluetoothR = deviceBluetooth;
        rssiBluetoothR = rssiBluetooth;
        nomeR = nome;
        new controllaMisurazioniBluetooth().execute();
    }


    private static class controllaMisurazioniBluetooth extends AsyncTask<String, String, String> {
        public controllaMisurazioniBluetooth( ) {
            Log.i("nomeR" , nomeR);
            Log.i("info " , "inizio a mandare su");
        }

        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nome", nomeR));

            for (int i = 0; i < deviceBluetoothR.size(); i++) {
                Log.i("inserisco dev Blue -->" , (String ) deviceBluetoothR.get(i));
                //ssid //bssid // rssidMedia //rssidVarianza
                params.add(new BasicNameValuePair("typeDevice[]", (String) deviceBluetoothR.get(i)));
                //params.add(new BasicNameValuePair("typeBssid[]", (String) bssid.get(i)));
                //params.add(new BasicNameValuePair("typeRssidMedia[]", (String) rssidMedia.get(i)));
            }

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"mach/machNomiBluetooth.php";

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
                    rpRisp = json.getJSONArray("arrayCompresso");
                    myMapBlue = new HashMap<String, Integer>();
                    for (int i = 0; i < rpRisp.length(); i = i+2 ) {
                        String nomerp = rpRisp.get(i).toString();
                        int value = Integer.parseInt(rpRisp.get(i+1).toString());
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
            PrincipaleLocalizzati.inserisciHasMapNomiBluetooth(myMapBlue);
        }


    }
}
