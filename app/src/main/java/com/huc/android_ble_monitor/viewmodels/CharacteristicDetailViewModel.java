package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BluLeDevice;

public class CharacteristicDetailViewModel extends ViewModel {
    private MutableLiveData<BluetoothGattService> mService;
    public MutableLiveData<BluLeDevice> mDevice;
    private MutableLiveData<BluetoothGattCharacteristic> mCharacteristic;

    public void init(BluetoothGattService service, BluLeDevice device, BluetoothGattCharacteristic bluetoothGattCharacteristic){
        mService = new MutableLiveData<>(service);
        mDevice = new MutableLiveData<>(device);
        mCharacteristic = new MutableLiveData<>(bluetoothGattCharacteristic);
    }

    public void updateRssi(Integer Rssi){
        BluLeDevice newDevice = mDevice.getValue();
        newDevice.mCurrentRssi = Rssi;
        mDevice.setValue(newDevice);
    }

    public LiveData<BluetoothGattService> getService(){ return mService; }
    public LiveData<BluLeDevice> getDevice(){ return mDevice; }
    public LiveData<BluetoothGattCharacteristic> getCharacteristic() { return mCharacteristic; }
}
