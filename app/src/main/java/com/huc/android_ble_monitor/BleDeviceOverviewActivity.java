package com.huc.android_ble_monitor;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.huc.android_ble_monitor.util.ActivityUtil;

public class BleDeviceOverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        setContentView(R.layout.activity_ble_device_overview);

        ActivityUtil.setToolbar(this, false);
    }
}
