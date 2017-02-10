package com.example.alessandrorappini.way.Localizzazione;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.R;

import java.util.HashMap;
import java.util.LinkedList;

public class Visualizzazione extends AppCompatActivity {

    static TextView textNomiWifi , textNomiBluetooth , textFrequenzeWifi;

    static HashMap decisioni;
    static  HashMap hashMapNomiWifi = null;
    static  HashMap hashMapNomiBluetooth = null;
    static  HashMap hashMapFrequenzeWifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizzazione);

        Bundle bundle = this.getIntent().getExtras();
        decisioni = (HashMap) bundle.getSerializable("generaleAnalisi");
        hashMapNomiWifi = (HashMap) bundle.getSerializable("hashMapNomiWifi");
        hashMapNomiBluetooth = (HashMap) bundle.getSerializable("hashMapNomiBluetooth");
        hashMapFrequenzeWifi = (HashMap) bundle.getSerializable("hashMapFrequenzeWifi");
        textNomiWifi = (TextView)findViewById(R.id.NomiWifi);
        textNomiBluetooth = (TextView)findViewById(R.id.NomiBluetooth);
        textFrequenzeWifi = (TextView)findViewById(R.id.frequenzeWifi);

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
                String miglioreNomiWifi = dammiIlMigliore(hashMapNomiWifi);
                textNomiWifi.setText(miglioreNomiWifi);
            }
            boolean nomiBluetoot = (boolean) decisioni.get("Bluetootk");
            if (nomiBluetoot){
                String miglioreNomiBluetootk = dammiIlMigliore(hashMapNomiBluetooth);
                textNomiBluetooth.setText(miglioreNomiBluetootk);
            }
        }

        boolean frequenze = (boolean) decisioni.get("frequenze");
        if(frequenze == true){
            boolean frequenzeWifi = (boolean) decisioni.get("WiFi");
            if (frequenzeWifi){
                String miglioreFrequenzeWifi = dammiIlMigliore(hashMapFrequenzeWifi);
                textFrequenzeWifi.setText(miglioreFrequenzeWifi);
            }
            //boolean frequenzeBluetootk = (boolean) decisioni.get("Bluetootk");
        }
/*
        boolean both = (boolean) decisioni.get("both");
        if(nomi == true){
            boolean bothWifi = (boolean) decisioni.get("WiFi");
            boolean bothBluetootk = (boolean) decisioni.get("Bluetootk");
        }*/


    }

    private String dammiIlMigliore(HashMap hashMapNomiMetodoUniversale) {
        String best = null;
        int registro = 0;
        LinkedList prov = new LinkedList();
        for ( Object key : hashMapNomiMetodoUniversale.keySet() ) {
            prov.add(key);
        }
        for (int i=0 ; i < hashMapNomiMetodoUniversale.size(); i++){
            int intProvvisorio = (int) hashMapNomiMetodoUniversale.get(prov.get(i));
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

        return best;
    }
}
