package com.example.alessandrorappini.way.Server;
/**
 * Classe creata per avere un punto di accesso univerase
 * al path al fine di cambiarlo nella maniera più semplice
 * possibile
 */
public class Setpath {
    /**
     *
     * path per settare la connessione con i file php
     *
     */
    /*
    vedere gli id --> ipconfig
     */

    /*
    far partite mongo db
    mongod  --dbpath C:\mongoDB
    mongo
     */

    //!!!!!!! connesso alla stessa rete
    static String path = "http://192.168.1.20:8080/way/";
    //!!!!!!! connesso alla stessa rete

    //!!!!!!!!!!             hotspot
    //http://www.chimerarevo.com/internet/creare-hotspot-wireless-su-windows-182898/
    //netsh wlan set hostednetwork mode=allow ssid=Hotspot key=Password123
    //netsh wlan start hostednetwork
    //netsh wlan stop hostednetwork
    //netsh wlan set hostednetwork mode=disallow
    //static String path = "http://192.168.137.1:8080/way/";
    //!!!!!!!!!!             hotspot

    //!!!!!!!!!!            LOCALE
    //static String path = "http://10.0.2.2:8080/way/";
    //!!!!!!!!!!            LOCALE



    public String getPath() {
            return path;
        }

    public Setpath (){}
}

