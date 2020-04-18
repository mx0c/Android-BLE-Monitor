package com.huc.android_ble_monitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;
import com.huc.android_ble_monitor.Util.ActivityUtil;

import java.util.Objects;

public class BleDeviceOverviewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        setContentView(R.layout.activity_ble_device_overview);

        ActivityUtil.setToolbar(this, false);
    }
}
