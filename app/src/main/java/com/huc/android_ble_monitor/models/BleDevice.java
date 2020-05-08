package com.huc.android_ble_monitor.models;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class BleDevice implements Serializable {
    //TTL in Milliseconds
    private static int timeToLive = 10000;

    public ScanResult mScanResult;
    public Integer mConnectionState;
    public BluetoothGatt mBluetoothGatt;
    public long mTimestamp;

    public BleDevice(ScanResult sr){
        this.mConnectionState = BluetoothProfile.STATE_DISCONNECTED;
        this.mScanResult = sr;
        mTimestamp = new Date().getTime() + timeToLive;
    }

    public int getServiceCount() {
        List<ParcelUuid> listUuids = this.mScanResult.getScanRecord().getServiceUuids();
        return listUuids != null ? listUuids.size() : 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return mScanResult.getDevice().getAddress().equals(((BleDevice)obj).mScanResult.getDevice().getAddress());
    }
}

