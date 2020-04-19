package com.huc.android_ble_monitor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.ToastModel;

import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<BleDevice>> mBleDevices = new MutableLiveData<>();
    private MutableLiveData<ToastModel> mToastBroadcast = new MutableLiveData<>();

    private boolean isBluetoothEnabled;

    public void init() {
        isBluetoothEnabled = true;
    }

    public void enableBluetoothScan(Boolean isScanEnabled) {
        if(isScanEnabled) {
            // ToDo: call service and enable scanning
        } else {
            // ToDo: call service and disable scanning
        }
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        isBluetoothEnabled = bluetoothEnabled;
    }

    public void connectToNewDevice(BleDevice bleDevice, int position) {
        // ToDo: call service to connect to new device
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    public LiveData<List<BleDevice>> getBleDevices() {
        return mBleDevices;
    }

    public LiveData<ToastModel> getToast() {
        return mToastBroadcast;
    }
}
