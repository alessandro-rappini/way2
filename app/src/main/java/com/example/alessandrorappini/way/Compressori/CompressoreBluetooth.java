package com.example.alessandrorappini.way.Compressori;

import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 06/02/2017.
 */

public class CompressoreBluetooth {

    static LinkedList totaleB;
    static int position ;
    public static String edificio , rpSelezionato , so , nomeDevice , precisione;
    public static int controllo = 0;

    public static void inizia(BluetoothCheif cheifBluetooth) {
        totaleB = new LinkedList();
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
                totaleB.add(l);
            }else {
                BluetoothObj  l = (BluetoothObj) bluetoothObj.get(i);
                boolean presente = controlla(l);
                if(presente == false){
                    totaleB.add(l);
                } else {
                    BluetoothObj  appoggio = (BluetoothObj) totaleB.get(position);
                    appoggio.inserisciRssiMedia(l.getRssi());
                }
            }
        }
    }

    private static boolean controlla(BluetoothObj l) {
        boolean esiste = false;
        String bssid = l.getDevice();
        for (int i=0 ; i<totaleB.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleB.get(i);
            if (appoggio.getDevice().equals(bssid)) {
                esiste = true;
                position = i;
            }
        }
        return esiste;
    }

    private static void calcolaMediaVarianza() {
        for (int i=0 ; i<totaleB.size() ; i++) {
            BluetoothObj appoggio = (BluetoothObj) totaleB.get(i);
            appoggio.eseguiCalcoliRssi();
        }
        invio();
    }

    private static void invio() {PrincipaleLocalizzati.attesaBluetooth(totaleB); }

    //totale è la lista comressa
}
