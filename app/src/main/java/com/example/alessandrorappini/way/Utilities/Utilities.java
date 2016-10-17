package com.example.alessandrorappini.way.Utilities;

import java.util.Map;

/**
 * Created by Alessandro Rappini on 13/10/2016.
 */

public class Utilities {

    //http://stackoverflow.com/questions/8112975/get-key-from-a-hashmap-using-the-value
    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }
}
