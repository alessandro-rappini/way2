package com.example.alessandrorappini.way;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.alessandrorappini.way.Interazione.MenuInterazione;
import com.example.alessandrorappini.way.Localizzazione.PrincipaleLocalizzati;

/**
 * Main Class.
 * Prima Activity che si apre quando si avvia l'applicazione.
 * La sua unica funzione è quella di far scegliere cosa si vuole fare
 * Se interagire col data base oppure localizzarsi
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void localizzati (View view){
        Intent intent = new Intent(this , PrincipaleLocalizzati.class);
        startActivity(intent);
    }

    public void interagisci (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }


}
