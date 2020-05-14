package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;

import java.util.List;

public class ServicesOverviewActivityViewModel extends BaseViewModel {
    private MutableLiveData<BluetoothGattService> mService;
    public MutableLiveData<BleDevice> mDevice;

    public void init(BluetoothGattService service, BleDevice device){
        mService = new MutableLiveData<>(service);
        mDevice = new MutableLiveData<>(device);
        mBinder = new MutableLiveData<>();
    }

    public void updateRssi(Integer Rssi){
        BleDevice newDevice = mDevice.getValue();
        newDevice.mCurrentRssi = Rssi;
        mDevice.setValue(newDevice);
    }

    public LiveData<BluetoothLeService.LocalBinder> getBinder() {
        return mBinder;
    }
    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }
    public LiveData<BluetoothGattService> getService(){ return mService; }
    public LiveData<BleDevice> getDevice(){ return mDevice; }
}
