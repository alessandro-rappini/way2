package com.example.alessandrorappini.way.Localizzazione;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Utilities.OggBoth;

import java.util.HashMap;
import java.util.LinkedList;

public class Visualizzazione extends AppCompatActivity {

    static TextView textNomiWifi , textNomiBluetooth , textFrequenzeWifi ,textFrequenzeBluetooth ,textFrequenzeCell , textAlgoBest , textAlgoBestFrequenze ;

    static HashMap decisioni;
    static  HashMap hashMapNomiWifi = null;
    static  HashMap hashMapNomiBluetooth = null;
    static  HashMap hashMapFrequenzeWifi = null;
    static  HashMap hashMapFrequenzeBluetooth = null;
    static  HashMap hashMapFrequenzeCell = null;
    //static  HashMap hashMapFrequenzeBoth = null;
    static OggBoth oggBoth;
    static Bundle bundle;
    static int totAnalisi;

    static LinkedList  bothList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizzazione);

        bundle = this.getIntent().getExtras();

        decisioni = (HashMap) bundle.getSerializable("generaleAnalisi");
        /*
        hashMapNomiWifi = (HashMap) bundle.getSerializable("hashMapNomiWifi");
        hashMapNomiBluetooth = (HashMap) bundle.getSerializable("hashMapNomiBluetooth");
        hashMapFrequenzeWifi = (HashMap) bundle.getSerializable("hashMapFrequenzeWifi");
        hashMapFrequenzeBluetooth = (HashMap) bundle.getSerializable("hashMapFrequenzeBluetooth");
        */

        textNomiWifi = (TextView)findViewById(R.id.NomiWifi);
        textNomiBluetooth = (TextView)findViewById(R.id.NomiBluetooth);
        textFrequenzeWifi = (TextView)findViewById(R.id.frequenzeWifi);
        textFrequenzeBluetooth = (TextView)findViewById(R.id.frequenzeBluetooth);
        textFrequenzeCell = (TextView)findViewById(R.id.frequenzeCell);
        textAlgoBest = (TextView)findViewById(R.id.textAlgoritmo);
        textAlgoBestFrequenze = (TextView)findViewById(R.id.textAlgoritmoF);
        bothList = new LinkedList();

        visualizza();
    }

    public void esci (View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }

    private void visualizza() {
        boolean nomi = (boolean) decisioni.get("nomi");
        if(nomi == true){
            boolean nomiWifi = (boolean) decisioni.get("WiFi");
            if (nomiWifi){
                hashMapNomiWifi = (HashMap) bundle.getSerializable("hashMapNomiWifi");
                int o=0;
                o++;
                String miglioreNomiWifi = dammiIlMigliore(hashMapNomiWifi , "nomiWifi");
                textNomiWifi.setText("Wifi : " + miglioreNomiWifi);
            }
            boolean nomiBluetoot = (boolean) decisioni.get("Bluetootk");
            if (nomiBluetoot){
                hashMapNomiBluetooth = (HashMap) bundle.getSerializable("hashMapNomiBluetooth");
                int o=0;
                o++;
                String miglioreNomiBluetootk = dammiIlMigliore(hashMapNomiBluetooth , "nomiBluetooth");
                textNomiBluetooth.setText("Bluetooth : " + miglioreNomiBluetootk);
            }
        }

        boolean frequenze = (boolean) decisioni.get("frequenze");
        if(frequenze == true){
            boolean frequenzeWifi = (boolean) decisioni.get("WiFi");
            if (frequenzeWifi){
                hashMapFrequenzeWifi = (HashMap) bundle.getSerializable("hashMapFrequenzeWifi");
                int o=0;
                o++;
                String miglioreFrequenzeWifi = dammiIlMigliore(hashMapFrequenzeWifi , "frequenzeWifi");
                textFrequenzeWifi.setText("Wifi : " + miglioreFrequenzeWifi);
            }

            boolean frequenzeBluetootk = (boolean) decisioni.get("Bluetootk");
            if (frequenzeBluetootk){
                hashMapFrequenzeBluetooth = (HashMap) bundle.getSerializable("hashMapFrequenzeBluetooth");
                int o=0;
                o++;
                String miglioreFrequenzeBluetooth = dammiIlMigliore(hashMapFrequenzeBluetooth , "frequenzeBluetooth");
                textFrequenzeBluetooth.setText("Bluetooth : " + miglioreFrequenzeBluetooth);
            }

            boolean frequenzeCell = (boolean) decisioni.get("NetWork");
            if (frequenzeCell){
                hashMapFrequenzeCell = (HashMap) bundle.getSerializable("hashMapFrequenzeCell");
                int o=0;
                o++;
                String miglioreFrequenzeBluetooth = dammiIlMigliore(hashMapFrequenzeCell , "frequenzeCell");
                textFrequenzeCell.setText("NetWork : " + miglioreFrequenzeBluetooth);
            }

            boolean both = (boolean) decisioni.get("both");
            if(both){
                    oggBoth = (OggBoth) bundle.getSerializable("hashMapFrequenzeBoth");
                    textAlgoBest.setText("L'algoritmo migliore è : " + oggBoth.getAlgortimo());
                    textAlgoBestFrequenze.setText("La posizione è : " + oggBoth.getRp());
                   // calcolaIlMigliore();

                }
        }




    }

 /*   private void calcolaIlMigliore() {
        for (int i=0 ; i < bothList.size() ; i++){
            OggettoBoth appoggio = (OggettoBoth) bothList.get(i);
            double ins = (double) totAnalisi;
            appoggio.decrementaMedia(ins);
        }
        visualizzaIlMigliore();
    }*/

   /* private void visualizzaIlMigliore() {
        String bestAlgoritmo = null;
        String bestRp = null;
        double erroreMax = 0; // deve essere il più ALTO possibile
        for (int i=0 ; i < bothList.size(); i++){
            OggettoBoth appoggio = (OggettoBoth) bothList.get(i);
            if (i==0){
                bestAlgoritmo = appoggio.getAlgoritmo();
                bestRp = appoggio.getRp();
                erroreMax = appoggio.getFrequenzaDecrementata();
            }else {
                double erroreMaxAppoggio = appoggio.getFrequenzaDecrementata();
                if (erroreMax < erroreMaxAppoggio){
                    bestAlgoritmo = appoggio.getAlgoritmo();
                    bestRp = appoggio.getRp();
                    erroreMax = appoggio.getFrequenzaDecrementata();
                }
            }
        }

        textAlgoBest.setText("L'algoritmo con meno errore è : " + bestAlgoritmo);
        textAlgoBestFrequenze.setText("La posizione è : " + bestRp);


    }*/

    private String dammiIlMigliore(HashMap hashMapNomiMetodoUniversale , String algoritmo) {
        String best = null;
        int registro = 0;
        LinkedList prov = new LinkedList();
        for ( Object key : hashMapNomiMetodoUniversale.keySet() ) {
            prov.add(key);
        }
        for (int i=0 ; i < hashMapNomiMetodoUniversale.size(); i++){
            int intProvvisorio = (int) hashMapNomiMetodoUniversale.get(prov.get(i));
            totAnalisi = totAnalisi + intProvvisorio;
            if (i==0){
                best = (String) prov.get(i);
                registro = intProvvisorio;
            }else {
                if (registro < intProvvisorio){
                    best = (String) prov.get(i);
                    registro = intProvvisorio;
                }
            }
        }
        /*OggettoBoth oggettoBoth = new OggettoBoth(registro , best , algoritmo);
        bothList.add(oggettoBoth);*/


        return best;
    }
}
