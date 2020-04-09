package com.huc.android_ble_monitor.Models;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.Nullable;

import java.io.UnsupportedEncodingException;
import java.util.List;

public class BleDevice {
    public BluetoothDevice mDevice;
    public List<ParsedAdvertisingPacket> mParsedAdPacket;
    public int rssi;

    public BleDevice(BluetoothDevice device, List<ParsedAdvertisingPacket> adPackets, int rssi){
        this.mDevice = device;
        this.mParsedAdPacket = adPackets;
        this.rssi = rssi;
    }

    /**
     * More informations: <a href="https://stackoverflow.com/questions/26290640/android-bluetoothdevice-getname-return-null"></a>
     * @return returns Name from advertised Data, if not included in advertised data returns "unknown"
     */
    public String getNameFromAdvPackets(){
        for (ParsedAdvertisingPacket pap: mParsedAdPacket) {
            if(pap.mAdvType == 0x09){
                try {
                    return new String(pap.mAdvData, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    return "unknown";
                }
            }
        }
        return "unknown";
    }
}
