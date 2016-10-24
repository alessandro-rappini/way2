package com.example.alessandrorappini.way.Interazione;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Server.JSONParser;
import com.example.alessandrorappini.way.Server.Setpath;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.alessandrorappini.way.R.id.spinnerEdificio;
import static com.example.alessandrorappini.way.R.id.spinnerRP;
import static com.example.alessandrorappini.way.Utilities.Utilities.getKeyFromValue;

public class AggiungiMisurazioni extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    JSONArray edifici = null;
    JSONArray rpRisp = null;
    String err = "no";
    int lunghezzaArray;

    //spinner
    Spinner sp , spRp;
    String[] spinnerArrayEdifici , spinnerArrayEdificiRp;
    HashMap<String,String> spinnerMapEdifici = new HashMap<String, String>();
    private ArrayAdapter<String> spinnerAdapter;
    boolean spinnerPrimo = true;

    //global
    String  nameSelezionato;
    Boolean errDialog = false;
    //istanzia l'oggetto dialogo
    static Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_misurazioni);

        new popolaEdifici().execute();
    }

    class popolaEdifici extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {

            //creo la lista di valori
            List<NameValuePair> valori = new ArrayList<NameValuePair>();
            valori.add(new BasicNameValuePair("nome", "nome"));
            // creo il path
            Setpath setpath =new Setpath();
            String path = setpath.getPath();
            String url = path+"getNomeEdifici.php";
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
                        spinnerMapEdifici.put(idEdicio ,nome);
                        spinnerArrayEdifici[i] = nome;
                    }
                } else {
                    err="si";
                    Log.i("info" , "non sono presenti edifici");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String file_url) {
            if(err=="si"){
                Toast.makeText(AggiungiMisurazioni.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
                thread.start();
            }else {
                popolaSpinner();
                new popolaReferencePoint().execute();
            }
        }
    }

    private void popolaSpinner() {
        sp=(Spinner) findViewById(spinnerEdificio);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, spinnerArrayEdifici);
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
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
    }

    class popolaReferencePoint extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {

            //creo la lista di valori
            List<NameValuePair> valori = new ArrayList<NameValuePair>();

            String key = (String) getKeyFromValue(spinnerMapEdifici, nameSelezionato);
            valori.add(new BasicNameValuePair("key", key));

            // creo il path
            Setpath setpath =new Setpath();
            String path = setpath.getPath();
            String url = path+"getReferencePoint.php";
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
                        spinnerArrayEdificiRp[i] = nome;
                        }
                } else {
                    err="si";
                    Log.i("info" , "non sono presenti edifici");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String file_url) {
            if(err=="si"){
                Toast.makeText(AggiungiMisurazioni.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
                thread.start();
            }else {
                popolaSpinnerRp();
            }
        }
    }

    private void popolaSpinnerRp() {
        spRp=(Spinner) findViewById(spinnerRP);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, spinnerArrayEdificiRp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRp.setAdapter(adapter);
    }

    Thread thread = new Thread(){
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

    public void esci (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }


    public void start (View view){
        Log.i("nulla" , "nulla");
    }
}
