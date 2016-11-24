package com.example.alessandrorappini.way.Oggetti.Bluetooth;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 24/11/2016.
 */

public class BluetoothObj {

    String device;
    int rssi;
    LinkedList rssiList;

    public BluetoothObj(String deviceV) {
        this.device = deviceV;
        rssiList = new LinkedList<>();
    }

    public String getDevice() {
        return device;
    }

    public Integer getRssi() {
        return rssi;
    }

    public void inserisciRssi (int lst){
        rssiList.add(lst);
    }

    public void calcolaMediaRssi() {
        int somma =0 ;
        int numeroValori;
        numeroValori = rssiList.size();
        for (int i=0 ; i< numeroValori ; i++){
            int ap = (int) rssiList.get(i);
            somma = somma + ap;
        }
        int media = (somma/numeroValori);
        rssi = media;
        rssiList.clear();
    }

}
