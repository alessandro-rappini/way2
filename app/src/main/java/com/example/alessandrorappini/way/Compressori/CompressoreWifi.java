package com.example.alessandrorappini.way.Compressori;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;

import java.util.LinkedList;

/**
 * Metodo richiamato dalla classe "PrincipaleLocalizzati"
 * comprime i risultati ottenuti dalle varie interazioni delle analisi del WiFI
 * calcolando la media e la varianza degli stessi oggetti con lo stesso bssid
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
        /*
          se è la il primo ciclo inseririsci i risultati della lista in modo
          da creare una lista indicizzata
        */
        for (int i=0 ; i<wifiObj.size() ; i++){
            if (posizione == 0){
                WifiObj  l = (WifiObj) wifiObj.get(i);
                totale.add(l);
            }else {
                /*
                    se non è la prima analisi, allora bisogna controllare se il nuovo dato
                    e verificare se è presente o meno nella lista
                */
                WifiObj  l = (WifiObj) wifiObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    /*
                    se è presente allora inseriemo l'ulteriore dato provenite dalla rilevazione
                    nella lista dell'oggetto in cui l'bssid sia uguale
                     */
                    totale.add(l);
                } else {
                    // se non è presente lo inserimo nella lista in modo da creare un nuovo indice
                    WifiObj  appoggio = totale.get(position);
                    appoggio.inserisciRssi(l.getRssi());
                }
            }
        }
    }

    // effettua il controllo su una possibile pre esisteza
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

    // richiama il mettodo che calcola la media e la varianza
    private static void calcolaMediaVarianza() {
        for (int i=0 ; i<totale.size() ; i++) {
            WifiObj appoggio = totale.get(i);
            appoggio.eseguiCalcoliRssi();
        }
        invio();
    }

    //invia i dati
    private static void invio() {
        PrincipaleLocalizzati.attesaWifi(totale);
    }



}
