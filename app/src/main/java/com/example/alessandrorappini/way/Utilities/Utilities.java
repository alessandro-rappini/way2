package com.example.alessandrorappini.way.Utilities;

import android.os.Build;
import android.text.TextUtils;

import java.util.Map;

/**
 * Classe creata per posizionare quei metodi che si richiamano in più classi
 * senza che abbino una collocazione fissa.
 * è riportata la fonete da dove sono stati copiati.
 */

public class Utilities {

    //http://stackoverflow.com/questions/8112975/get-key-from-a-hashmap-using-the-value
    // ritorna la posizione di un oggetto all'interno di una mappa
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    // ritorna il tipo di sistema operativo installato sul dispositivo
    public static String so() {
        return Build.VERSION.RELEASE ;
    }

    //http://stackoverflow.com/questions/1995439/get-android-phone-model-programmatically
    // ritorna il tipo di dispositivo utilizzato , nome del produtore e tipi di modello
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }
}
