package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.adapters.CharacteristicListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.ServicesOverviewActivityViewModel;

public class ServicesOverviewActivity extends BaseActivity {
    static final String TAG = "BLEM_ServicesOverview";
    public static BluetoothGattService staticGattService;
    public static BleDevice staticBleDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_overview_activity);
        ActivityUtil.setToolbar(this, false);
        setObservers();
    }

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(ServicesOverviewActivityViewModel.class);
        ((ServicesOverviewActivityViewModel)mViewModel).init(staticGattService);
    }

    private void setObservers(){
        ((ServicesOverviewActivityViewModel)mViewModel).getBinder().observe(ServicesOverviewActivity.this, new Observer<BluetoothLeService.LocalBinder>() {
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
                                    .setTitle("Read from Characteristic returned:")
                                    .setMessage("Read Characteristic "+ characteristic.getUuid().toString() + ":\nRaw: " + characteristic.getValue() + "\nString: " + characteristic.getStringValue(0)
                                    + "\nInt32: "+ characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32,0))
                                    .setNeutralButton("Ok", null)
                                    .show();
                        }
                    });
                    mBluetoothLeService.getWriteCharacteristic().observe(ServicesOverviewActivity.this, new Observer<BluetoothGattCharacteristic>() {
                        @Override
                        public void onChanged(BluetoothGattCharacteristic characteristic) {
                            EditText et = new EditText(ServicesOverviewActivity.this);
                            new MaterialAlertDialogBuilder(ServicesOverviewActivity.this)
                                    .setTitle("Write from Characteristic returned:")
                                    .setMessage("Successfully wrote: " + characteristic.getValue().toString())
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
        TextView serviceName = findViewById(R.id.service_name_textview);
        TextView RssiTextView = findViewById(R.id.RSSI_TextView);
        TextView DeviceNameTextView = findViewById(R.id.DeviceName_TextView);
        TextView DeviceUUIDTextView = findViewById(R.id.DeviceUUID_TextView);
        ImageView BondStateImageView = findViewById(R.id.BondState_ImageView);
        TextView BondStateTextView = findViewById(R.id.BondState_TextView);
        ListView characteristicListview = findViewById(R.id.characteristic_listview);

        serviceUUID.setText(staticGattService.getUuid().toString());
        serviceName.setText(resolver.serviceNameResolver(staticGattService));
        RssiTextView.setText(resolver.deviceRssiResolver(staticBleDevice.mScanResult));
        DeviceNameTextView.setText(resolver.deviceNameResolver(staticBleDevice.mScanResult));
        DeviceUUIDTextView.setText(staticBleDevice.mScanResult.getDevice().getAddress());
        BondStateImageView.setImageResource(resolver.bondStateImageResolver(staticBleDevice.mScanResult));
        BondStateTextView.setText(resolver.bondStateTextResolver(staticBleDevice.mScanResult));
        characteristicListview.setAdapter(new CharacteristicListAdapter(this, staticGattService.getCharacteristics(),this.mBluetoothLeService));
    }
}
