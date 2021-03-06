package com.example.alessandrorappini.way.Interazione;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.R;

/*
    Menu principale nella parte di interazione con il database.
    In questa parte l'utente può scegliere come interagire col db
    Le scelte sono :
    -   inseriemnto di un nuovo edificio
    -   inserimento di un nuovo reference point
    -   rilevazone dei dati
    -   uscire dal menu di scelta
 */
public class MenuInterazione extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_interazione);
    }

    public void esci (View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }

    public void aggiungiEdificio (View view){
        Intent intent = new Intent(this , AggiungiEdificio.class);
        startActivity(intent);
    }

    public void aggiungiReferencePoint (View view){
        Intent intent = new Intent(this , AggiungiReferencePoint.class);
        startActivity(intent);
    }

    public void aggiungiRilevazioni (View view){
        Intent intent = new Intent(this , AggiungiMisurazioni.class);
        startActivity(intent);
    }


}
