package com.huc.android_ble_monitor.services;

import android.bluetooth.BluetoothGattCharacteristic;

public interface IBLeCharacteristicCallbacks {
    void onCharacteristicRead(BluetoothGattCharacteristic characteristic);
    void onCharacteristicWrite(BluetoothGattCharacteristic characteristic);
    void onCharacteristicNotify(BluetoothGattCharacteristic characteristic);
}
