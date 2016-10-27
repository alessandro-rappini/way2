package com.example.alessandrorappini.way.Oggetti;

/**
 * Created by Alessandro Rappini on 25/10/2016.
 */

public class WifiObj {

    String ssid;
    String bssid;
    int rssi;


    public WifiObj(String ssid, String bssid, int rssi) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
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
}
