package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.adapters.ServicesListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.DeviceDetailViewModel;

import java.util.ArrayList;

public class DeviceDetailActivity extends BaseActivity {
    private static final String TAG = "BLEM_BleDeviceOverview";

    public static BleDevice staticBleDevice;
    private ListView mListViewOfServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_device_overview);
        setObservers();
    }

    public void setObservers(){
        ((DeviceDetailViewModel)mViewModel).getmBleDevice().observe(this, new Observer<BleDevice>() {
            @Override
            public void onChanged(BleDevice bleDevice) {
                Log.d(TAG, "onChanged: BleDevice value changed");
                if(bleDevice == null) return;
                initializeViews();
            }
        });
        mViewModel.getmBinder().observe(this, new Observer<BluetoothLeService.LocalBinder>() {
            @Override
            public void onChanged(BluetoothLeService.LocalBinder localBinder) {
                if(localBinder == null){
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService = null;
                }else{
                    //bound to service
                    mBluetoothLeService = localBinder.getService();
                    mBluetoothLeService.getBluetoothDevice().observe(DeviceDetailActivity.this, new Observer<BleDevice>() {
                        @Override
                        public void onChanged(BleDevice bleDevice) {
                            ((DeviceDetailViewModel)mViewModel).updateBleDevie(bleDevice);
                        }
                    });
                    mListViewOfServices.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ServicesOverviewActivity.staticGattService = (BluetoothGattService)parent.getAdapter().getItem(position);
                            ServicesOverviewActivity.staticBleDevice = DeviceDetailActivity.staticBleDevice;
                            Intent intent = new Intent(DeviceDetailActivity.this, ServicesOverviewActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    public void initializeViews() {
        PropertyResolver resolver = new PropertyResolver(this);

        TextView tvName = findViewById(R.id.DeviceName_TextView);
        TextView tvAddress = findViewById(R.id.DeviceUUID_TextView);
        TextView tvBonded = findViewById(R.id.BondState_TextView);
        TextView tvRssi = findViewById(R.id.RSSI_TextView);
        TextView tvConnectability = findViewById(R.id.Connectability_TextView);
        TextView tvCompanyIdentifier = findViewById(R.id.CompanyIdentifier_TextView);
        ImageView ivBondstate = findViewById(R.id.BondState_ImageView);
        TextView tvServices = findViewById(R.id.Services_TextView);
        mListViewOfServices = findViewById(R.id.lv_services);

        //setting ListView Adapter
        ServicesListAdapter adapter = new ServicesListAdapter(this, new ArrayList<BluetoothGattService>());
        mListViewOfServices.setAdapter(adapter);

        ScanResult bleScanResult = staticBleDevice.mScanResult;
        tvBonded.setText(resolver.bondStateTextResolver(bleScanResult));
        ivBondstate.setImageResource(resolver.bondStateImageResolver(bleScanResult));
        tvName.setText(resolver.deviceNameResolver(bleScanResult));
        tvAddress.setText(bleScanResult.getDevice().getAddress());
        tvRssi.setText(resolver.deviceRssiResolver(bleScanResult));
        tvCompanyIdentifier.setText(resolver.deviceManufacturerResolver(bleScanResult));
        tvConnectability.setText(resolver.deviceConnectabilityResolver(bleScanResult));

        ArrayList<String> uuids = resolver.deviceServiceResolver(staticBleDevice, bleScanResult);
        tvServices.setText("Services (" + staticBleDevice.getServiceCount() + ")");

        if (staticBleDevice.mBluetoothGatt != null) {
            adapter.clear();
            adapter.addAll(staticBleDevice.mBluetoothGatt.getServices());
            adapter.notifyDataSetChanged();
            if(adapter.getCount() > 0) {
                findViewById(R.id.loading_spinner).setVisibility(View.GONE);
                findViewById(R.id.lv_services).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(DeviceDetailViewModel.class);
        ((DeviceDetailViewModel)mViewModel).init(staticBleDevice);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                BleDevice bleDevice =  ((DeviceDetailViewModel)mViewModel).getmBleDevice().getValue();
                if (bleDevice != null) {
                    if (bleDevice.mConnectionState == BluetoothProfile.STATE_CONNECTED) {
                        new MaterialAlertDialogBuilder(DeviceDetailActivity.this)
                                .setTitle("Disconnect")
                                .setMessage("Disconnect from " + staticBleDevice.mScanResult.getDevice().getName())
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "onClick: yes");
                                        mBluetoothLeService.disconnect();
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d(TAG, "onClick: no");
                                        finish();
                                    }
                                })
                                .show();
                    }
                    else {
                        return false;
                    }
                }
                break;
        }
        return true;
    }
}
