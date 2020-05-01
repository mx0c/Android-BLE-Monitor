package com.huc.android_ble_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;

public class ServicesOverviewActivity extends AppCompatActivity {
    public static BluetoothGattService staticGattService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.services_overview_activity);

        TextView serviceUUID = findViewById(R.id.service_uuid_textview);
        serviceUUID.setText(staticGattService.getUuid().toString());

        BLEPropertyToViewResolver resolver = new BLEPropertyToViewResolver(this);
        TextView serviceName = findViewById(R.id.service_name_textview);
        serviceName.setText(resolver.serviceNameResolver(staticGattService));

        ListView characteristicListview = findViewById(R.id.characteristic_listview);
    }
}
