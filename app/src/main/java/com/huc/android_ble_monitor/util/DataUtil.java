package com.huc.android_ble_monitor.util;

import com.huc.android_ble_monitor.StaticContext;
import com.huc.android_ble_monitor.models.NameInformation;
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
public class DataUtil {
    static private HashMap<String,NameInformation> mCharacteristicUuidToNameInformationMap = loadCharacteristicUuidToNameInformationMap();
    static private HashMap<String,NameInformation> mServiceUuidToNameInformationMap = loadServiceUuidToNameInformationMap();
    static private HashMap<String,NameInformation> mDeclarationUuidToNameInformationMap = loadDeclarationUuidToNameInformationMap();
    static private HashMap<String,NameInformation> mDescriptorUuidToNameInformationMap = loadDescriptorUuidToNameInformationMap();
    static private HashMap<Integer, String> mManufacturerIdToNameMap = loadManufacturerIdToStringMap();

    public final static String UNKNOWN_UUID = "UNKNOWN UUID";
    public final static String UNKNOWN_MANUFACTURER ="UNKNOWN MANUFACTURER";

    static public NameInformation resolveUuidToNameInformation(String uuid){
        NameInformation ni = mCharacteristicUuidToNameInformationMap.get(uuid);
        if(ni != null){
            return ni;
        }

        ni = mDeclarationUuidToNameInformationMap.get(uuid);
        if(ni != null){
            return ni;
        }

        ni = mDescriptorUuidToNameInformationMap.get(uuid);
        if(ni != null){
            return ni;
        }

        ni = mServiceUuidToNameInformationMap.get(uuid);
        if(ni != null){
            return ni;
        }else{
            ni = new NameInformation(UNKNOWN_UUID, UNKNOWN_UUID);
            return ni;
        }
    }

    static public String resolveManufacturerId(int id){
        String res = mManufacturerIdToNameMap.get(id);
        if(res != null){
            return res;
        }
        return UNKNOWN_MANUFACTURER;
    }

    /**
     * Used to read in json files from asset folder.
     * @param filename
     * @return JSONArray object if successful otherwise null.
     */
    static public JSONArray loadJSONArrayFromAsset(String filename) {
        try {
            InputStream is = StaticContext.getContext().getAssets().open(filename);
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
     * @return if successful a map with manufacturer ID's as keys and their asociated String representation as values,
     * otherwise null.
     */
    static public HashMap<Integer, String> loadManufacturerIdToStringMap(){
        JSONArray jsonArray = loadJSONArrayFromAsset("company_ids.json");
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
     * This static method can be used to read in the "descriptor_uuids.json" file located in the Assets folder.
     * @return if successful returns a map with service-uuid's as keys and a NameInformation Object as values which consists
     * of a "name" and "identifier" string, otherwise null.
     */
    static public HashMap<String, NameInformation> loadDescriptorUuidToNameInformationMap(){
        JSONArray jsonArray = loadJSONArrayFromAsset("descriptor_uuids.json");
        HashMap<String, NameInformation> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                map.put(obj.getString("uuid"),
                        new NameInformation(
                                obj.getString("name"),
                                obj.getString("identifier")
                        )
                );
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * This static method can be used to read in the "characteristic_uuids.json" file located in the Assets folder.
     * @return if successful returns a map with service-uuid's as keys and a NameInformation Object as values which consists
     * of a "name" and "identifier" string, otherwise null.
     */
    static public HashMap<String, NameInformation> loadCharacteristicUuidToNameInformationMap(){
        JSONArray jsonArray = loadJSONArrayFromAsset("characteristic_uuids.json");
        HashMap<String, NameInformation> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                map.put(obj.getString("uuid"),
                        new NameInformation(
                                obj.getString("name"),
                                obj.getString("identifier")
                        )
                );
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * This static method can be used to read in the "service_uuids.json" file located in the Assets folder.
     * @return if successful returns a map with service-uuid's as keys and a NameInformation Object as values which consists
     * of a "name" and "identifier" string, otherwise null.
     */
    static public HashMap<String, NameInformation> loadServiceUuidToNameInformationMap(){
        JSONArray jsonArray = loadJSONArrayFromAsset("service_uuids.json");
        HashMap<String,NameInformation> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                map.put(obj.getString("uuid"),
                        new NameInformation(
                                obj.getString("name"),
                                obj.getString("identifier")
                        )
                );
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }

    /**
     * This static method can be used to read in the "gatt_declarations_uuids.json" file located in the Assets folder.
     * @return if successful returns a map with service-uuid's as keys and a NameInformation Object as values which consists
     * of a "name" and "identifier" string, otherwise null.
     */
    static public HashMap<String, NameInformation> loadDeclarationUuidToNameInformationMap(){
        JSONArray jsonArray = loadJSONArrayFromAsset("gatt_declarations_uuids.json");
        HashMap<String,NameInformation> map = new HashMap<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject obj = jsonArray.getJSONObject(i);
                String test = obj.keys().next();
                map.put(obj.getString("uuid"),
                        new NameInformation(
                                obj.getString("name"),
                                obj.getString("identifier")
                        )
                );
            }catch (JSONException ex){
                ex.printStackTrace();
                return null;
            }
        }
        return map;
    }
}
