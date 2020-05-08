package com.huc.android_ble_monitor.viewmodels;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.services.BluetoothLeService;

public abstract class BaseViewModel extends ViewModel {
    protected MutableLiveData<BluetoothLeService.LocalBinder> mBinder = new MutableLiveData<>();

    protected final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBinder.postValue((BluetoothLeService.LocalBinder) service);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBinder.postValue(null);
        }
    };

    public LiveData<BluetoothLeService.LocalBinder> getmBinder() {
        return mBinder;
    }

    public ServiceConnection getServiceConnection() {
        return mServiceConnection;
    }
}
