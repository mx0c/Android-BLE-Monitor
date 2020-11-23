package com.huc.android_ble_monitor.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.huc.android_ble_monitor.BuildConfig;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

/**
 * Util Class to read stream of decoded HCI Packets from HCI Snoop Log
 */
public class HciSnoopLogUtil implements IHciDecoder {
    private String TAG = "BLEM_HciSnoopLog";
    private String BTSTACK_CONFIG_FILE = "bt_stack.conf";
    private String BTSTACK_CONFIG_PATH = "/etc/bluetooth/" + BTSTACK_CONFIG_FILE;
    private String BTSNOOP_FALLBACK_FILE = "/btsnoop_hci.log";
    private String BTSNOOP_FALLBACK_PATH = Environment.getExternalStorageDirectory() + BTSNOOP_FALLBACK_FILE;
    private IPacketReceptionCallback mCallback;
    public static String BTSNOOP_PATH;
    private int mReadLastPacketCount = 1000;
    private int mHciSnoopLogPacketCount;

    static {
        System.loadLibrary("hciviewer");
    }

    public HciSnoopLogUtil(IPacketReceptionCallback cb, Context ctx){
        setPacketReceptionCb(cb);
        BTSNOOP_PATH = getSnoopLogLocation();

        // Check if file exists
        File file = new File(BTSNOOP_PATH);
        if(!file.exists()){
            new MaterialAlertDialogBuilder(ctx)
                    .setTitle("Error")
                    .setMessage("Could not find Bluetooth Snoop log file. Make sure to Activate the Option in the Android Developer Settings and your device supports this feature.")
                    .setNeutralButton("Ok", null)
                    .show();
        }else {
            startHciLogStream(BTSNOOP_PATH, mReadLastPacketCount);
        }
    }

    public void restartStreaming(){
        stopHciLogStream();
        startHciLogStream(BTSNOOP_PATH, mReadLastPacketCount);
    }

    @Override
    public native void startHciLogStream(String filePath, int lastPacketCount);

    @Override
    public native void stopHciLogStream();

    /**
     * Returns the HCI Snoop log location from bt_stack.conf file
     * @return log filepath
     */
    @Nullable
    public String getSnoopLogLocation(){
        // check if conf file exists
        File file = new File(this.BTSTACK_CONFIG_PATH);
        if(!file.exists()) return BTSNOOP_FALLBACK_PATH;

        // read in conf file
        Properties prop = new Properties();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return BTSNOOP_FALLBACK_PATH;
        }

        String res = prop.getProperty("btsnoopfilename");
        if(res == null){
            Log.e(TAG, "getSnoopLogLocation: couldn't read btsnoop path from config using fallback path");
            return BTSNOOP_FALLBACK_PATH;
        } else{
            Log.v(TAG, "getSnoopLogLocation: Successfully read Snoop log Location from bt_stack.conf, " + res);
            return res;
        }
    }

    @Override
    public void setPacketReceptionCb(IPacketReceptionCallback cb) {
        mCallback = cb;
    }

    /**
     * callback called from native function when a HCI pakcet has been decoded
     *
     * @param snoopFrame snoop frame part
     * @param hciFrame   HCI packet part
     */
    public void onHciFrameReceived(final String snoopFrame, final String hciFrame) {
        if (mCallback != null) {
            mCallback.onHciFrameReceived(snoopFrame, hciFrame);
        }
    }

    public void onError(int errorCode, String errorMessage) {
        if (mCallback != null) {
            mCallback.onError(errorCode, errorMessage);
        }
    }

    /**
     * callback called from native function when packet count is finished
     *
     * @param packetCount total number of HCI packet available
     */
    public void onFinishedPacketCount(int packetCount) {
        mHciSnoopLogPacketCount = packetCount;
        if (mCallback != null) {
            mCallback.onFinishedPacketCount(packetCount);
        }
    }

    /**
     * Creates a json representation of an array containing the hci snoop packets
     * @param packets L2Cap / ATT / HCI Packets
     * @param <T>
     * @return
     */
    public <T> String serializePackets(ArrayList<T> packets) {
        Gson gson = new Gson();
        return gson.toJson(packets);
    }


    public File writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "hci");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            return gpxfile.getAbsoluteFile();
        } catch (Exception e){
            Log.e(TAG, "Failed writing file " + sFileName + " to internal storage " + dir, e);
        }
        return null;
    }

    public <T> Uri getSharableUriForBlePackets(Context mContext, String sFileName, ArrayList<T> packets) {
        String jsonHciPackets = serializePackets(packets);
        File dir = writeFileOnInternalStorage(mContext, sFileName, jsonHciPackets);
        Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(mContext), BuildConfig.APPLICATION_ID + ".provider", dir.getAbsoluteFile());
        return uri;
    }
}
