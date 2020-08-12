package com.huc.android_ble_monitor.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogsUtil {
    public static ArrayList<String> readLogs(String filter, String tag) {
        ArrayList<String> res = new ArrayList();
        try {
            String filterParam = buildFilter(filter, tag);
            Process process = Runtime.getRuntime().exec("logcat -d -v brief " + filterParam);
            Log.d("BLEM_Debug", "executed: " + "logcat -d -v brief " + filterParam);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                res.add(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private static String buildFilter(String filter, String tag){
        if(filter.equals("All") && tag.equals("All"))
            return "";
        if(tag.equals("All"))
            tag = "*";
        switch (filter){
            case "Debug":
                return tag + ":" + "D *:S";
            case "All":
                return tag + ":" + "* *:S";
            case "Verbose":
                return tag + ":" + "V *:S";
            case "Info":
                return tag + ":" + "I *:S";
            case "Warning":
                return tag + ":" + "W *:S";
            case "Error":
                return tag + ":" + "E *:S";
            case "Fatal":
                return tag + ":" + "F *:S";
            default:
                return "";
        }
    }
}