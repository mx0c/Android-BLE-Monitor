package com.huc.android_ble_monitor.activities;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.characteristicDetailActivity.DescriptorListAdapter;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.services.IBLeDescriptorCallbacks;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.BinaryUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.CharacteristicDetailViewModel;

public class CharacteristicDetailActivity extends BaseActivity<CharacteristicDetailViewModel> implements IBLeDescriptorCallbacks {
    static final String TAG = "BLEM_CharacteristicDetailActivity";
    static BluetoothGattService staticGattService;
    static BluLeDevice staticBleDevice;
    static BluetoothGattCharacteristic staticCharacteristic;

    private PropertyResolver mResolver;
    TextView characteristicName;
    TextView characteristicUUID;
    TextView characteristicIdentifier;
    ListView descriptorListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characteristic_detail_view);
        ActivityUtil.setToolbar(this, false);
        mResolver = new PropertyResolver();
        setObservers();
    }

    @Override
    protected void onServiceBinded() {
        mViewModel.getService().observe(CharacteristicDetailActivity.this, new Observer<BluetoothGattService>() {
            @Override
            public void onChanged(BluetoothGattService service) {
                //update service related views when service changed
                characteristicName = findViewById(R.id.characteristic_name_textview);
                characteristicUUID = findViewById(R.id.characteristic_uuid_textview);
                characteristicIdentifier = findViewById(R.id.characteristic_identifier_textview);
                descriptorListView = findViewById(R.id.descriptor_listview);

                characteristicName.setText(mResolver.characteristicNameResolver(staticCharacteristic));
                characteristicUUID.setText(staticCharacteristic.getUuid().toString());
                characteristicIdentifier.setText(mResolver.characteristicIdentifierResolver(staticCharacteristic));
                descriptorListView.setAdapter(new DescriptorListAdapter(CharacteristicDetailActivity.this, staticCharacteristic.getDescriptors(), mBluetoothLeService));
            }
        });
        mBluetoothLeService.getCurrentRssi().observe(CharacteristicDetailActivity.this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer rssi) {
                mViewModel.updateRssi(rssi);
            }
        });

    }

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(CharacteristicDetailViewModel.class);
        mViewModel.init(staticGattService, staticBleDevice, staticCharacteristic);
    }

    private void setObservers(){
        mViewModel.mDevice.observe(CharacteristicDetailActivity.this, new Observer<BluLeDevice>() {
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

    @Override
    public void onDescriptorRead(final BluetoothGattDescriptor descriptor) {
        CharacteristicDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(CharacteristicDetailActivity.this);
                builder.setTitle("Read from descriptor returned:")
                .setMessage("Read descriptor: " + descriptor.getUuid().toString() + "\nRaw (0x): " + BinaryUtil.byteArrToHexString(descriptor.getValue()) + "\nString: " + new String(descriptor.getValue()))
                .setNeutralButton("Ok", null)
                .show();
                builder = null;
                System.gc();
            }
        });
    }

    @Override
    public void onDescriptorWrite(final BluetoothGattDescriptor descriptor) {
        CharacteristicDetailActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialAlertDialogBuilder(CharacteristicDetailActivity.this)
                        .setTitle("Write to descriptor returned:")
                        .setMessage("Write descriptor " + descriptor.getUuid().toString() + "\nRaw (0x): " + BinaryUtil.byteArrToHexString(descriptor.getValue()) + "\nString: " + new String(descriptor.getValue()))
                        .setNeutralButton("Ok", (DialogInterface.OnClickListener) null)
                        .show();
            }
        });
    }
}
