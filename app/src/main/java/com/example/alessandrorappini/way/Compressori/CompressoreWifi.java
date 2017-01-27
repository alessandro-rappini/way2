package com.example.alessandrorappini.way.Compressori;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 27/01/2017.
 */

public class CompressoreWifi {
    public  static LinkedList<WifiObj> totale;
    static int position ;

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
        invio();
    }

    private static void invio() {
        PrincipaleLocalizzati.attesaWifi(totale);
    }

    //totale è la lista comressa


}
