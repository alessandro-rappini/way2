package com.example.alessandrorappini.way.Localizzazione;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.NetWork.NetWorkCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.R;

import java.util.LinkedList;

public class PrincipaleLocalizzati extends AppCompatActivity {

    public static Context con;
    public static Intent inte;
    public static boolean selectedDialog= false;

    Spinner  mySpinner;

    static WifiCheif cheifWifi;
    static BluetoothCheif bluetoohCheif;
    static NetWorkCheif netWorkCheif;

    public static int precisione;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale_localizzati);
        mySpinner=(Spinner) findViewById(R.id.spinnerNum);
    }

    public void esci (View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void avviaLocalizzati (View view){
        precisione = Integer.parseInt(mySpinner.getSelectedItem().toString());
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
            cheifWifi = new WifiCheif(precisione , con , inte , "localizzazione");
        }

        if (localBlue == true){
            bluetoohCheif = new BluetoothCheif(precisione , con , inte  , "localizzazione");
        }

        if(localNet == true){
            netWorkCheif = new NetWorkCheif(precisione , con , inte , "localizzazione");
        }
    }

    public synchronized  static  void inserisciCheifWiFiLocalizzazione(LinkedList lista){
        cheifWifi.inserisci(lista);
    }

    public synchronized  static  void inserisciCheifBlueLocalizzazione(LinkedList lista){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BluetoothCheif.inserisci(lista);
        }
    }

    public synchronized  static  void inserisciCheifNetWordLocalizzazione(int value){
        NetWorkCheif.inserisci(value);
    }

    public static void prendiDatiWifi() {
        Log.i("info" , "siamo dentro il prendiDatiWifi");
        Log.i("info" , "la lunghezza è --> " + cheifWifi.getLunghezza() );
        Log.i("info" , "--------------------------");
    }


    public static void prendiDatiBluetooth() {
        Log.i("info" , "siamo dentro il prendiDatiBluetooth");
        Log.i("info" , "la lunghezza è --> " + bluetoohCheif.getLunghezza() );
        Log.i("info" , "--------------------------");
    }

    public static void prendiDatiNetWork() {
        Log.i("info" , "siamo dentro il prendiDatiNetWork");
        Log.i("info" , "--------------------------");
    }


}
