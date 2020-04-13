package com.huc.android_ble_monitor.Models;

import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;

import java.util.ArrayList;
import java.util.List;

public class BleDevice {
    public ScanResult mScanResult;
    public List<BluetoothGattService> mServices;

    public BleDevice(ScanResult sr, List<BluetoothGattService> services){
        if(services != null) {
            this.mServices = services;
        }else{
            this.mServices = new ArrayList<>();
        }
        this.mScanResult = sr;
    }
}

