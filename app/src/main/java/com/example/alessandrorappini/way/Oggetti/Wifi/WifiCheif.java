package com.example.alessandrorappini.way.Oggetti.Wifi;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 27/10/2016.
 */

public class WifiCheif {
    LinkedList <LinkedList> list;

    public WifiCheif(){
        list = new LinkedList<>();
    }

    public void inserisci (LinkedList lst){
        list.add(lst);
    }

    public int   getLunghezza(){
         return list.size();
    }

}
