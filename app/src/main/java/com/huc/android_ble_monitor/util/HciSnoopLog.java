package com.huc.android_ble_monitor.util;

import android.util.Log;

import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class HciSnoopLog implements IHciDecoder {
    private String mRawSnoopLog;
    private String mLogFilepath;
    private String TAG = this.getClass().getSimpleName();
    private String BTSTACK_CONFIG_FILE = "bt_stack.conf";
    private String BTSTACK_CONFIG_PATH = "/etc/bluetooth/" + BTSTACK_CONFIG_FILE;
    private String BTSNOOP_FALLBACK_FILE = "btsnoop_hci.log";
    private String BTSNOOP_FALLBACK_PATH = "/sdcard/" + BTSNOOP_FALLBACK_FILE;
    private IPacketReceptionCallback callback;

    static {
        System.loadLibrary("hciviewer");
    }

    public HciSnoopLog(){
        readSnoopLog();
        Log.d(TAG, "HciSnoopLog: Read in snoop Log: " + mRawSnoopLog);
        startHciLogStream(BTSNOOP_FALLBACK_PATH, 1000);
    }

    @Override
    public native void startHciLogStream(String filePath, int lastPacketCount);

    @Override
    public native void stopHciLogStream();

    /**
     * tries to read in snoop log from location specified in bt_stack.conf file
     */
    public void readSnoopLog(){
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
        mLogFilepath = prop.getProperty("btsnoopfilename");
        return mLogFilepath;
    }

    @Override
    public void setPacketReceptionCb(IPacketReceptionCallback cb) {
        callback = cb;
    }

    /**
     * callback called from native function when a HCI pakcet has been decoded
     *
     * @param snoopFrame snoop frame part
     * @param hciFrame   HCI packet part
     */
    public void onHciFrameReceived(final String snoopFrame, final String hciFrame) {
        if (callback != null) {
            callback.onHciFrameReceived(snoopFrame, hciFrame);
        }
    }

    public void onError(int errorCode, String errorMessage) {
        if (callback != null) {
            callback.onError(errorCode, errorMessage);
        }
    }

    /**
     * callback called from native function when packet count is finished
     *
     * @param packetCount total number of HCI packet available
     */
    public void onFinishedPacketCount(int packetCount) {
        if (callback != null) {
            callback.onFinishedPacketCount(packetCount);
        }
    }
}
