package com.example.alessandrorappini.way.Utilities;

import java.io.Serializable;

/**
 * Created by Alessandro Rappini on 16/02/2017.
 */

public class OggBoth implements Serializable {
    String rpR , algoritmoR ;
    double potenzaR;

    public OggBoth( String rp , Double potenza , String algoritmo){
        rpR =rp;
        potenzaR =potenza;
        algoritmoR = algoritmo;
    }

    public Double getPotenza() {
        return potenzaR;
    }

    public String getRp() {
        return rpR;
    }

    public String getAlgortimo() {
        return algoritmoR;
    }

}
