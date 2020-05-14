package com.huc.android_ble_monitor.viewmodels;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;

public class DeviceDetailViewModel extends BaseViewModel {
    public MutableLiveData<BleDevice> mBleDevice = new MutableLiveData<>();

    public void init(final BleDevice bleDevice) {
        if (bleDevice != null) {
            mBleDevice.setValue(bleDevice);
        }
    }

    public LiveData<BleDevice> getmBleDevice() {
        return mBleDevice;
    }

    public void updateBleDevie(BleDevice bleDevice) {
        this.mBleDevice.setValue(bleDevice);
    }

    public void updateRssi(Integer Rssi){
        BleDevice newDevice = mBleDevice.getValue();
        newDevice.mCurrentRssi = Rssi;
        mBleDevice.setValue(newDevice);
    }

    public LiveData<BluetoothLeService.LocalBinder> getmBinder() {
        return mBinder;
    }
}
