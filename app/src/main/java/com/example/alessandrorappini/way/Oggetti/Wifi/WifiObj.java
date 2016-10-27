package com.example.alessandrorappini.way.Oggetti.Wifi;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public class WifiObj {

    String ssid;
    String bssid;
    int rssi;
    int varianzaRssi;


    public WifiObj(String ssid, String bssid, int rssi) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
        varianzaRssi =0;
        }

    public String getSsid() {
        return ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public int getRssi() {
        return rssi;
    }

    public int varianzaRssi() {
        return rssi;
    }


}
