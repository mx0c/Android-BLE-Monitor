package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.BluetoothGattService;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.models.BleDevice;

public class ServicesOverviewActivityViewModel extends ViewModel {
    private MutableLiveData<BluetoothGattService> mService;
    public MutableLiveData<BleDevice> mDevice;

    public void init(BluetoothGattService service, BleDevice device){
        mService = new MutableLiveData<>(service);
        mDevice = new MutableLiveData<>(device);
    }

    public void updateRssi(Integer Rssi){
        BleDevice newDevice = mDevice.getValue();
        newDevice.mCurrentRssi = Rssi;
        mDevice.setValue(newDevice);
    }

    public LiveData<BluetoothGattService> getService(){ return mService; }
    public LiveData<BleDevice> getDevice(){ return mDevice; }
}
