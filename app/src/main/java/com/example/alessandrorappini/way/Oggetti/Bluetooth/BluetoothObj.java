package com.example.alessandrorappini.way.Oggetti.Bluetooth;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 24/11/2016.
 */

public class BluetoothObj {

    String device;
    int rssi;
    static int rssiMedia;
    static int rssiVarianza;
    LinkedList rssiList , rssiMediaList ;
    int differenza;
    int scarto = 0 ;

    public BluetoothObj(String deviceV) {
        this.device = deviceV;
        rssiList = new LinkedList<>();
        rssiMediaList = new LinkedList<>();
        rssiMedia = 0;
        rssiVarianza =0;
    }

    public String getDevice() {
        return device;
    }

    public Integer getRssi() {
        return rssi;
    }

    public Integer getRssiMedia() {
        return rssiMedia;
    }

    public Integer getRssiVarianza() {
        return rssiVarianza;
    }

    public void inserisciRssi (int lst){
        rssiList.add(lst);
    }

    public void inserisciRssiMedia (int lst){
        rssiMediaList.add(lst);
    }

    //non lo si usa più
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
    // non lo si usa piu

    public void prendiIlPrimo(){
        rssi = (int) rssiList.get(1);
        rssiList.clear();
    }

    public void eseguiCalcoliRssi() {
        if(rssiMediaList.size()>1){
        boolean controlloUguali = controlloUgualianza();

        if(controlloUguali == true){
            rssi = (int) rssiMediaList.get(0);
        }else {
            int tot = 0 ;
            for (int i=0 ; i< rssiMediaList.size() ; i++){
                int num = (int) rssiMediaList.get(i);
                tot = tot + num;

            }
            int media = tot / rssiMediaList.size();
            rssiMedia = media;
            for (int i=0 ; i< rssiMediaList.size() ; i++){
                int num = (int) rssiMediaList.get(i);
                differenza =  (num - media)^2;
                scarto = scarto + differenza;
                differenza=0;
            }

            //METODO UFFICIALE SECONDO LA STATISTICA
            rssiVarianza = scarto / (rssiMediaList.size()-1);
            //varianzaRssi = scarto;  --> sbagliato lo tengo solo per capire in un futuro
        }
    }else {
        rssiMedia = (int) rssiMediaList.get(0);
    }
    }

    private boolean controlloUgualianza() {
        boolean tuttiUguali = true;
        int primo = (int) rssiMediaList.get(0);
        for (int i=0 ; i< rssiMediaList.size() ; i++){
            int val = (int) rssiMediaList.get(i);
            if (primo != val){
                tuttiUguali=false;
            }
        }
        return tuttiUguali;
    }
}
