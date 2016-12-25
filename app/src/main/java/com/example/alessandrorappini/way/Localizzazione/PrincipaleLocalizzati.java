package com.example.alessandrorappini.way.Localizzazione;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.R;

public class PrincipaleLocalizzati extends AppCompatActivity {

    public static Context con;
    public static Intent inte;
    public static boolean selectedDialog= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale_localizzati);
    }

    public void esci (View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }

    public void avviaLocalizzati (View view){

        final CheckBox checkBoxWIFI = (CheckBox) findViewById(R.id.ckWIFI);
        final CheckBox checkBoxBluetooth = (CheckBox) findViewById(R.id.ckBluetooth);
        final CheckBox checkBoxNetWork = (CheckBox) findViewById(R.id.ckreteCell);
        boolean localWifi= false;
        boolean localBlue= false;
        boolean localNet= false;
        if (checkBoxWIFI.isChecked()) {
            localWifi = true;
            selectedDialog = true;
        }
        if (checkBoxBluetooth.isChecked()) {
            localBlue = true;
            selectedDialog = true;
        }
        if (checkBoxNetWork.isChecked()) {
            localNet = true;
            selectedDialog = true;
        }

        con = getApplicationContext();
        inte = getIntent();

        if (selectedDialog== true){

        }else {
            Toast.makeText(PrincipaleLocalizzati.this, "Seleziona almeno uno strumento di analisi", Toast.LENGTH_LONG).show();
        }

        if(localWifi == true){
            //cheifWifi = new WifiCheif(precisione , con , inte);
        }

        if (localBlue == true){
            //bluetoohCheif = new BluetoothCheif(precisione , con , inte);
        }

        if(localNet == true){
            //netWorkCheif = new NetWorkCheif(precisione , con , inte);
        }
    }


}
