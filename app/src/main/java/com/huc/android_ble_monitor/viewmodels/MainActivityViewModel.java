package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

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
        isBluetoothEnabled = true;
        mBleDevices.setValue(new ArrayList<BleDevice>());
    }

    public void enableBluetoothScan(Boolean isScanEnabled) {
        if(isScanEnabled) {
            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
            List<ScanFilter> filters = new ArrayList<ScanFilter>();
            BleUtility.mBleScanner.startScan(filters, scanSettings, new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    if(!BleUtility.containsDevice(mBleDevices.getValue(), result)){
                        List<BleDevice> devices = mBleDevices.getValue();
                        devices.add(new BleDevice(result, null));
                        mBleDevices.postValue(devices);
                    }else{
                        mBleDevices.setValue(BleUtility.updateDevice(mBleDevices.getValue(), new BleDevice(result, null)));
                    }
                }
            });
        } else {
            BleUtility.mBleScanner.stopScan(new ScanCallback() {});
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
