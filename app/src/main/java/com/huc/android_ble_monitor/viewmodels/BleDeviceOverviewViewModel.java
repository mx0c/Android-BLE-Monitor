package com.huc.android_ble_monitor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.models.BleDevice;

public class BleDeviceOverviewViewModel extends ViewModel {
    private MutableLiveData<BleDevice> mBleDevice = new MutableLiveData<>();

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
}
