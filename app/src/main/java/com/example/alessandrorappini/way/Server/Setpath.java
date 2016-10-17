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
    //static String path = "http://localhost:8080/way/";
    // static String path = "http://localhost/way/";
    static String path = "http://10.0.2.2:8080/way/";


    public String getPath() {
            return path;
        }

    public Setpath (){}
}

