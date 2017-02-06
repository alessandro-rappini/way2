package com.example.alessandrorappini.way.Interazione;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.alessandrorappini.way.R.layout.dialog_inserimento_rp;
import static com.example.alessandrorappini.way.Utilities.Utilities.getKeyFromValue;

    /*
        classe creata per l'inseriemnto dei Reference Point all'interno dei dataBase
        ogni reference point è associato a un edificio, per rendere tutto il più
        dinamico possibile e facile per l'utente il nome degli edifici sono pre caricati
        dentro un menu a tendina, scelto l'dificio di interesse e inserito il nome del reference
        point all'inteno dell'apposito spazio si può effetuare la chiamata
     */

public class AggiungiReferencePoint extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    JSONArray edifici = null;
    String err = "no";
    int lunghezzaArray;

    //spinner
    Spinner sp;
    String[] spinnerArray;
    HashMap<String,String> spinnerMap = new HashMap<String, String>();

    //global
    String keyIns , nameIns;
    Boolean errDialog = false;

    //istanzia l'oggetto dialogo
    static Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_reference_point);
        // appena creo l'activity faccio subito la chiamata per caricare gli edifici dentro lo spinner
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
                    // successo
                    edifici = json.getJSONArray("edificio");
                    lunghezzaArray = edifici.length();
                    spinnerArray = new String[edifici.length()];
                    for (int i = 0; i < edifici.length(); i++) {
                        //prendo il json i-esimo lo decodifico e lo metto detro a temp
                        JSONObject temp = edifici.getJSONObject(i);
                        //predno l'id , il nome e il tipo per creare l'oggetto
                        String id = temp.getString("idEdificio");

                        JSONObject reader = new JSONObject(id);
                        String idEdicio = reader.getString("$id");
                        String nome = temp.getString("nome");
                        // creo un associazione chiave valore per una mappa
                        // che mi andrà a comporre lo spinner
                        spinnerMap.put(idEdicio ,nome);
                        spinnerArray[i] = nome;
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
                Toast.makeText(AggiungiReferencePoint.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
                thread.start();
            }else {
                popolaSpinner();
            }
        }

    }

    // metodo che popola lo spinner con i dati scaricati dal database
    private void popolaSpinner() {
        sp=(Spinner) findViewById(R.id.spinnerRP);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this ,android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
    }

    public void carica (View view){
        String name = sp.getSelectedItem().toString();
        String key = (String) getKeyFromValue(spinnerMap, name);
        visualizzaDialog(key , name);
    }

    /*
        metodo che apre la dialog che acqusirà il nome del reference point
     */
    private void visualizzaDialog(final String key, final String name) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(dialog_inserimento_rp);
        // bottone che chiude la dialog
        Button dialogEsci = (Button) dialog.findViewById(R.id.btn_nascondiDialog);
        dialogEsci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // bottone che serve per confermare l'invio
        // si occupa anche dei quisire la stringa (nome reference point) che andremo a inserire
        Button bottone = (Button) dialog.findViewById(R.id.btn_inserisci);
        bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eMail=(EditText)dialog.findViewById(R.id.edit_user);
                nameIns=eMail.getText().toString();
                keyIns = key;
                //avvia il metodo per la chiamata al server
                new inserisciRP().execute();
                ;
            }
        });
        dialog.show();
    }

    private class inserisciRP extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", keyIns));
            params.add(new BasicNameValuePair("nome", nameIns));
            // creo il path
            Setpath setpath =new Setpath();
            String path = setpath.getPath();
            String url = path+"inserisciReferencePoint.php";
            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.d("Server ", json.toString());
            try {
                int success = json.getInt("successo");
                if (success != 1) {
                    errDialog = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            dialog.dismiss();
            if(errDialog == false){
                Toast tea = Toast.makeText(getApplicationContext(), "Reference point inserito correttamente", Toast.LENGTH_LONG);
                tea.show();
                threadCambio.start();
            }else {
                Toast tea = Toast.makeText(getApplicationContext(), "Errore inserimento reference point", Toast.LENGTH_LONG);
                tea.show();
                threadCambio.start();
            }
        }
    }

    Thread thread = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                AggiungiReferencePoint.this.finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    Thread threadCambio = new Thread(){
        @Override
        public void run() {
            try {
                Thread.sleep(500);
                Intent intent = new Intent(getApplicationContext() , MenuInterazione.class);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    //pulsare esci che mi ritorna al menu di interazione col database
    public void esci (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }
}
