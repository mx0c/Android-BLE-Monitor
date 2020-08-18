package com.huc.android_ble_monitor.util;

import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HciSnoopLog {
    private String mRawSnoopLog;
    private String BTSTACK_CONFIG_FILE = "bt_stack.conf";
    private String BTSTACK_CONFIG_PATH = "/etc/bluetooth/" + BTSTACK_CONFIG_FILE;
    private String BTSNOOP_FALLBACK_FILE = "btsnoop_hci.log";
    private String BTSNOOP_FALLBACK_PATH = "/sdcard/" + BTSNOOP_FALLBACK_FILE;

    public HciSnoopLog(){
        pullSnoopLog();
    }

    /**
     * tries to read in snoop log from location specified in bt_stack.conf file
     */
    public void pullSnoopLog(){
        String location = getSnoopLogLocation();
        if(location == null){
            location = BTSNOOP_FALLBACK_PATH;
        }

        //read in snoop log file
        try {
            FileInputStream fis = new FileInputStream(new File(location));
            StringBuilder sb = new StringBuilder();
            while(fis.available() > 0) {
                sb.append((char)fis.read());
            }
            fis.close();
            this.mRawSnoopLog = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the HCI Snoop log location from bt_stack.conf file
     * @return
     */
    @Nullable
    public String getSnoopLogLocation(){
        // check if conf file exists
        File file = new File(this.BTSTACK_CONFIG_PATH);
        if(!file.exists()) return null;

        // read in conf file
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return prop.getProperty("btsnoopfilename");
    }
}
