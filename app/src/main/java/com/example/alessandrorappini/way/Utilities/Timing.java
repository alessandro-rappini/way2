package com.example.alessandrorappini.way.Utilities;

/**
 * Created by Alessandro Rappini on 02/11/2016.
 */

public class Timing {
    int uno = 1000;
    int due = 2000;
    int tre = 3000;
    int quattro = 4000;
    int cinque = 5000;
    int sei = 6000;
    int sette = 7000;
    int otto = 8000;
    int nove = 9000;
    int dieci = 10000;

    public Timing() {

    }

    public Integer getTiming(int num) {
        if(num == 0){
            return uno;
        }
        if(num == 1){
            return due;
        }
        if(num == 2){
            return tre;
        }
        if(num == 3){
            return quattro;
        }
        if(num == 4){
            return cinque;
        }
        if(num == 5){
            return sei;
        }
        if(num == 6){
            return sette;
        }
        if(num == 7){
            return otto;
        }
        if(num == 8){
            return nove;
        }
        if(num == 9){
            return dieci;
        }

        return null;
    }
}
