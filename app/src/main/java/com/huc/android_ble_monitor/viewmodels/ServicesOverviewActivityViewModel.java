package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.services.BluetoothLeService;

import java.util.List;

public class ServicesOverviewActivityViewModel extends BaseViewModel {
    private MutableLiveData<BluetoothGattService> mService;

    public void init(BluetoothGattService service){
        mService = new MutableLiveData<>(service);
        mBinder = new MutableLiveData<>();
    }

    public LiveData<BluetoothLeService.LocalBinder> getBinder() {
        return mBinder;
    }
    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }
    public LiveData<BluetoothGattService> getService(){ return mService; }
}
