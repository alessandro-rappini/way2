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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Bluetooth.BluetoothObjAsyncTask;
import com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo;
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
   // WifiManager  wifi;
    //String wifis[];
    //WifiScanReceiver wifiReciever;

    public static Context con;
    public static Intent inte;



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

        con = getApplicationContext();
        inte = getIntent();
        if (checkBoxWIFI.isChecked()) {
            cheifWifi = new WifiCheif(precisione , con , inte);
            }
        if (checkBoxBluetooth.isChecked()) {
            BluetoothObjAsyncTask boat = new BluetoothObjAsyncTask(precisione , con , inte);

        }
    }

    public synchronized  static  void inserisciCheif(LinkedList lista){
        Log.i("inserisci" , "inserisci dentro");
        cheifWifi.inserisci(lista);
    }

    public static void scopatta() {
        WifiAlgo.inizia(cheifWifi);
    }
}



