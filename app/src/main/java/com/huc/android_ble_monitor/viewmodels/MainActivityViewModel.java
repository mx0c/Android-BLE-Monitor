package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanResult;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.util.BleUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivityViewModel extends ViewModel {
    private static final String TAG = "BLEM_MAViewModel";
    private MutableLiveData<List<BluLeDevice>> mBleDevices = new MutableLiveData<>();
    private MutableLiveData<ToastModel> mToastBroadcast = new MutableLiveData<>();
    private boolean isScanEnabled;
    private boolean isBluetoothEnabled;

    public void init() {
        mBleDevices.setValue(new ArrayList<BluLeDevice>());
        isBluetoothEnabled = true;
        isScanEnabled = false;
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                checkTimeStamp();
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void checkTimeStamp(){
        try {
            long currTime = new Date().getTime();
            List<BluLeDevice> devices = mBleDevices.getValue();
            List<BluLeDevice> toRemove = new ArrayList<>();
            for (BluLeDevice dev : devices) {
                if (dev.mTimestamp <= currTime) {
                    Log.d(TAG, "checkTimeStamp: removed " + dev.mScanResult.getDevice().getAddress() + " because TTL was exceeded.");
                    toRemove.add(dev);
                }
            }
            devices.removeAll(toRemove);
            mBleDevices.postValue(devices);
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "checkTimeStamp: " + e.getMessage());
        }
    }

    public void setBluetoothEnabled(boolean bluetoothEnabled) {
        isBluetoothEnabled = bluetoothEnabled;
    }

    public boolean isBluetoothEnabled() {
        return isBluetoothEnabled;
    }

    public boolean isScanEnabled() {
        return isScanEnabled;
    }

    public void setScanEnabled(boolean scanEnabled) {
        isScanEnabled = scanEnabled;
    }

    public void registerScanResult(ScanResult scanResult) {
        if(!BleUtil.containsDevice(mBleDevices.getValue(), scanResult)){
            List<BluLeDevice> devices = mBleDevices.getValue();
            devices.add(new BluLeDevice(scanResult));
            mBleDevices.postValue(devices);
        }else{
            mBleDevices.postValue(BleUtil.updateDevice(mBleDevices.getValue(), new BluLeDevice(scanResult)));
        }
    }

    public void refreshBleDevices() {
        List<BluLeDevice> devices = mBleDevices.getValue();
        devices.clear();
        this.mBleDevices.postValue(devices);
    }

    public LiveData<ToastModel> getToast() {
        return mToastBroadcast;
    }

    public LiveData<List<BluLeDevice>> getmBleDevices() {
        return mBleDevices;
    }
}
