package com.huc.android_ble_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.viewmodels.BaseViewModel;
import com.huc.android_ble_monitor.viewmodels.DeviceDetailViewModel;

/**
 * Base ActivityClass which sets Theme and binds to the BluetoothLeService
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected static final String TAG = "";
    protected BaseViewModel mViewModel;
    protected BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mViewModel.getServiceConnection(), BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        super.onCreate(savedInstanceState);
    }
}
