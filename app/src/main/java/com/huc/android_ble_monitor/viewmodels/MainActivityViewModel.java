package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.MainActivity;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.BleUtility;

import java.util.ArrayList;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<BleDevice>> mBleDevices = new MutableLiveData<>();
    private MutableLiveData<ToastModel> mToastBroadcast = new MutableLiveData<>();
    private MutableLiveData<BluetoothLeService.LocalBinder> mBinder = new MutableLiveData<>();

    private boolean isBluetoothEnabled;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBinder.postValue((BluetoothLeService.LocalBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };

    public void init() {
        mBleDevices.setValue(new ArrayList<BleDevice>());
        isBluetoothEnabled = true;
    }

    public ServiceConnection getmServiceConnection() {
        return mServiceConnection;
    }

    public MutableLiveData<BluetoothLeService.LocalBinder> getmBinder() {
        return mBinder;
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

    public LiveData<ToastModel> getToast() {
        return mToastBroadcast;
    }

    public LiveData<List<BleDevice>> getmBleDevices() {
        return mBleDevices;
    }
}
