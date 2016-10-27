package com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi;

import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 27/10/2016.
 */

public class WifiAlgo {
    public  static LinkedList <WifiObj> generale;

    public static void inizia(WifiCheif cheifWifi) {
        generale = new LinkedList();
        for (int i =0 ; i<cheifWifi.getLunghezza() ; i++ ){
            LinkedList l = cheifWifi.getLista(i);
            apri(l);
        }

    }

    private static void apri(LinkedList wifiObj) {
        for (int i=0 ; i<wifiObj.size() ; i++){
            WifiObj  l = (WifiObj) wifiObj.get(i);
            /*Log.i("getSsid" , l.getSsid() );
            Log.i("getBssid" , l.getBssid());
            Log.i("getRssi" , l.getRssi() + "");*/


        }
    }
}
