package com.example.alessandrorappini.way.Compressori;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;

import java.util.LinkedList;

/**
 * Metodo richiamato dalla classe "PrincipaleLocalizzati"
 * comprime i risultati ottenuti dalle varie interazioni delle analisi del Bluetooth
 * calcolando la media e la varianza degli stessi oggetti con lo stesso UUID
 */

public class CompressoreBluetooth {

    static LinkedList totaleR;
    static int positionR ;

    public static void inizia(BluetoothCheif cheifBluetooth) {
        totaleR = new LinkedList();
        int lunghezza = cheifBluetooth.getLunghezza();
        for (int i=0 ; i < lunghezza ; i++){
            LinkedList appoggio = cheifBluetooth.getLista(i);
            apriB(appoggio , i);
        }
        calcolaMediaVarianza();
    }


    private static void apriB(LinkedList bluetoothObj , int posizione) {
        for (int i=0 ; i<bluetoothObj.size() ; i++){
            if (posizione == 0){
                /*
                se è la il primo ciclo inseririsci i risultati della lista in modo
                da creare una lista indicizzata*/
                BluetoothObj l = (BluetoothObj) bluetoothObj.get(i);
                totaleR.add(l);
            }else {
                /*
                    se non è la prima analisi, allora bisogna controllare se il nuovo dato
                    e verificare se è presente o meno nella lista
                */
                BluetoothObj  l = (BluetoothObj) bluetoothObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    /*
                    se è presente allora inseriemo l'ulteriore dato provenite dalla rilevazione
                    nella lista dell'oggetto in cui l'UUID sia uguale
                     */
                    totaleR.add(l);
                } else {
                    // se non è presente lo inserimo nella lista in modo da creare un nuovo indice
                    BluetoothObj  appoggio = (BluetoothObj) totaleR.get(positionR);
                    appoggio.inserisciRssiMedia(l.getRssi());
                }
            }
        }
    }

    // effettua il controllo su una possibile pre esisteza
    private static boolean controlla(BluetoothObj l) {
        boolean esiste = false;
        String bssid = l.getDevice();
        for (int i=0 ; i<totaleR.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleR.get(i);
            if (appoggio.getDevice().equals(bssid)) {
                esiste = true;
                positionR = i;
            }
        }
        return esiste;
    }

    // richiama il mettodo che calcola la media e la varianza
    private static void calcolaMediaVarianza() {
        for (int i=0 ; i<totaleR.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleR.get(i);
            appoggio.eseguiCalcoliRssi();
        }
        invio();
    }
    //invia i dati
    private static void invio() {PrincipaleLocalizzati.attesaBluetooth(totaleR); }

}
