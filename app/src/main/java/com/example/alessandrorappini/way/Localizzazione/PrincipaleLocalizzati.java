package com.example.alessandrorappini.way.Localizzazione;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneFrequenzeBluetooth;
import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneFrequenzeBoth;
import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneFrequenzeNetWork;
import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneFrequenzeWifi;
import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneNomiBluetooth;
import com.example.alessandrorappini.way.ChiamateLocalizzazione.ChiamataLocalizzazioneNomiWifi;
import com.example.alessandrorappini.way.Compressori.CompressoreBluetooth;
import com.example.alessandrorappini.way.Compressori.CompressoreWifi;
import com.example.alessandrorappini.way.MainActivity;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothCheif;
import com.example.alessandrorappini.way.Oggetti.Bluetooth.BluetoothObj;
import com.example.alessandrorappini.way.Oggetti.NetWork.NetWorkCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiCheif;
import com.example.alessandrorappini.way.Oggetti.Wifi.WifiObj;
import com.example.alessandrorappini.way.R;
import com.example.alessandrorappini.way.Server.Setpath;
import com.example.alessandrorappini.way.Utilities.OggBoth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.alessandrorappini.way.Misurazioni.Misurazioni.Wifi.WifiAlgo.jsonParser;

    /*
        Classe utilizzare per acquisire le preferenze dell'utente sul fatto di come
        intende localizzarsi, quali tecnologie usare e quele tipo di algoritmo utilizzare
        questa classe istanzia gli oggetti che effettuano le rilevazioni e gli oggetti
        che si occupano di richiamare i file PHP di contronto.
        La prima cosa che la classe fa è popolare lo spinner, il quale all'interno ci andranno il
        nome di tutti gli edifici.

     */

public class PrincipaleLocalizzati extends AppCompatActivity {

    public static Context con;
    public static Intent inte;
    public static boolean selectedDialog= false;

    Spinner  mySpinner;
    static Spinner  spEdificio;
    int lArray;
    JSONArray edificiSel = null;
    String[] spinnerArrayEdificiSel;
    HashMap<String, String> spinnerMapEdificiSel = new HashMap<String, String>();
    String err = "no";

    String nameSelezionatoEficifio;

    static WifiCheif cheifWifi;
    static BluetoothCheif bluetoohCheif;
    static NetWorkCheif netWorkCheif;

    public static int precisione;
    //BOLLEAN che mi gestiscono gli algoritmi da lanciare
    private static Map<String, Boolean> generaleAnalisi ;
    ////////////////////////////////
    static boolean attesaWifi= true;
    static boolean attesaBlue= true;
    static boolean attesaNet= false;

    static int lungStabile;

    static LinkedList wifiCompressi ;
    static LinkedList bluetoothCompressi ;
    //array per invio dati //ssid //bssid // rssidMedia //rssidVarianza *******WIFI
    static LinkedList  ssid = new LinkedList();
    static LinkedList  bssid = new LinkedList();
    static LinkedList  rssidMedia = new LinkedList();

    //array per invio dati //ssid //bssid // rssidMedia //rssidVarianza ******* Bluetooth
    static LinkedList  deviceBluetooth = new LinkedList();
    static LinkedList  rssiBluetooth = new LinkedList();
    //
    static int mediaNetWork;
    //dialog
    static ProgressDialog dialog;
    //PatterMatch nomi
    static  HashMap hashMapNomiWifi = null;
    static  HashMap hashMapNomiBluetooth = null;
    static  HashMap hashMapFrequenzeWifi = null;
    static  HashMap hashMapFrequenzeBluetooth = null;
    static  HashMap hashMapFrequenzeCell = null;
    static  OggBoth frequenzeBoth = null;
    static  HashMap hashMapFrequenzeBoth = null;

    //Boolean Controllo
    //nomi
    static boolean nomiWifi =true;
    static boolean nomiBlue =true;

    static boolean frequenzeWifi =true;
    static boolean frequenzeBlue =true;
    static boolean frequenzeCell =true;
    //both
    /*static boolean nomiWifi =true;
    static boolean nomiBlue =true;*/

