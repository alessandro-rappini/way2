package com.example.alessandrorappini.way.Misurazioni;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public  class WiFi   extends AsyncTask<Void, Void , Void>  {
    WifiManager  wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;

    Context con = null;
    Intent inte = null;

    public WiFi(Context c, Intent i){
        con = c ;
        inte = i;
    }

    protected void onPreExecute(){
        Log.i("info" , " onPreExecute");
        Log.i("info" , "------------------ "  );
        Log.i("info" , " "  );
    }

    @Override
    protected Void doInBackground(Void... params){
        Log.i("info" , "doInBackground");
        new WifiScanReceiver().onReceive(con , inte);
        return null;
    }

    protected void onPostExecute(Void result){
        Log.i("info" , " onPostExecute");
        Log.i("info" , "------------------ "  );
        Log.i("info" , " "  );Log.i("info" , " "  );Log.i("info" , " "  );Log.i("info" , " "  );Log.i("info" , " "  );Log.i("info" , " "  );
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
            }
        }
    }


}