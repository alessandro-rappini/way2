package com.example.alessandrorappini.way.Localizzazione;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessandrorappini.way.Compressori.CompressoreWifi;
import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.NetWork.NetWorkCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;
import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Server.Setpath;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo.jsonParser;

public class PrincipaleLocalizzati extends AppCompatActivity {

    public static Context con;
    public static Intent inte;
    public static boolean selectedDialog= false;

    Spinner  mySpinner;

    static WifiCheif cheifWifi;
    static BluetoothCheif bluetoohCheif;
    static NetWorkCheif netWorkCheif;

    public static int precisione;

    static boolean attesaWifi= true;
    static boolean attesaBlue= false;
    static boolean attesaNet= false;

    static LinkedList wifiCompressi ;
    //array per invio dati //ssid //bssid // rssidMedia //rssidVarianza
    static LinkedList  ssid = new LinkedList();
    static LinkedList  bssid = new LinkedList();
    static LinkedList  rssidMedia = new LinkedList();
    LinkedList rssidVarianza;


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
        //lavoriamo qui
        int i=0;
        Log.i("info" , "siamo dentro il prendiDatiWifi");
        Log.i("info" , "la lunghezza è --> " + cheifWifi.getLunghezza() );
        Log.i("info" , "--------------------------");
        CompressoreWifi.inizia(cheifWifi);
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

    public static void attesaWifi(LinkedList<WifiObj> cheifWifiCompresso) {
        wifiCompressi = cheifWifiCompresso;
        attesaWifi = false;
        creaArrayWifi();


    }

    private static void creaArrayWifi() {
        for (int i = 0; i < wifiCompressi.size(); i++) {
            WifiObj appoggio = (WifiObj) wifiCompressi.get(i);
            int rf=0;
            String a = appoggio.getSsid();
            String b = appoggio.getBssid();
            String c = String.valueOf(appoggio.getMediaRssi());

            ssid.add(appoggio.getSsid());
            bssid.add(appoggio.getBssid());
            rssidMedia.add( String.valueOf(appoggio.getMediaRssi()));
        }
        controlla();
    }


    public synchronized static void controlla() {
        Log.i("info" , " siamo dentro il controlla");
        if (attesaWifi == false && attesaBlue  == false && attesaNet == false){
            new inserisciMisurazioniWifi().execute();;

        }
    }

    private static class inserisciMisurazioniWifi extends AsyncTask<String, String, String> {

        public inserisciMisurazioniWifi( ) {
            Log.i("info " , "inizio a mandare su");
        }

        protected String doInBackground(String... args) {

            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();


            for (int i = 0; i < wifiCompressi.size(); i++) {
                //ssid //bssid // rssidMedia //rssidVarianza
                params.add(new BasicNameValuePair("typeSsid[]", (String) ssid.get(i)));
                params.add(new BasicNameValuePair("typeBssid[]", (String) bssid.get(i)));
                params.add(new BasicNameValuePair("typeRssidMedia[]", (String) rssidMedia.get(i)));

            }

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path+"cointrolloDati.php";

            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.i("ris","risultato");
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("info","OK");
                } else {
                    Log.i("info","ERRORE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void doInBackground(){
            Log.i("invio" , "invio");
        }

        protected void onPostExecute(String file_url) {
            finito();
        }

        public synchronized void finito(){
            fine();
        }
    }

    private static void fine() {
        Log.i("********","********");
        Log.i("finito","ABBIAMO INSERTITO TUTTO");
        Log.i("********","********");

    }
}
