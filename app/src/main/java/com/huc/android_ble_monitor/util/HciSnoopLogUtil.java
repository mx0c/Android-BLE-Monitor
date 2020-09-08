package com.huc.android_ble_monitor.util;

import androidx.annotation.Nullable;
import com.huc.android_ble_monitor.models.AttPacket;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.models.L2capPacket;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class HciSnoopLogUtil implements IHciDecoder {
    private String mRawSnoopLog;
    private String mLogFilepath;
    private String TAG = this.getClass().getSimpleName();
    private String BTSTACK_CONFIG_FILE = "bt_stack.conf";
    private String BTSTACK_CONFIG_PATH = "/etc/bluetooth/" + BTSTACK_CONFIG_FILE;
    private String BTSNOOP_FALLBACK_FILE = "btsnoop_hci.log";
    private String BTSNOOP_FALLBACK_PATH = "/sdcard/" + BTSNOOP_FALLBACK_FILE;
    private IPacketReceptionCallback mCallback;
    private int mReadLastPacketCount = 1000;
    private int mHciSnoopLogPacketCount;

    static {
        System.loadLibrary("hciviewer");
    }

    public HciSnoopLogUtil(IPacketReceptionCallback cb){
        setPacketReceptionCb(cb);
        readSnoopLog();
        startHciLogStream(BTSNOOP_FALLBACK_PATH, mReadLastPacketCount);
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
     * @return log filepath
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
}