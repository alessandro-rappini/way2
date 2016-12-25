package com.example.alessandrorappini.way.Interazione;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth.BluetoothAlgo;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.NetWork.NetWorkAlgo;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.NetWork.NetWorkCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Server.JSONParser;
import com.example.alessandrorappini.way.Server.Setpath;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.example.alessandrorappini.way.R.id.spinnerEdificio;
import static com.example.alessandrorappini.way.R.id.spinnerRP;
import static com.example.alessandrorappini.way.R.layout.dialog_misurazioni;
import static com.example.alessandrorappini.way.Utilities.Utilities.getKeyFromValue;

public class AggiungiMisurazioni extends AppCompatActivity {


    public static String edificio , rpSelezionato;
    JSONParser jsonParser = new JSONParser();
    JSONArray edifici = null;
    JSONArray rpRisp = null;
    String err = "no";
    String errNum = "no";
    int lunghezzaArray;

    static WifiCheif cheifWifi;
    static BluetoothCheif bluetoohCheif;
    static NetWorkCheif netWorkCheif;
   // WifiManager  wifi;
    //String wifis[];
    //WifiScanReceiver wifiReciever;

    static Dialog dialogRilevazioni = null;
    static Dialog dialogFine = null;

    public static Context con;
    public static Intent inte;

    public static boolean selectedDialog= false;
    public static boolean wifiDone= true;
    public static boolean blueDone= true;
    public static boolean netWorkDone= true;

    public static int contatoreWiFi = 0;
    public static int contatoreBlue = 0;
    public static int contatoreNetWorke = 0;


    private static Context mContext;


    //spinner
    Spinner sp, spRp , mySpinner;
    String[] spinnerArrayEdifici, spinnerArrayEdificiRp;
    HashMap<String, String> spinnerMapEdifici = new HashMap<String, String>();
    private ArrayAdapter<String> spinnerAdapter;
    Button buttonStart ;

    //global
    String nameSelezionato;
    Boolean errDialog = false;
    //istanzia l'oggetto dialogo
    static Dialog dialog = null;

