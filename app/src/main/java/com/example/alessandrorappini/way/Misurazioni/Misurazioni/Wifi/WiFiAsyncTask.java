package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public  class WiFiAsyncTask  {
    WifiManager  wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;
    WifiObj oby;
    WifiCheif cc;
    LinkedList <WifiObj> listParziale;
    Context con = null;
    Intent inte = null;


    public WiFiAsyncTask(Context c, Intent i ){
        con = c ;
        inte = i;
        new avviaRicerca().execute();
    }

    public void SetUnregisterReceiver() {
        con.unregisterReceiver(wifiReciever);
    }


    private  class avviaRicerca extends AsyncTask<String, String, String> {
        protected void onPreExecute(){
            listParziale = new <WifiObj>  LinkedList ();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("info", "                !!!         doInBackground");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            wifi = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
            wifiReciever = new WifiScanReceiver();
            con.registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            wifi.startScan();
            return null;
        }
    }


    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];
            for(int i = 0; i < wifiScanList.size(); i++){
                ScanResult scanresult = wifi.getScanResults().get(i);
                int rssi = scanresult.level;
                String bssid = scanresult.BSSID;
                String ssid = scanresult.SSID;
                Log.i("trovato" , "TROVATO rssi:   " +rssi );
                Log.i("trovato" , "TROVATO bssid:   " +bssid );
                Log.i("trovato" , "TROVATO ssid:   " +ssid );
                Log.i("info" , "------------------ "  );
                oby = new WifiObj(ssid , bssid , rssi);
                listParziale.add(oby);
            }
            AggiungiMisurazioni.inserisciCheif(listParziale);
        }
    }

}