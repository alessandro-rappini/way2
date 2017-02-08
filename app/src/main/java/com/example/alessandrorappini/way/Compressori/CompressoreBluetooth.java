package com.example.alessandrorappini.way.Compressori;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 06/02/2017.
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
                BluetoothObj l = (BluetoothObj) bluetoothObj.get(i);
                totaleR.add(l);
            }else {
                BluetoothObj  l = (BluetoothObj) bluetoothObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    totaleR.add(l);
                } else {
                    BluetoothObj  appoggio = (BluetoothObj) totaleR.get(positionR);
                    appoggio.inserisciRssiMedia(l.getRssi());
                }
            }
        }
    }

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

    private static void calcolaMediaVarianza() {
        for (int i=0 ; i<totaleR.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleR.get(i);
            appoggio.eseguiCalcoliRssi();
        }
        invio();
    }

    private static void invio() {PrincipaleLocalizzati.attesaBluetooth(totaleR); }

    //totale è la lista comressa
}
