package com.example.alessandrorappini.way.Oggetti.Wifi;

import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 27/10/2016.
 */

public class WifiCheif {
    LinkedList <LinkedList> list;
    int precisione;

    public WifiCheif(int pr){
        precisione = pr;
        list = new LinkedList<>();
    }

    public void inserisci (LinkedList lst){
        list.add(lst);
    }

    public int   getLunghezza(){
         return list.size();
    }

    public void controlla(){
        if ((precisione) == list.size()){
            AggiungiMisurazioni.scopatta();
        }else {
            Log.i("infooo" , "continuo a ciclare");
        }
    }

    public LinkedList getLista(int i){
        return list.get(i);
    }
}
