package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanResult;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.util.BleUtility;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivityViewModel extends BaseViewModel {
    private static final String TAG = "BLEM_MAViewModel";
    private MutableLiveData<List<BleDevice>> mBleDevices = new MutableLiveData<>();
    private MutableLiveData<ToastModel> mToastBroadcast = new MutableLiveData<>();

    private boolean isBluetoothEnabled;

    @Override
    public void init() {
        mBleDevices.setValue(new ArrayList<BleDevice>());
        isBluetoothEnabled = true;
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
            List<BleDevice> devices = mBleDevices.getValue();
            List<BleDevice> toRemove = new ArrayList<>();
            for (BleDevice dev : devices) {
                if (dev.mTimestamp <= currTime) {
                    Log.d(TAG, "checkTimeStamp: removed " + dev.mScanResult.getDevice().getAddress() + " because TTL is exceeded.");
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

    public void registerScanResult(ScanResult scanResult) {
        if(!BleUtility.containsDevice(mBleDevices.getValue(), scanResult)){
            List<BleDevice> devices = mBleDevices.getValue();
            devices.add(new BleDevice(scanResult));
            mBleDevices.postValue(devices);
        }else{
            mBleDevices.postValue(BleUtility.updateDevice(mBleDevices.getValue(), new BleDevice(scanResult)));
        }
    }

    public void clearBleDevices() {
        List<BleDevice> devices = mBleDevices.getValue();
        devices.clear();
        this.mBleDevices.postValue(devices);
    }

    public LiveData<ToastModel> getToast() {
        return mToastBroadcast;
    }

    public LiveData<List<BleDevice>> getmBleDevices() {
        return mBleDevices;
    }
}
