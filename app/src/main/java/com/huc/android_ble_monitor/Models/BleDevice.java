package com.huc.android_ble_monitor.Models;

import android.bluetooth.BluetoothDevice;

import java.util.List;

public class BleDevice {
    public BluetoothDevice mDevice;
    public List<ParsedAdvertisingPacket> mParsedAdPacket;

    public BleDevice(BluetoothDevice device, List<ParsedAdvertisingPacket> adPackets){
        this.mDevice = device;
        this.mParsedAdPacket = adPackets;
    }
}
