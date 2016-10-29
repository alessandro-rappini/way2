package com.example.alessandrorappini.way.Interazione;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Server.JSONParser;
import com.example.alessandrorappini.way.Server.Setpath;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AggiungiEdificio extends AppCompatActivity {

    JSONParser jsonParser = new JSONParser();
    String nome ;
    String err="no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_edificio);
    }

    public void esci (View view){
        Intent intent = new Intent(this , MenuInterazione.class);
        startActivity(intent);
    }

    public void invia (View view){
        EditText Enome = (EditText)findViewById(R.id.editText_Nome);
        nome= Enome.getText().toString();

        if(nome.length()!=0){
            new inserisciEdificio().execute();
        }else {
            Toast.makeText(this, "Inserisci il nome dell'edificio", Toast.LENGTH_LONG).show();
        }
    }


    private class inserisciEdificio extends AsyncTask<String, String, String> {
        protected String doInBackground(String... args) {
            //creo la lista con tutti i parametri
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nome", nome));

            // creo il path
            Setpath setpath =new Setpath();
            String path = setpath.getPath();
            String url = path+"inserimentoEdificio.php";

            // svolgo la chiamata
            JSONObject json = jsonParser.makeHttpRequest(url, "POST", params);
            //controllo il risultato
            Log.d("Server ", json.toString());
            try {
                int successo = json.getInt("successo");
                if (successo == 1) {
                    Log.i("info","INSERITO");
                } else if (successo == 2) {
                    Log.i("info","Record già presnte");
                    err="ndd";
                }
                else {
                    err="si";
                    String messaggio = json.getString("messaggio");
                    Log.i("info",messaggio);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            if(err=="no"){
                Toast.makeText(AggiungiEdificio.this, "Edificio inserito correttamente", Toast.LENGTH_LONG).show();
                thread.start();
            } else if (err == "ndd") {
                Toast.makeText(AggiungiEdificio.this, "Record già presente nella collezzione", Toast.LENGTH_LONG).show();
                thread.start();
            }
            else {
                Toast.makeText(AggiungiEdificio.this, "ERRORE INSERIMENTO", Toast.LENGTH_LONG).show();
                thread.start();
            }
        }
        /**
         * thread che mi fa ritornare alla scermata iniziale
         */
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    AggiungiEdificio.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

    }

    public class inserisciMisurazioniWifi {
    }
}
