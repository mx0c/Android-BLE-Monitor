package com.huc.android_ble_monitor.activities;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.CharacteristicListAdapter;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.services.IBLeServiceCallbacks;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.ServicesOverviewActivityViewModel;


public class ServicesOverviewActivity extends BaseActivity<ServicesOverviewActivityViewModel> implements IBLeServiceCallbacks {
    static final String TAG = "BLEM_ServicesOverview";
    public static BluetoothGattService staticGattService;
    public static BluLeDevice staticBleDevice;
    private PropertyResolver mResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_overview_activity);
        ActivityUtil.setToolbar(this, false);
        mResolver = new PropertyResolver();
        setObservers();
    }

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(ServicesOverviewActivityViewModel.class);
        mViewModel.init(staticGattService, staticBleDevice);
    }

    @Override
    public void onCharacteristicRead(final BluetoothGattCharacteristic characteristic) {
        ServicesOverviewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialAlertDialogBuilder(ServicesOverviewActivity.this)
                .setTitle("Read from Characteristic returned:")
                .setMessage("Read Characteristic "+ characteristic.getUuid().toString() + ":\nRaw: " + characteristic.getValue() + "\nString: " + characteristic.getStringValue(0)
                        + "\nInt32: "+ characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32,0))
                .setNeutralButton("Ok", null)
                .show();
            }
        });
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGattCharacteristic characteristic) {
        ServicesOverviewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText et = new EditText(ServicesOverviewActivity.this);
                new MaterialAlertDialogBuilder(ServicesOverviewActivity.this)
                        .setTitle("Write from Characteristic returned:")
                        .setMessage("Successfully wrote: " + characteristic.getValue().toString())
                        .setNeutralButton("Ok", null)
                        .show();
            }
        });
    }

    @Override
    public void onCharacteristicNotify(final BluetoothGattCharacteristic characteristic) {
        ServicesOverviewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialAlertDialogBuilder(ServicesOverviewActivity.this)
                        .setTitle("Received Notification from Characteristic")
                        .setMessage(characteristic.getValue().toString())
                        .setNeutralButton("Ok", null)
                        .setNegativeButton("Disable Notification", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //disable notification
                                mBluetoothLeService.setCharacteristicNotification(characteristic, false);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onServiceBinded() {
        mBluetoothLeService.registerActivityCallbacks(ServicesOverviewActivity.this);
        mViewModel.getService().observe(ServicesOverviewActivity.this, new Observer<BluetoothGattService>() {
            @Override
            public void onChanged(BluetoothGattService service) {
                //update service related views when service changed
                TextView serviceUUID = findViewById(R.id.service_uuid_textview);
                TextView serviceName = findViewById(R.id.service_name_textview);
                ListView characteristicListview = findViewById(R.id.characteristic_listview);

                serviceUUID.setText(service.getUuid().toString());
                serviceName.setText(mResolver.serviceNameResolver(service));
                characteristicListview.setAdapter(new CharacteristicListAdapter(ServicesOverviewActivity.this, service.getCharacteristics(), mBluetoothLeService));
            }
        });
        mBluetoothLeService.getCurrentRssi().observe(ServicesOverviewActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer rssi) {
                mViewModel.updateRssi(rssi);
            }
        });
    }

    private void setObservers(){
        mViewModel.mDevice.observe(ServicesOverviewActivity.this, new Observer<BluLeDevice>() {
            @Override
            public void onChanged(BluLeDevice device) {
                //update Device related views when device changed
                TextView RssiTextView = findViewById(R.id.RSSI_TextView);
                TextView DeviceNameTextView = findViewById(R.id.DeviceName_TextView);
                TextView DeviceUUIDTextView = findViewById(R.id.DeviceUUID_TextView);
                ImageView BondStateImageView = findViewById(R.id.BondState_ImageView);
                TextView BondStateTextView = findViewById(R.id.BondState_TextView);
                RssiTextView.setText(mResolver.deviceRssiResolver(device.mCurrentRssi));
                DeviceNameTextView.setText(mResolver.deviceNameResolver(device.mScanResult));
                DeviceUUIDTextView.setText(device.mScanResult.getDevice().getAddress());
                BondStateImageView.setImageResource(mResolver.bondStateImageResolver(device.mScanResult));
                BondStateTextView.setText(mResolver.bondStateTextResolver(device.mScanResult));
            }
        });
    }
}
