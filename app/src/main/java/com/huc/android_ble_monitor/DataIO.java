package com.huc.android_ble_monitor;

import android.content.Context;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataIO {

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
            }
        }
        return map;
    }

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
            }
        }
        return map;
    }

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
            }
        }
        return map;
    }

}
