package com.example.alessandrorappini.way.Server;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 * Classe che prende in input l'url alla quale fare l'interrogazione, il tipo di chiamata da effettuare
 * ovvero se si tratta di un POST o di un GET e la lista di valori alla quale fare l'interrogazione .
 * Successivamne ritornandoci un file json il metodo decodifica il file json in un StringBuilder che ciclando in
 * un ciclo while ci estrapola tutti i valrori
 *
 */
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // costruttore
    public  JSONParser() {
    }


    public static JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params) {

        //effettua la chiamata HTTP
        try {

            //se si tratta di un post
            if(method == "POST"){
                //crea un nuovo oggetto httpCLient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                //ci setta dentro l'url che gli passo
                HttpPost httpPost = new HttpPost(url);
                //setta dentro l'url che effettuerà la chiamata i valori che gli passo
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                Log.i("HttpPost" , httpPost.getURI() + "");
                //eseguo la chiamata
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Log.i("httpResponse" , httpResponse.getEntity() + "");
                //predno i valori della chiamata
                HttpEntity httpEntity = httpResponse.getEntity();
                Log.i("httpEntity" , httpEntity.toString() + "");
                // setto dentro lo sream i valori
                is = httpEntity.getContent();

            }else if(method == "GET"){ // se si tratta di un GET
                //crea un nuovo oggetto httpCLient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                // setto dentro l'url i valori in formato utf-8
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                //creo un nuovo oggetto httpGet
                HttpGet httpGet = new HttpGet(url);
                //eseguo la chimata
                HttpResponse httpResponse = httpClient.execute(httpGet);
                //predno i valori della chiamata
                HttpEntity httpEntity = httpResponse.getEntity();
                // setto dentro lo sream i valori
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // decodifico i valori presi in output dalla chiamata
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Errore", "Errore della conversione -> " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("Errore", "Errore nel parse -> " + e.toString());
        }

        return jObj;

    }
}