package com.example.alessandrorappini.way.Oggetti.Bluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

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

    static int contatore =0 ;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BluetoothCheif(int pr , Context c, Intent i){
        precisione = pr;
        con = c ;
        inte = i;

        listPrimaria = new LinkedList<>();
        ChiamataBold chiamata = new ChiamataBold(0);
        chiamaBluetooth();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void chiamaBluetooth() {
        contatore++;
        BluetoothObjTask boat = new BluetoothObjTask(con);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void inserisci(LinkedList lista) {

        listPrimaria.add(lista);
        if(contatore == precisione){
            Log.i("*****" , "abbiamoFinito");
        }else {
            ChiamataBold chiamata = new ChiamataBold(1);
            chiamaBluetooth();
        }
    }
}
