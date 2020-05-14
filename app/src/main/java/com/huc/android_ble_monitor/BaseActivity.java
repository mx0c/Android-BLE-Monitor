package com.huc.android_ble_monitor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.viewmodels.BaseViewModel;

/**
 * Base ActivityClass which sets Theme and binds to the BluetoothLeService
 */
public abstract class BaseActivity<T extends BaseViewModel> extends AppCompatActivity {
    protected static final String TAG = "";
    protected T mViewModel;
    protected BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        initializeViewModel();

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mViewModel.getServiceConnection(), BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        super.onCreate(savedInstanceState);
    }

    protected abstract void initializeViewModel();
}
