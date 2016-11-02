package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;
import com.example.alessandrorappini.way.Utilities.Timing;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public  class WiFiAsyncTask extends AsyncTask<Void, Void , Void>  {
    WifiManager  wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;
    WifiObj oby;
    WifiCheif cc;
    LinkedList <WifiObj> listParziale;

    Context con = null;
    Intent inte = null;
    int timing ;

    public WiFiAsyncTask(Context c, Intent i , int timingFor){
        con = c ;
        inte = i;
        Timing t = new Timing();
        timing = t.getTiming(timingFor);
        Log.i("timing" , timing+"");
    }

    protected void onPreExecute(){
        Log.i("info" , " onPreExecute");
        listParziale = new <WifiObj>  LinkedList ();
    }

    @Override
    protected Void doInBackground(Void... params){
        Log.i("info" , "                !!!         doInBackground");
        Log.i("timing" , timing+"");
        try {
            Thread.sleep(timing);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        new WifiScanReceiver().onReceive(con , inte);
        return null;
    }

    protected void onPostExecute(Void result){
        Log.i("info" , " onPostExecute");

        AggiungiMisurazioni.inserisciCheif(listParziale);
    }


    private class WifiScanReceiver extends BroadcastReceiver {

        public void onReceive(Context c, Intent intent) {
            //Log.i("inizio" , "sono all inizo");
            wifi=(WifiManager)c.getSystemService(Context.WIFI_SERVICE);
            wifiReciever = new WifiScanReceiver();
            wifi.startScan();

            List<ScanResult> wifiScanList = wifi.getScanResults();
            //Log.i("INFO" , "LUNGHEZZA");
            wifis = new String[wifiScanList.size()];
            //Log.i("INFO" , wifiScanList.size() +"");
            for(int i = 0; i < wifiScanList.size(); i++){

                ScanResult scanresult = wifi.getScanResults().get(i);
                int rssi = scanresult.level;
                String bssid = scanresult.BSSID;
                String ssid = scanresult.SSID;
                Log.i("info" , "------------------ "  );
                Log.i("trovato" , "TROVATO rssi:   " +rssi );
                Log.i("trovato" , "TROVATO bssid:   " +bssid );
                Log.i("trovato" , "TROVATO ssid:   " +ssid );
                Log.i("info" , "------------------ "  );
                Log.i("info" , " "  );
               // Log.i("INFO" , (wifiScanList.get(i)).toString());
                oby = new WifiObj(ssid , bssid , rssi);
                listParziale.add(oby);

            }
        }
    }

}