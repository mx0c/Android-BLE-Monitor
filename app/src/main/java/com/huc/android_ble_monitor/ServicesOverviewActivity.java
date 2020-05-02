package com.huc.android_ble_monitor;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huc.android_ble_monitor.adapters.CharacteristicsAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;

import org.w3c.dom.Text;

public class ServicesOverviewActivity extends AppCompatActivity {
    public static BluetoothGattService staticGattService;
    public static BleDevice staticBleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.services_overview_activity);

        setViewContent();
    }

    void setViewContent(){
        BLEPropertyToViewResolver resolver = new BLEPropertyToViewResolver(this);

        TextView serviceUUID = findViewById(R.id.service_uuid_textview);
        serviceUUID.setText(staticGattService.getUuid().toString());

        TextView serviceName = findViewById(R.id.service_name_textview);
        serviceName.setText(resolver.serviceNameResolver(staticGattService));

        TextView RssiTextView = findViewById(R.id.RSSI_TextView);
        RssiTextView.setText(resolver.deviceRssiResolver(staticBleDevice.mScanResult));

        TextView DeviceNameTextView = findViewById(R.id.DeviceName_TextView);
        DeviceNameTextView.setText(resolver.deviceNameResolver(staticBleDevice.mScanResult));

        TextView DeviceUUIDTextView = findViewById(R.id.DeviceUUID_TextView);
        DeviceUUIDTextView.setText(resolver.deviceAddressResolver(staticBleDevice.mScanResult.getDevice()));

        ImageView BondStateImageView = findViewById(R.id.BondState_ImageView);
        BondStateImageView.setImageResource(resolver.bondStateImageResolver(staticBleDevice.mScanResult));

        TextView BondStateTextView = findViewById(R.id.BondState_TextView);
        BondStateTextView.setText(resolver.bondStateTextResolver(staticBleDevice.mScanResult));

        ListView characteristicListview = findViewById(R.id.characteristic_listview);
        characteristicListview.setAdapter(new CharacteristicsAdapter(this,staticGattService.getCharacteristics()));
    }
}
