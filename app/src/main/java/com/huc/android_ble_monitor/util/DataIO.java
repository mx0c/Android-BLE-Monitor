package com.huc.android_ble_monitor.util;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * This class can be used to read in json files located in the assets folder. They contain f.e. useful
 * informations about standardized characteristics and services. Source: https://github.com/NordicSemiconductor/bluetooth-numbers-database
 */
public class DataIO {

    /**
     * Used to read in json files from asset folder.
     * @param context
     * @param filename
     * @return JSONArray object if successful otherwise null.
     */
    static public JSONArray loadJSONArrayFromAsset(Context context, String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONArray(json);
        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * This static method can be used to read in the "company_ids.json" file located in the Assets folder.
     * @param ctx
     * @return if successful a map with manufacturer ID's as keys and their asociated String representation as values,
     * otherwise null.
     */
    static public HashMap<Integer, String> loadManufacturerIdToStringMap(Context ctx){
        JSONArray jsonArray = loadJSONArrayFromAsset(ctx, "company_ids.json");
        HashMap<Integer, String> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                map.put(obj.getInt("code"), obj.getString("name"));
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * This static method can be used to read in the "characteristic_uuids.json" file located in the Assets folder.
     * @param ctx
     * @return if successful a map with characteristic-uuid's as keys and a second map as value which consists
     * of "name" and "identifier" string as available keys and their asociated values, otherwise null.
     */
    static public HashMap<String, HashMap<String,String>> loadCharacteristicData(Context ctx){
        JSONArray jsonArray = loadJSONArrayFromAsset(ctx, "characteristic_uuids.json");
        HashMap<String,HashMap<String,String>> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                HashMap<String,String> submap = new HashMap<String, String>();
                submap.put("name",obj.getString("name"));
                submap.put("identifier",obj.getString("identifier"));
                map.put(obj.getString("uuid"), submap);
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * This static method can be used to read in the "service_uuids.json" file located in the Assets folder.
     * @param ctx
     * @return if successful a map with service-uuid's as keys and a second map as values which consists
     * of "name" and "identifier" string as available keys and their asociated values, otherwise null.
     */
    static public HashMap<String, HashMap<String,String>> loadServiceData(Context ctx){
        JSONArray jsonArray = loadJSONArrayFromAsset(ctx, "service_uuids.json");
        HashMap<String,HashMap<String,String>> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                HashMap<String,String> submap = new HashMap<String, String>();
                submap.put("name",obj.getString("name"));
                submap.put("identifier",obj.getString("identifier"));
                map.put(obj.getString("uuid"), submap);
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

}