    static Context mContext ;
    LinkedList rssidVarianza;
    static JSONArray rpRisp = null;
    static LinkedList rispMachNomiWifi = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principale_localizzati);
        mContext = this;
        mySpinner=(Spinner) findViewById(R.id.spinnerNum);
        new popolaEdificiLoc().execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    class popolaEdificiLoc extends AsyncTask<String, String, String> {
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
                    // in caso di successo
                    edificiSel = json.getJSONArray("edificio");
                    lArray = edificiSel.length();
                    spinnerArrayEdificiSel = new String[edificiSel.length()];
                    for (int i = 0; i < edificiSel.length(); i++) {
                        //prendo il json i-esimo lo decodifico e lo metto detro a temp
                        JSONObject temp = edificiSel.getJSONObject(i);
                        //predno l'id , il nome e il tipo per creare l'oggetto
                        String id = temp.getString("idEdificio");
                        JSONObject reader = new JSONObject(id);
                        String idEdicio = reader.getString("$id");
                        String nome = temp.getString("nome");
                        /*
                            mappa chiave valore dove sono contenuti il nome degli edifici e
                            l'indentificativo dell'edificio all'inteno del database
                         */
                        spinnerMapEdificiSel.put(idEdicio, nome);
                        // registro dove mi segno le posizioni degli elementi all'intendo della mappa
                        spinnerArrayEdificiSel[i] = nome;
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
                Toast.makeText(PrincipaleLocalizzati.this, "Errore caricamento Dati", Toast.LENGTH_LONG).show();
            } else {
                popolaSpinner();
            }
        }
    }
    /*
        classe che mi popola lo spinner
     */
    private void popolaSpinner() {
        spEdificio = (Spinner )  findViewById(R.id.vadoDiSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayEdificiSel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEdificio.setAdapter(adapter);
        nameSelezionatoEficifio = spEdificio.getSelectedItem().toString();

        spEdificio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                nameSelezionatoEficifio = spEdificio.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    public void esci (View view){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void avviaLocalizzati (View view){
        con = getApplicationContext();
        inte = getIntent();
        precisione = Integer.parseInt(mySpinner.getSelectedItem().toString());
        final CheckBox checkBoxWIFI = (CheckBox) findViewById(R.id.ckWIFI);
        final CheckBox checkBoxBluetooth = (CheckBox) findViewById(R.id.ckBluetooth);
        final CheckBox checkBoxNetWork = (CheckBox) findViewById(R.id.ckreteCell);

        final CheckBox checkBoxNomi = (CheckBox) findViewById(R.id.ckWNomi);
        final CheckBox checkBoxFrequenze = (CheckBox) findViewById(R.id.ckFrequenza);
        final CheckBox checkBoxBoth = (CheckBox) findViewById(R.id.ckNetWork);

        if(!checkBoxNomi.isChecked() && !checkBoxFrequenze.isChecked() && !checkBoxBoth.isChecked()){
            Toast.makeText(PrincipaleLocalizzati.this, "Seleziona almeno un pattern di analisi", Toast.LENGTH_LONG).show();
        }else{
                // crea la mappa con deltro le preferenze dell'utente su quale algoritmo utilizzare
                generaleAnalisi= new HashMap<>();
                if(checkBoxNomi.isChecked()){
                    generaleAnalisi.put("nomi" , true) ;
                }else {
                    generaleAnalisi.put("nomi" , false) ;
                }
                if(checkBoxFrequenze.isChecked()){
                    generaleAnalisi.put("frequenze" , true) ;
                }else {
                    generaleAnalisi.put("frequenze" , false) ;
                }
                if(checkBoxBoth.isChecked()){
                    generaleAnalisi.put("both" , true) ;
                }else {
                    generaleAnalisi.put("both" , false) ;
                }
                // rileva le preferenze dell'utente su quali tecnologie usare
                if (checkBoxWIFI.isChecked()) {
                    generaleAnalisi.put("WiFi" , true) ;
                    //localWifi = true;
                    nomiWifi = false;
                    frequenzeWifi = false;
                    //istanzio l'oggetto resposabile delle analisi wifi
                    cheifWifi = new WifiCheif(precisione , con , inte , "localizzazione");
                    selectedDialog = true;
                }else {
                    generaleAnalisi.put("WiFi" , false) ;
                }

                if (checkBoxBluetooth.isChecked()) {
                    generaleAnalisi.put("Bluetootk" , true) ;
                    nomiBlue = false;
                    frequenzeBlue = false;
                    // istanzio l'oggetto responsabile dell'analisi del bluetooth
                    bluetoohCheif = new BluetoothCheif(precisione , con , inte  , "localizzazione");
                    //localBlue = true;
                    selectedDialog = true;
                }else {
                    generaleAnalisi.put("Bluetootk" , false) ;
                }

                if (checkBoxNetWork.isChecked()) {
                    generaleAnalisi.put("NetWork" , true) ;
                    frequenzeCell = false;
                    //istanzio l'oggetto responsabile delle rilevazioni delle rete cellulare
                    netWorkCheif = new NetWorkCheif(precisione , con , inte  , "localizzazione");
                    //localBlue = true;
                    selectedDialog = true;
                }else {
                    generaleAnalisi.put("NetWork" , false) ;
                }

                if (selectedDialog== true){
                 dialog = ProgressDialog.show(this, "Analisi", "Attendere prego", true);
                }else {
                    Toast.makeText(PrincipaleLocalizzati.this, "Seleziona almeno uno strumento di analisi", Toast.LENGTH_LONG).show();
                }
        }
    }

    //inserisco le rilevazioni del WiFi dentro la lista dell'oggetto istazianto mediante l'apposito metodo
    public synchronized  static  void inserisciCheifWiFiLocalizzazione(LinkedList lista){
        cheifWifi.inserisci(lista);
    }
    //inserisco le rilevazioni del Bluetooth dentro la lista dell'oggetto istazianto mediante l'apposito metodo
    public synchronized  static  void inserisciCheifBlueLocalizzazione(LinkedList lista){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            bluetoohCheif.inserisci(lista);
        }
    }
    //inserisco le rilevazioni della rete cellulare dentro la lista dell'oggetto istazianto mediante l'apposito metodo
    public synchronized  static  void inserisciCheifNetWordLocalizzazione(int value){
        netWorkCheif.inserisci(value);
    }

    //comprime i dati
    public static void prendiDatiWifi() {
        CompressoreWifi.inizia(cheifWifi);
    }
    //comprime i dati
    public static void prendiDatiBluetooth() {
        CompressoreBluetooth.inizia(bluetoohCheif);
    }
    //comprime i dati
    public static void prendiDatiNetWork() {
        mediaNetWork = netWorkCheif.getMediaNetWork();
        String nome = spEdificio.getSelectedItem().toString();
        ChiamataLocalizzazioneFrequenzeNetWork chiamataLocalizzazioneFrequenzeNetWork =new ChiamataLocalizzazioneFrequenzeNetWork(mediaNetWork , nome);
    }

    // indica al sistema che le rilevazioni WiFi sono completate
    public static void attesaWifi(LinkedList<WifiObj> cheifWifiCompresso) {
        wifiCompressi = cheifWifiCompresso;
        attesaWifi = false;
        creaArrayWifi();
    }
    // indica al sistema che le rilevazioni Bluetooth sono completate
    public static void attesaBluetooth(LinkedList<WifiObj> cheifBluetoothCompresso) {
        bluetoothCompressi = cheifBluetoothCompresso;
        attesaBlue = false;
        creaArrayBluetooth();
    }

    //viene creato l'array
    private static void creaArrayBluetooth() {
        String nome = spEdificio.getSelectedItem().toString();
        BluetoothObj appoggio;
        for (int i = 0; i < bluetoothCompressi.size(); i++) {
            appoggio = (BluetoothObj) bluetoothCompressi.get(i);
            deviceBluetooth.add(appoggio.getDevice());
            rssiBluetooth.add( String.valueOf(appoggio.getRssiMedia()));
        }
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ChiamataLocalizzazioneNomiBluetooth chiamaBluetoothNomi = new ChiamataLocalizzazioneNomiBluetooth(deviceBluetooth , rssiBluetooth , nome);
        ChiamataLocalizzazioneFrequenzeBluetooth chiamaBluetoothFrequenze = new ChiamataLocalizzazioneFrequenzeBluetooth(deviceBluetooth , rssiBluetooth , nome);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    //viene creato l'array
    private static void creaArrayWifi() {
        int lung = wifiCompressi.size();
        lungStabile = lung;
        for (int i = 0; i < wifiCompressi.size(); i++) {
            WifiObj appoggio = (WifiObj) wifiCompressi.get(i);

            ssid.add(appoggio.getSsid());
            bssid.add(appoggio.getBssid());
            rssidMedia.add( String.valueOf(appoggio.getMediaRssi()));
        }
        String nome = spEdificio.getSelectedItem().toString();
        Log.i("nome" , nome);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        ChiamataLocalizzazioneNomiWifi chiamataWifiNomi = new ChiamataLocalizzazioneNomiWifi(ssid , bssid , rssidMedia , lung , nome);
        ChiamataLocalizzazioneFrequenzeWifi chiamataFrequenzeWifi = new ChiamataLocalizzazioneFrequenzeWifi(ssid , bssid , rssidMedia , lung , nome);
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    /*
        metodo richiamato dalla classe ChiamataLocalizzazioneNomiWifi inserire i dati analizzati dentro la mappa
     */
    public static synchronized    void inserisciHasMapNomiWifi(HashMap map){
        nomiWifi = true;
        hashMapNomiWifi = map;
        fine();
    }

    /*
        metodo richiamato dalla classe ChiamataLocalizzazioneNomiBluetooth inserire i dati analizzati dentro la mappa
     */
    public static synchronized    void inserisciHasMapNomiBluetooth(HashMap map){
        nomiBlue = true;
        hashMapNomiBluetooth = map;
        fine();
    }

    /*
      metodo richiamato dalla classe ChiamataLocalizzazioneFrequenzeWifi inserire i dati analizzati dentro la mappa
     */
    public static synchronized    void inserisciHasMapFrequenzeWifi(HashMap map){
        frequenzeWifi = true;
        hashMapFrequenzeWifi = map;
        fine();
    }

    /*
      metodo richiamato dalla classe ChiamataLocalizzazioneFrequenzeBluetooth inserire i dati analizzati dentro la mappa
     */
    public static synchronized    void inserisciHasMapFrequenzeBluetooth(HashMap map){
        frequenzeBlue = true;
        hashMapFrequenzeBluetooth = map;
        fine();
    }
    /*
      metodo richiamato dalla classe ChiamataLocalizzazioneFrequenzeNetWork inserire i dati analizzati dentro la mappa
     */
    public static synchronized    void inserisciHasMapFrequenzeNetWork(HashMap map){
        frequenzeCell = true;
        hashMapFrequenzeCell = map;
        fine();
    }


     synchronized static void fine() {
        if (nomiWifi == true && nomiBlue == true && frequenzeWifi == true && frequenzeBlue == true && frequenzeCell == true){
            //generaleAnalisi --> la mappa con dentro i valori che l'utente ha deciso di visualizzare
            //hashMapNomiBluetooth  --> mappa con dentro i nomi rilevati dalla frequenza bluetooth
            //hashMapNomiWifi --> mappa con dentro i nomi rilevati dalla frequenza bluetooth

            //WiFi Bluetootk NetWork
            if(generaleAnalisi.get("both") && generaleAnalisi.get("WiFi") && generaleAnalisi.get("Bluetootk") && generaleAnalisi.get("NetWork")){
                    //facccio l'analisi
                    // poi mando via
                    Log.i("info" , "faccio l'analisi");
                    String nome = spEdificio.getSelectedItem().toString();
                    ChiamataLocalizzazioneFrequenzeBoth chiamataLocalizzazioneFrequenzeBoth = new ChiamataLocalizzazioneFrequenzeBoth(nome , ssid , bssid , rssidMedia , deviceBluetooth , rssiBluetooth , mediaNetWork , lungStabile);

            }else  {
                    mandaVia();
            }

        }

    }

    /*
      metodo richiamato dalla classe ChiamataLocalizzazioneFrequenzeBothk inserire i dati analizzati dentro la mappa
     */

    public static synchronized    void inserisciHasMapFrequenzeBoth(OggBoth map){
        frequenzeBoth = map;
        mandaVia();

    }

    /*
        crea un bundle contente tutte le preferenze dell'utente e le manda alla classe PrincipaleLcalizzati
        in modo tale che sappia cosa l'utente ha richiesto
     */
    private static void mandaVia() {
        {Log.i("info" , "fine");
            Bundle  bundle = new Bundle();
            bundle.putSerializable("generaleAnalisi" , (Serializable) generaleAnalisi);
            bundle.putSerializable("hashMapNomiBluetooth" ,  hashMapNomiBluetooth);
            bundle.putSerializable("hashMapNomiWifi" ,  hashMapNomiWifi);
            bundle.putSerializable("hashMapFrequenzeWifi" ,  hashMapFrequenzeWifi);
            bundle.putSerializable("hashMapFrequenzeBluetooth" ,  hashMapFrequenzeBluetooth);
            bundle.putSerializable("hashMapFrequenzeCell" ,  hashMapFrequenzeCell);
            bundle.putSerializable("hashMapFrequenzeBoth" , (OggBoth) frequenzeBoth);
            dialog.dismiss();
            Context c = getAppContext();
            Intent intent = new Intent(c , Visualizzazione.class);
            intent.putExtras(bundle);
            c.startActivity(intent);}
    }

    public static Context getAppContext(){

        return mContext;
    }



}
