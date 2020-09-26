package com.huc.android_ble_monitor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.models.BluLeDevice;

public class DeviceDetailViewModel extends ViewModel{
    public MutableLiveData<BluLeDevice> mBleDevice = new MutableLiveData<>();

    public void init(final BluLeDevice bleDevice) {
        if (bleDevice != null) {
            mBleDevice.setValue(bleDevice);
        }
    }

    public LiveData<BluLeDevice> getmBleDevice() {
        return mBleDevice;
    }

    public void updateBleDevie(BluLeDevice bleDevice) {
        this.mBleDevice.setValue(bleDevice);
    }
}
