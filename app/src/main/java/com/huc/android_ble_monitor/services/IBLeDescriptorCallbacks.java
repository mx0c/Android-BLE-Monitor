package com.huc.android_ble_monitor.services;

import android.bluetooth.BluetoothGattDescriptor;

public interface IBLeDescriptorCallbacks {
    void onDescriptorRead(BluetoothGattDescriptor descriptor);
    void onDescriptorWrite(BluetoothGattDescriptor descriptor);
}
