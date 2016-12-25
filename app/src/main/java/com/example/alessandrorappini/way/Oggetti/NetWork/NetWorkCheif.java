package com.example.alessandrorappini.way.Oggetti.NetWork;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.alessandrorappini.way.Interazione.AggiungiMisurazioni;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.NetWork.NetWorkObjTask;

import java.util.LinkedList;

/**
 * Created by Alessandro Rappini on 03/12/2016.
 */

public class NetWorkCheif {
    static Context con;
    Intent inte;
    static int precisione;
    static LinkedList<Integer> listPrimaria;
    static int contatoreNetWork =0 ;
    static NetWorkObjTask netWorkObjTask;
    static int netWorkMedia;
    static int netWorkVarianza;
    static int differenza;
    static int scarto = 0 ;


    public NetWorkCheif(int pr , Context c, Intent i , String invio){
        precisione = pr;
        con = c ;
        inte = i;
        listPrimaria = new LinkedList<>();
        Log.i("info" , "vado col primo");
        chiamaNetWork();
    }

    private static void chiamaNetWork() {
        contatoreNetWork++;
        netWorkObjTask = new NetWorkObjTask(con);
    }

    public static void inserisci(int value) {
        Log.i("info" , "volta  --> " + contatoreNetWork);
        Log.i("info" , "Dereguistro il ricevitore ");
        netWorkObjTask.SetUnregisterReceiver();
        Log.i("info" , "abbiamo inserito  "  + value);
        listPrimaria.add(value);
        if(contatoreNetWork == precisione){
            contatoreNetWork =0;
            Log.i("info" , "abbiamo finito chiamo lo scompattatore");
            calcolaMedia();
        }else {
            chiamaNetWork();
        }
    }

    private static void calcolaMedia() {
        if(precisione==1){
            netWorkMedia = listPrimaria.get(0);
            netWorkVarianza = 0;
        }else {
            int somma =0 ;
            int numeroValori;
            numeroValori = listPrimaria.size();
            for (int i=0 ; i< numeroValori ; i++){
                int ap = (int) listPrimaria.get(i);
                somma = somma + ap;
            }
            int media = (somma/numeroValori);
            netWorkMedia = media;


            for (int i=0 ; i< numeroValori ; i++){
                int num = (int) listPrimaria.get(i);
                differenza =  (num - media)^2;
                scarto = scarto + differenza;
                differenza=0;
            }

            //METODO UFFICIALE SECONDO LA STATISTICA
            netWorkVarianza = scarto / (numeroValori-1);
        }

        AggiungiMisurazioni.inviaNetWork();
    }

    public Integer getMediaNetWork() {
        return netWorkMedia;
    }

    public Integer getVarianzaNetWork() {
        return netWorkVarianza;
    }
}
