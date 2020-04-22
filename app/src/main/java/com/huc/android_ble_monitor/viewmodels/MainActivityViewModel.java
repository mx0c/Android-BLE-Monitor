package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanResult;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.util.BleUtility;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<BleDevice>> mBleDevices = new MutableLiveData<>();
    private MutableLiveData<ToastModel> mToastBroadcast = new MutableLiveData<>();

    private boolean isBluetoothEnabled;

    public void init() {
        mBleDevices.setValue(new ArrayList<BleDevice>());
        isBluetoothEnabled = true;
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        isBluetoothEnabled = bluetoothEnabled;
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    public void registerScanResult(ScanResult scanResult) {
        if(!BleUtility.containsDevice(mBleDevices.getValue(), scanResult)){
            List<BleDevice> devices = mBleDevices.getValue();
            devices.add(new BleDevice(scanResult, null));
            mBleDevices.postValue(devices);
        }else{
            mBleDevices.postValue(BleUtility.updateDevice(mBleDevices.getValue(), new BleDevice(scanResult, null)));
        }
    }

    public LiveData<ToastModel> getToast() {
        return mToastBroadcast;
    }

    public LiveData<List<BleDevice>> getmBleDevices() {
        return mBleDevices;
    }
}
