package com.example.alessandrorappini.way.Oggetti.Wifi;

import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public class WifiObj {

    String ssid;
    String bssid;
    int mediaRssi , varianzaRssi;
    //LinkedList varianzaRssi;
    LinkedList rssi;

    int media;
    int differenza;
    int scarto = 0 ;


    public WifiObj(String ssid, String bssid, int rssiV) {
        this.ssid = ssid;
        this.bssid = bssid;
        rssi = new LinkedList<>();
        rssi.add(rssiV);
        mediaRssi = 0;
        varianzaRssi = 0;
        //this.rssi = rssi;
        //varianzaRssi =new LinkedList<>();
        }

    public Integer getMediaRssi() {
        return mediaRssi;
    }

    public Integer getVarianzaRssi() {
        return varianzaRssi;
    }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getRssi() {
        int val = (int) rssi.get(0);
        return val;
    }

    public void inserisciRssi (int lst){
        rssi.add(lst);
    }

    public int getNumeroRssi(){
        return rssi.size();
    }

    public void eseguiCalcoliRssi (){
        if(rssi.size()>1){
            boolean controlloUguali = controlloUgualianza();

            if(controlloUguali == true){
                mediaRssi = (int) rssi.get(0);
            }else {
                int tot = 0 ;
                for (int i=0 ; i< rssi.size() ; i++){
                    int num = (int) rssi.get(i);
                    tot = tot + num;
                    media = tot / rssi.size();
                }
                Log.i("media" , "la media -->" + media);
                for (int i=0 ; i< rssi.size() ; i++){
                    int num = (int) rssi.get(i);
                    differenza =  (num - media)^2;
                    scarto = scarto + differenza;
                }

                Log.i("differenza" , "la differenza -->" + differenza);
                Log.i("scarto" , "la scarto -->" + scarto);

                varianzaRssi = scarto / (rssi.size()-1);
            }
        }else {
            mediaRssi = (int) rssi.get(0);
        }
    }

    private boolean controlloUgualianza() {
        boolean tuttiUguali = true;
        int primo = (int) rssi.get(0);
        for (int i=0 ; i< rssi.size() ; i++){
            int val = (int) rssi.get(i);
            if (primo != val){
                tuttiUguali=false;
            }
        }

        return tuttiUguali;
    }


    // public int varianzaRssi() {        return rssi; }

}
