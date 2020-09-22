package com.huc.android_ble_monitor.util;

import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LogsUtil {
    public static ArrayList<Pair<String,String>> readLogs(String filter) {
        ArrayList<Pair<String, String>> res = new ArrayList();
        try {
            String filterParam = "*:" + filter.charAt(0);
            Process process = Runtime.getRuntime().exec("logcat -d -v time " + filterParam);
            Log.d("BLEM_Debug", "executed: " + "logcat -d -v time " + filterParam);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if(line.contains("-----") || !line.contains("BLEM"))
                    continue;
                String[] splitted = line.split(" ");
                String time = splitted[0] + " " + splitted[1];
                String log = line.replace(time, "");
                Pair p = new Pair(time, log);
                res.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}