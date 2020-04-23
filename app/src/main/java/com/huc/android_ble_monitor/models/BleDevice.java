package com.huc.android_ble_monitor.models;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BleDevice implements Serializable {
    public ScanResult mScanResult;
    public BluetoothGatt mBluetoothGatt;
    public List<BluetoothGattService> mServices;

    public BleDevice(ScanResult sr, List<BluetoothGattService> services){
        if(services != null) {
            this.mServices = services;
        }else{
            this.mServices = new ArrayList<>();
        }
        this.mScanResult = sr;
    }

    public int getServiceCount() {
        List<ParcelUuid> listUiids = this.mScanResult.getScanRecord().getServiceUuids();

        return listUiids != null ? listUiids.size() : 0;
    }

    public ScanResult getmScanResult() {
        return mScanResult;
    }

    public void setmScanResult(ScanResult mScanResult) {
        this.mScanResult = mScanResult;
    }

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }

    public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
        this.mBluetoothGatt = mBluetoothGatt;
    }

    public List<BluetoothGattService> getmServices() {
        return mServices;
    }

    public void setmServices(List<BluetoothGattService> mServices) {
        this.mServices = mServices;
    }
}

