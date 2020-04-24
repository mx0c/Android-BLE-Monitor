package com.huc.android_ble_monitor.viewmodels;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.BleDeviceOverviewActivity;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;

public class BleDeviceOverviewViewModel extends ViewModel {
    private MutableLiveData<BleDevice> mBleDevice = new MutableLiveData<>();
    private MutableLiveData<BluetoothLeService.LocalBinder> mBinder = new MutableLiveData<>();

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

    public MutableLiveData<BluetoothLeService.LocalBinder> getmBinder() {
        return mBinder;
    }

    public ServiceConnection getmServiceConnection() {
        return mServiceConnection;
    }

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
