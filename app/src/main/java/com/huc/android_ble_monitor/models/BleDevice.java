package com.huc.android_ble_monitor.models;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



public class BleDevice implements Serializable {
    //TTL in Milliseconds
    private static int timeToLive = 10000;

    public ScanResult mScanResult;
    public Integer mConnectionState;
    public BluetoothGatt mBluetoothGatt;
    public long mTimestamp;
    public Integer mCurrentRssi;

    public BleDevice(ScanResult sr){
        this.mConnectionState = BluetoothProfile.STATE_DISCONNECTED;
        this.mScanResult = sr;
        mTimestamp = new Date().getTime() + timeToLive;
        mCurrentRssi = sr.getRssi();
    }

    public int getAdvertisedServiceCount() {
        List<ParcelUuid> listUuids = this.mScanResult.getScanRecord().getServiceUuids();
        return listUuids != null ? listUuids.size() : 0;
    }

    public int getServiceCount() {
        List<BluetoothGattService> gattServices = this.mBluetoothGatt.getServices();
        return gattServices != null ? gattServices.size() : 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return mScanResult.getDevice().getAddress().equals(((BleDevice)obj).mScanResult.getDevice().getAddress());
    }
}

