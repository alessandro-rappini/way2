package com.example.alessandrorappini.way.Oggetti.Bluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth.BluetoothObjTask;
import com.example.alessandrorappini.way.Utilities.ChiamataBold;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 24/11/2016.
 */

public class BluetoothCheif {

    static Context con;
    Intent inte;
    static int precisione;
    static LinkedList <LinkedList> listPrimaria;
    static String fine;
    static int contatoreBlue =0 ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BluetoothCheif(int pr , Context c, Intent i , String invio){
        precisione = pr;
        con = c ;
        inte = i;
        fine = invio;
        listPrimaria = new LinkedList<>();
        ChiamataBold chiamata = new ChiamataBold(0);
        chiamaBluetooth();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void chiamaBluetooth() {
        contatoreBlue++;
        BluetoothObjTask boat = new BluetoothObjTask(con , fine);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void inserisci(LinkedList lista) {

        listPrimaria.add(lista);
        if(contatoreBlue == precisione){
            Log.i("info" , "abbiamo finito chiamo lo scompattatore");
            scomapatta();
        }else {
            ChiamataBold chiamata = new ChiamataBold(1);
            Log.i("info" , "contatore  --> " + contatoreBlue);
            chiamaBluetooth();
        }
    }

    private static void scomapatta() {

//        int i=listPrimaria.size();
  //      int h=0;
        if(fine.equals("misurazioni")){
            AggiungiMisurazioni.scompattaBluetooth();
        }
        if(fine.equals("localizzazione")){
            PrincipaleLocalizzati.prendiDatiBluetooth();
        }
    }

    public LinkedList getLista(int i){
        return listPrimaria.get(i);
    }

    public int   getLunghezza(){
        return listPrimaria.size();
    }
}
