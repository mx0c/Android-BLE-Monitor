package com.huc.android_ble_monitor.activities;

import android.bluetooth.BluetoothGattService;

import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.viewmodels.DescriptorDetailViewModel;

public class DescriptorDetailActivity extends BaseActivity<DescriptorDetailViewModel> {
    static final String TAG = "BLEM_DescriptorDetailActivity";
    static BluetoothGattService staticGattService;
    static BluLeDevice staticBleDevice;


    @Override
    protected void onServiceBinded() {

    }

    @Override
    protected void initializeViewModel() {

    }
}
