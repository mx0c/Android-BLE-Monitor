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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.adapters.ServicesListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.viewmodels.BleDeviceOverviewViewModel;

import java.util.ArrayList;

public class BleDeviceOverviewActivity extends AppCompatActivity {
    private static final String TAG = "BLEM_BleDeviceOverview";

    public static BleDevice staticBleDevice;
    private BleDeviceOverviewViewModel mBleDeviceOverviewViewModel;
    private PropertyResolver propertyResolver;
    private ServicesListAdapter mServicesListAdapter;

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvBonded;
    private TextView tvRssi;
    private TextView tvConnectability;
    private TextView tvCompanyIdentifier;
    private TextView tvServices;
    private ImageView ivBondstate;
    public ListView lvServices;
    private BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        setContentView(R.layout.activity_ble_device_overview);

        ActivityUtil.setToolbar(this, false);
        initializeViews();

        mServicesListAdapter = new ServicesListAdapter(this, new ArrayList<BluetoothGattService>());
        lvServices.setAdapter(mServicesListAdapter);

        propertyResolver = new PropertyResolver(this);

        mBleDeviceOverviewViewModel = ViewModelProviders.of(this).get(BleDeviceOverviewViewModel.class);
        mBleDeviceOverviewViewModel.init(staticBleDevice);

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mBleDeviceOverviewViewModel.getmServiceConnection(), BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        setObservers();
    }

    public void setObservers(){
        mBleDeviceOverviewViewModel.getmBleDevice().observe(this, new Observer<BleDevice>() {
            @Override
            public void onChanged(BleDevice bleDevice) {
                Log.d(TAG, "onChanged: BleDevice value changed");
                if(bleDevice == null) return;
                mapBleObjectToActivity(bleDevice);
            }
        });
        mBleDeviceOverviewViewModel.getmBinder().observe(this, new Observer<BluetoothLeService.LocalBinder>() {
            @Override
            public void onChanged(BluetoothLeService.LocalBinder localBinder) {
                if(localBinder == null){
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService = null;
                }else{
                    //bound to service
                    mBluetoothLeService = localBinder.getService();
                    mBluetoothLeService.getBluetoothDevice().observe(BleDeviceOverviewActivity.this, new Observer<BleDevice>() {
                        @Override
                        public void onChanged(BleDevice bleDevice) {
                            mBleDeviceOverviewViewModel.updateBleDevie(bleDevice);
                        }
                    });
                    lvServices.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ServicesOverviewActivity.staticGattService = (BluetoothGattService)parent.getAdapter().getItem(position);
                            ServicesOverviewActivity.staticBleDevice = BleDeviceOverviewActivity.staticBleDevice;
                            Intent intent = new Intent(BleDeviceOverviewActivity.this, ServicesOverviewActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    public void initializeViews() {
        tvName = findViewById(R.id.DeviceName_TextView);
        tvAddress = findViewById(R.id.DeviceUUID_TextView);
        tvBonded = findViewById(R.id.BondState_TextView);
        tvRssi = findViewById(R.id.RSSI_TextView);
        tvConnectability = findViewById(R.id.Connectability_TextView);
        tvCompanyIdentifier = findViewById(R.id.CompanyIdentifier_TextView);
        ivBondstate = findViewById(R.id.BondState_ImageView);
        tvServices = findViewById(R.id.Services_TextView);
        lvServices = findViewById(R.id.lv_services);
    }

    public void mapBleObjectToActivity(BleDevice bleDevice) {
        ScanResult bleScanResult = bleDevice.mScanResult;
        tvBonded.setText(propertyResolver.bondStateTextResolver(bleScanResult));
        ivBondstate.setImageResource(propertyResolver.bondStateImageResolver(bleScanResult));
        tvName.setText(propertyResolver.deviceNameResolver(bleScanResult));
        tvAddress.setText(propertyResolver.deviceAddressResolver(bleScanResult));
        tvRssi.setText(propertyResolver.deviceRssiResolver(bleScanResult));
        tvCompanyIdentifier.setText(propertyResolver.deviceManufacturerResolver(bleScanResult));
        tvConnectability.setText(propertyResolver.deviceConnectabilityResolver(bleScanResult));

        ArrayList<String> uuids = propertyResolver.deviceServiceResolver(bleDevice, bleScanResult);
        tvServices.setText("Services (" + bleDevice.getServiceCount() + ")");

        if (bleDevice.mBluetoothGatt != null) {
            mServicesListAdapter.clear();
            mServicesListAdapter.addAll(bleDevice.mBluetoothGatt.getServices());
            mServicesListAdapter.notifyDataSetChanged();
            if(mServicesListAdapter.getCount() > 0) {
                findViewById(R.id.loading_spinner).setVisibility(View.GONE);
                findViewById(R.id.lv_services).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                BleDevice bleDevice =  mBleDeviceOverviewViewModel.getmBleDevice().getValue();
                if (bleDevice != null) {
                    if (bleDevice.mConnectionState == BluetoothProfile.STATE_CONNECTED) {
                        new MaterialAlertDialogBuilder(BleDeviceOverviewActivity.this)
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