    public static int precisione;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_misurazioni);

        mContext = this;


        buttonStart = (Button) findViewById(R.id.btnStart) ;
        //wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        //wifiReciever = new WifiScanReceiver();
        mySpinner=(Spinner) findViewById(R.id.spinnerNum);
        //cheifWifi = new WifiCheif();                            ***********************************************************
        new popolaEdifici().execute();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("AggiungiMisurazioni Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }


    class popolaEdifici extends AsyncTask<String, String, String> {

        protected String doInBackground(String... args) {

            //creo la lista di valori
            List<NameValuePair> valori = new ArrayList<NameValuePair>();
            valori.add(new BasicNameValuePair("nome", "nome"));
            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path + "getNomeEdifici.php";
            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", valori);
            //controllo il risultato
            Log.d("Server ", json.toString());

            try {
                int risp = json.getInt("successo");
                if (risp == 1) {
                    edifici = json.getJSONArray("edificio");
                    lunghezzaArray = edifici.length();
                    spinnerArrayEdifici = new String[edifici.length()];

                    for (int i = 0; i < edifici.length(); i++) {
                        //prendo il json i-esimo lo decodifico e lo metto detro a temp
                        JSONObject temp = edifici.getJSONObject(i);
                        //predno l'id , il nome e il tipo per creare l'oggetto
                        String id = temp.getString("idEdificio");

                        JSONObject reader = new JSONObject(id);
                        String idEdicio = reader.getString("$id");
                        String nome = temp.getString("nome");
                        spinnerMapEdifici.put(idEdicio, nome);
                        spinnerArrayEdifici[i] = nome;
                    }
                } else {
                    err = "si";
                    Log.i("info", "non sono presenti edifici");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if (err == "si") {
                Toast.makeText(AggiungiMisurazioni.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
                thread.start();
            } else {
                popolaSpinner();
                new popolaReferencePoint().execute();
            }
        }
    }

    private void popolaSpinner() {
        sp = (Spinner) findViewById(spinnerEdificio);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayEdifici);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        nameSelezionato = sp.getSelectedItem().toString();

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nameSelezionato = sp.getSelectedItem().toString();
                new popolaReferencePoint().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
    }

    class popolaReferencePoint extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {

            //creo la lista di valori
            List<NameValuePair> valori = new ArrayList<NameValuePair>();

            String key = (String) getKeyFromValue(spinnerMapEdifici, nameSelezionato);
            Log.i("keyyyyy","KEY");
            Log.i("key" , key);
            valori.add(new BasicNameValuePair("key", key));

            // creo il path
            Setpath setpath = new Setpath();
            String path = setpath.getPath();
            String url = path + "getReferencePoint.php";
            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", valori);
            //controllo il risultato

            Log.d("Server ", json.toString());
            try {
                int risp = json.getInt("successo");
                if (risp == 1) {
                    rpRisp = json.getJSONArray("value");
                    int lung = rpRisp.length();

                    spinnerArrayEdificiRp = new String[lung];
                    for (int i = 0; i < rpRisp.length(); i++) {
                        String nome = rpRisp.get(i).toString();
                        Log.i("nome" , nome);

                        spinnerArrayEdificiRp[i] = nome;
                    }
                } else if(risp == 35){
                    Log.i("******" , "*****");
                    Log.i("35" , "35");
                    Log.i("******" , "*****");
                    errNum="si";

                } else{
                    err = "si";
                    Log.i("info", "non sono presenti edifici");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        protected void onPostExecute(String file_url) {
            if(errNum=="si"){
                Toast.makeText( AggiungiMisurazioni.this, "Non ci sono reference point per questo edificio!!! INSERISCINE UNO", Toast.LENGTH_LONG).show();
                spRp.setEnabled(false);
                buttonStart.setEnabled(false);
            }else {
                if (err == "si") {
                    Toast.makeText(AggiungiMisurazioni.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
                    thread.start();
                } else {
                    popolaSpinnerRp();
                }
            }
        }
    }

    private void popolaSpinnerRp() {
        spRp = (Spinner) findViewById(spinnerRP);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayEdificiRp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRp.setAdapter(adapter);


    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                AggiungiMisurazioni.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void esci(View view) {
        Intent intent = new Intent(this, MenuInterazione.class);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void start(View view) {
        //edificio selezionato al momento dello strart
        edificio = (String) getKeyFromValue(spinnerMapEdifici, nameSelezionato);
        //
        rpSelezionato = spRp.getSelectedItem().toString();

        precisione = Integer.parseInt(mySpinner.getSelectedItem().toString());
        final CheckBox checkBoxWIFI = (CheckBox) findViewById(R.id.ckWIFI);
        final CheckBox checkBoxBluetooth = (CheckBox) findViewById(R.id.ckBluetooth);
        final CheckBox checkBoxNetWork = (CheckBox) findViewById(R.id.ckreteCell);

        con = getApplicationContext();
        inte = getIntent();
        boolean localWifi= false;
        boolean localBlue= false;
        boolean localNet= false;

        if (checkBoxWIFI.isChecked()) {
            localWifi = true;
            selectedDialog = true;
            wifiDone = false;
        }

        if (checkBoxBluetooth.isChecked()) {
            localBlue = true;
            selectedDialog = true;
            blueDone = false;
        }
        if (checkBoxNetWork.isChecked()) {
            localNet = true;
            selectedDialog = true;
            netWorkDone = false;
        }


        if (selectedDialog== true){
            apriDialog(localWifi , localBlue , localNet);
        }else {
            Toast.makeText(AggiungiMisurazioni.this, "Seleziona almeno uno strumento di analisi", Toast.LENGTH_LONG).show();
        }


        if(localWifi == true){
            cheifWifi = new WifiCheif(precisione , con , inte , "misurazioni");
        }

        if (localBlue == true){
            bluetoohCheif = new BluetoothCheif(precisione , con , inte , "misurazioni");
        }

        if(localNet == true){
            netWorkCheif = new NetWorkCheif(precisione , con , inte , "misurazioni");
        }

    }

    private void apriDialog(boolean bWifi , boolean bBlue  , boolean bNetWork) {

        dialogRilevazioni = new Dialog(this);
        dialogRilevazioni.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogRilevazioni.setCancelable(false);
        dialogRilevazioni.setContentView(dialog_misurazioni);

        TextView precisioneTextView=(TextView)dialogRilevazioni.findViewById(R.id.dialog_precisone);
        String datoDialog =  String.valueOf(precisione);
        precisioneTextView.setText(datoDialog);

        if (bWifi == true){
            TextView TextViewWiFi=(TextView)dialogRilevazioni.findViewById(R.id.contatoreWifi);
            String dato = "in corso";
            TextViewWiFi.setText(dato);
        }

        if (bBlue == true){
            TextView TextViewBlue=(TextView)dialogRilevazioni.findViewById(R.id.contatoreBlue);
            String dato = "in corso";
            TextViewBlue.setText(dato);
        }

        if( bNetWork == true){
            TextView TextViewNetWorg=(TextView)dialogRilevazioni.findViewById(R.id.contatoreNetWork);
            String dato = "in corso";
            TextViewNetWorg.setText(dato);
        }
        dialogRilevazioni.show();
    }

    //--------------------- inserisco dentro l'oggetto principale
    public synchronized  static  void inserisciCheifWiFi(LinkedList lista){
        TextView contatoreTextViewWiFi=(TextView)dialogRilevazioni.findViewById(R.id.contatoreWifi);
        contatoreWiFi ++ ;
        String datoWifi =  String.valueOf(contatoreWiFi);
        contatoreTextViewWiFi.setText(datoWifi);
        cheifWifi.inserisci(lista);
    }

    public static void inserisciCheifBlue(LinkedList lista){
        TextView contatoreTextViewBlue=(TextView)dialogRilevazioni.findViewById(R.id.contatoreBlue);
        contatoreBlue ++ ;
        String datoBlue =  String.valueOf(contatoreBlue);
        contatoreTextViewBlue.setText(datoBlue);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BluetoothCheif.inserisci(lista);
        }
    }

    public static void inserisciCheifNetWord(int value){
        TextView contatoreTextViewNetWorg=(TextView)dialogRilevazioni.findViewById(R.id.contatoreNetWork);
        contatoreNetWorke ++ ;
        String datoNetWork =  String.valueOf(contatoreNetWorke);
        contatoreTextViewNetWorg.setText(datoNetWork);

        NetWorkCheif.inserisci(value);
    }
    //-----------------------------------------------------------

    public static void scopattaWifi() {
        TextView TextViewWiFi=(TextView)dialogRilevazioni.findViewById(R.id.contatoreWifi);
        String messaggio = "terminata l'analisi";
        TextViewWiFi.setText(messaggio);
        wifiDone = true;
        controllaStatoAnalisi();
        WifiAlgo.inizia(cheifWifi);
    }

    public static void scompattaBluetooth () {
        /*int i= bluetoohCheif.getLunghezza();
        Log.i("lunghezza" , "lunghezza " + i);*/
        TextView TextViewBlue=(TextView)dialogRilevazioni.findViewById(R.id.contatoreBlue);
        String messaggio = "terminata l'analisi";
        TextViewBlue.setText(messaggio);
        blueDone = true;
        controllaStatoAnalisi();
        BluetoothAlgo.inizia(bluetoohCheif);
        }

    public static void inviaNetWork() {
        TextView TextViewNetWorg=(TextView)dialogRilevazioni.findViewById(R.id.contatoreNetWork);
        String messaggio = "terminata l'analisi";
        TextViewNetWorg.setText(messaggio);
        netWorkDone = true;
        controllaStatoAnalisi();
        NetWorkAlgo.invia(netWorkCheif);
    }

    public static synchronized void controllaStatoAnalisi(){
        if (wifiDone == true && blueDone == true && netWorkDone== true){
            dialogRilevazioni.dismiss();
            Toast.makeText(mContext, "Terminato l'analisi e l'inserimento", Toast.LENGTH_LONG).show();
            contatoreWiFi = 0;
            contatoreBlue = 0;
            contatoreNetWorke = 0;
            Thread threadFine = new Thread(){
                @Override
                public void run() {
                    try {

                        Thread.sleep(1000);
                        Intent fine = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(fine);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            threadFine.start();
        }
    }



}



