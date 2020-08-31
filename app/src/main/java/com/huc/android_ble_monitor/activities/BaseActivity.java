package com.huc.android_ble_monitor.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.services.BluetoothLeService;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Base ActivityClass which sets Theme and binds to the BluetoothLeService
 */
public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity {
    protected static final String TAG = "";
    protected T mViewModel;
    protected BluetoothLeService mBluetoothLeService;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            //bound to service
            mBluetoothLeService = ((BluetoothLeService.LocalBinder)service).getService();
            onServiceBinded();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //not bounded to service
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        initializeViewModel();

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        super.onCreate(savedInstanceState);
    }

    protected abstract void onServiceBinded();
    protected abstract void initializeViewModel();
}
