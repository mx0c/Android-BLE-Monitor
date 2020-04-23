package com.huc.android_ble_monitor.models;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BleDevice implements Serializable {
    public ScanResult mScanResult;
    public Integer mConnectionState;
    public BluetoothGatt mBluetoothGatt;

    public BleDevice(ScanResult sr){
        this.mScanResult = sr;
    }

    public int getServiceCount() {
        List<ParcelUuid> listUuids = this.mScanResult.getScanRecord().getServiceUuids();
        return listUuids != null ? listUuids.size() : 0;
    }
}

