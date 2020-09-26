package com.huc.android_ble_monitor.models;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.os.ParcelUuid;
import androidx.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class BluLeDevice implements Serializable {
    /**
     * TTL in Milliseconds
     * If the time exceeds and no new advertise-packets were received the device is deleted.
     */
    private static int timeToLive = 10000;

    /**
     * ScanResult which contains the advertised data od this Device
     */
    public ScanResult mScanResult;

    /**
     * ConnectionState of this Device (defined in BluetoothProfile)
     */
    public Integer mConnectionState;

    /**
     * GATT Object to interact with GattService of this Device
     */
    public BluetoothGatt mBluetoothGatt;

    /**
     * Timestamp of last received advertising packets
     */
    public long mTimestamp;

    /**
     * current RSSI of this Device in db/m
     */
    public Integer mCurrentRssi;

    public BluLeDevice(ScanResult sr){
        this.mConnectionState = BluetoothProfile.STATE_DISCONNECTED;
        this.mScanResult = sr;
        mTimestamp = new Date().getTime() + timeToLive;
        mCurrentRssi = sr.getRssi();
    }

    /**
     * Returns the amount of advertised Services
     * @return int
     */
    public int getAdvServiceCount() {
        List<ParcelUuid> listUuids = this.mScanResult.getScanRecord().getServiceUuids();
        return listUuids != null ? listUuids.size() : 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return mScanResult.getDevice().getAddress().equals(((BluLeDevice)obj).mScanResult.getDevice().getAddress());
    }
}

