package com.example.alessandrorappini.way;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.alessandrorappini.way.Interazione.MenuInterazione;

public class MainActivity extends AppCompatActivity {
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void localizzati (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }

    public void interagisci (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }


}
