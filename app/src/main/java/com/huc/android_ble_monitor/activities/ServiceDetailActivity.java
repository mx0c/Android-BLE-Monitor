package com.huc.android_ble_monitor.activities;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.serviceDetailActivity.CharacteristicListAdapter;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.services.IBLeCharacteristicCallbacks;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.BinaryUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.ServicesOverviewActivityViewModel;


public class ServiceDetailActivity extends BaseActivity<ServicesOverviewActivityViewModel> implements IBLeCharacteristicCallbacks {
    static final String TAG = "BLEM_ServicesOverview";
    public static BluetoothGattService staticGattService;
    public static BluLeDevice staticBleDevice;
    private PropertyResolver mResolver;

    TextView serviceUUID;
    TextView serviceName;
    TextView serviceIdentifier;
    ListView characteristicListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_detail);
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
        ServiceDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialAlertDialogBuilder(ServiceDetailActivity.this)
                .setTitle("Read from Characteristic returned:")
                .setMessage("Read Characteristic: "+ characteristic.getUuid().toString() + "\nRaw (0x): " + BinaryUtil.byteArrToHexString(characteristic.getValue()) + "\nString: " + characteristic.getStringValue(0)
                        + "\nInt32: "+ characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT32,0))
                .setNeutralButton("Ok", null)
                .show();
            }
        });
    }

    @Override
    public void onCharacteristicWrite(final BluetoothGattCharacteristic characteristic) {
        ServiceDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText et = new EditText(ServiceDetailActivity.this);
                new MaterialAlertDialogBuilder(ServiceDetailActivity.this)
                        .setTitle("Write from Characteristic returned:")
                        .setMessage("Successfully wrote: " + BinaryUtil.byteArrToHexString(characteristic.getValue()))
                        .setNeutralButton("Ok", null)
                        .show();
            }
        });
    }

    @Override
    public void onCharacteristicNotify(final BluetoothGattCharacteristic characteristic) {
        ServiceDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialAlertDialogBuilder(ServiceDetailActivity.this)
                        .setTitle("Received Notification from Characteristic")
                        .setMessage(BinaryUtil.byteArrToHexString(characteristic.getValue()))
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
        mBluetoothLeService.registerActivityCharacteristicCallbacks(ServiceDetailActivity.this);

        mViewModel.getService().observe(ServiceDetailActivity.this, new Observer<BluetoothGattService>() {
            @Override
            public void onChanged(BluetoothGattService service) {
                //update service related views when service changed
                serviceUUID = findViewById(R.id.service_uuid_textview);
                serviceName = findViewById(R.id.service_name_textview);
                serviceIdentifier = findViewById(R.id.service_identifier_textview);
                characteristicListview = findViewById(R.id.characteristic_listview);

                serviceUUID.setText(service.getUuid().toString());
                serviceName.setText(mResolver.serviceNameResolver(service));
                serviceIdentifier.setText(mResolver.serviceIdentifierResolver(service));
                characteristicListview.setAdapter(new CharacteristicListAdapter(ServiceDetailActivity.this, service.getCharacteristics(), mBluetoothLeService));
            }
        });
        mBluetoothLeService.getCurrentRssi().observe(ServiceDetailActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer rssi) {
                mViewModel.updateRssi(rssi);
            }
        });

        characteristicListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) parent.getAdapter().getItem(position);

                if (characteristic.getDescriptors().size() > 0) {
                    CharacteristicDetailActivity.staticGattService = staticGattService;
                    CharacteristicDetailActivity.staticBleDevice = staticBleDevice;
                    CharacteristicDetailActivity.staticCharacteristic = characteristic;
                    Intent intent = new Intent(ServiceDetailActivity.this, CharacteristicDetailActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

    private void setObservers(){
        mViewModel.mDevice.observe(ServiceDetailActivity.this, new Observer<BluLeDevice>() {
            @Override
            public void onChanged(BluLeDevice device) {
                //update Device related views when device changed
                if(device != null) {
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
            }
        });
    }
}
