package com.example.alessandrorappini.way.Utilities;

/**
 * Created by Alessandro Rappini on 11/02/2017.
 */

public class OggettoBoth {
    double frequenzaR ;
    double frequenzaDecrementata;
    String rpR ,  algoritmoR;
    public OggettoBoth(int frequenza , String rp , String algoritmo){
        frequenzaR = frequenza;
        rpR =rp;
        algoritmoR =algoritmo;
    }

    public void decrementaMedia( double somma) {
        frequenzaDecrementata = frequenzaR / somma;
    }

    public Double getFrequenzaDecrementata() {
        return frequenzaDecrementata;
    }

    public String getAlgoritmo() {
        return algoritmoR;
    }

    public String getRp() {
        return rpR;
    }

}
