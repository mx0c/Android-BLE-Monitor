package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.adapters.CharacteristicListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.ServicesOverviewActivityViewModel;

public class ServicesOverviewActivity extends AppCompatActivity {
    static final String TAG = "BLEM_ServicesOverview";
    public static BluetoothGattService staticGattService;
    public static BleDevice staticBleDevice;
    private ServicesOverviewActivityViewModel mViewModel;
    private BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.services_overview_activity);
        ActivityUtil.setToolbar(this, false);

        mViewModel = ViewModelProviders.of(this).get(ServicesOverviewActivityViewModel.class);
        mViewModel.init(staticGattService);

        //Bind to service
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mViewModel.getServiceConnection(), BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        setObservers();
    }

    private void setObservers(){
        mViewModel.getBinder().observe(ServicesOverviewActivity.this, new Observer<BluetoothLeService.LocalBinder>() {
            @Override
            public void onChanged(BluetoothLeService.LocalBinder localBinder) {
                if(localBinder == null){
                    //unbinded
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService = null;
                }else{
                    //bind to service
                    mBluetoothLeService = localBinder.getService();
                    setViewContent();
                    mBluetoothLeService.getReadCharacteristic().observe(ServicesOverviewActivity.this, new Observer<BluetoothGattCharacteristic>(){
                        @Override
                        public void onChanged(BluetoothGattCharacteristic characteristic) {
                            //gets called when characteristic is read
                            new MaterialAlertDialogBuilder(ServicesOverviewActivity.this)
                                    .setTitle("Disconnect")
                                    .setMessage("Read Characteristic "+ characteristic.getUuid().toString() + " :\nRaw: " + characteristic.getValue() + "\nString: " + characteristic.getStringValue(0)
                                    + "\nInt32: "+ characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32,0))
                                    .setNeutralButton("Ok", null)
                                    .show();
                        }
                    });
                }

            }
        });
    }

    void setViewContent(){
        PropertyResolver resolver = new PropertyResolver(this);

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
        characteristicListview.setAdapter(new CharacteristicListAdapter(this, staticGattService.getCharacteristics(),this.mBluetoothLeService));
    }
}
