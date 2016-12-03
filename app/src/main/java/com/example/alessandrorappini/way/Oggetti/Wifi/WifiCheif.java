package com.example.alessandrorappini.way.Oggetti.Wifi;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WiFiAsyncTask;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 27/10/2016.
 */

public class WifiCheif {
    LinkedList <LinkedList> list;
    int precisione;
    int contatoreWiFi = 0;

    boolean stop = false;
    Context con;
    Intent inte;
    int io;

    WiFiAsyncTask asyncTask;

    public WifiCheif(int pr , Context c, Intent i){
        precisione = pr;
        con = c ;
        inte = i;
        io=0;
        list = new LinkedList<>();
        Log.i("info" , "la precisione è " + precisione);
        chiamaAsync();
    }

    private synchronized void chiamaAsync() {
        Log.i("info" , "---- volta");
        Log.i("info" , io + "");
        Log.i("info" , "---- volta");
        if(stop==false){
            asyncTask =  new WiFiAsyncTask(con , inte );
        }else {
            Log.i("info" , "FERMO");
        }
        io++;
    }


    public synchronized void inserisci (LinkedList lst){
        Log.i("info" , "----");
        Log.i("info" , "chaimo --> SetUnregisterReceiver");
        Log.i("info" , "----");
        asyncTask.SetUnregisterReceiver();
        list.add(lst);
        contatoreWiFi ++;
        if ( contatoreWiFi == precisione ){
            stop=true;
            contatoreWiFi = 0;
            AggiungiMisurazioni.scopattaWifi();
        }else {
            chiamaAsync();
        }
    }

    public int getContatore (){
        return contatoreWiFi;
    }

    public int   getLunghezza(){
         return list.size();
    }


    public LinkedList getLista(int i){
        return list.get(i);
    }
}
