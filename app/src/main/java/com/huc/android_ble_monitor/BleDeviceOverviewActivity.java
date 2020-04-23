package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.huc.android_ble_monitor.adapters.ServicesListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;
import com.huc.android_ble_monitor.viewmodels.BleDeviceOverviewViewModel;

import java.util.ArrayList;

public class BleDeviceOverviewActivity extends AppCompatActivity {
    private static final String TAG = "BLEM_BleDeviceOverview";

    public static BleDevice staticBleDevice;
    private BleDeviceOverviewViewModel mBleDeviceOverviewViewModel;
    private BLEPropertyToViewResolver blePropertyToViewResolver;
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

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        mServicesListAdapter = new ServicesListAdapter(this, new ArrayList<BluetoothGattService>());
        lvServices.setAdapter(mServicesListAdapter);

        blePropertyToViewResolver = new BLEPropertyToViewResolver(this);

        mBleDeviceOverviewViewModel = ViewModelProviders.of(this).get(BleDeviceOverviewViewModel.class);
        mBleDeviceOverviewViewModel.init(staticBleDevice);

        mBleDeviceOverviewViewModel.getmBleDevice().observe(this, new Observer<BleDevice>() {
            @Override
            public void onChanged(BleDevice bleDevice) {
                Log.d(TAG, "onChanged: BleDevice value changed");
                mapBleObjectToView(bleDevice);
            }
        });

        lvServices.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: Clicked on List Item: " + ((BluetoothGattService)parent.getItemAtPosition(position)).getUuid());
            }
        });
    }

    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.d(TAG, "Unable to initialize Bluetooth");
            }

            mBluetoothLeService.getBluetoothDevice().observe(BleDeviceOverviewActivity.this, new Observer<BleDevice>() {
                @Override
                public void onChanged(BleDevice bleDevice) {
                    mBleDeviceOverviewViewModel.updateBleDevie(bleDevice);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
        }
    };

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

    public void mapBleObjectToView(BleDevice bleDevice) {
        if (bleDevice.getmBluetoothGatt() == null) {
            ScanResult bleScanResult = bleDevice.mScanResult;
            tvBonded.setText(blePropertyToViewResolver.bondStateTextResolver(bleScanResult));
            ivBondstate.setImageResource(blePropertyToViewResolver.bondStateImageResolver(bleScanResult));
            tvName.setText(blePropertyToViewResolver.deviceNameResolver(bleScanResult));
            tvAddress.setText(blePropertyToViewResolver.deviceAddressResolver(bleScanResult));
            tvRssi.setText(blePropertyToViewResolver.deviceRssiResolver(bleScanResult));
            tvCompanyIdentifier.setText(blePropertyToViewResolver.deviceManufacturerResolver(bleScanResult));
            tvConnectability.setText(blePropertyToViewResolver.deviceConnectabilityResolver(bleScanResult));

            ArrayList<String> uuids = blePropertyToViewResolver.deviceServiceResolver(bleDevice, bleScanResult);
            tvServices.setText("Services (" + bleDevice.getServiceCount() + ")");
        } else {
            BluetoothDevice mBluetoothDevice = bleDevice.getmBluetoothGatt().getDevice();
            Log.d(TAG, "mapBleObjectToView: " + mBluetoothDevice.getBondState());

            tvBonded.setText(blePropertyToViewResolver.bondStateTextResolver(mBluetoothDevice.getBondState()));
            ivBondstate.setImageResource(blePropertyToViewResolver.bondStateImageResolver(mBluetoothDevice.getBondState()));
            tvName.setText(blePropertyToViewResolver.deviceNameResolver(mBluetoothDevice.getName()));
            tvAddress.setText(blePropertyToViewResolver.deviceAddressResolver(mBluetoothDevice.getAddress()));

            // Update services in the view
            mServicesListAdapter.clear();
            mServicesListAdapter.addAll(bleDevice.mServices);
            mServicesListAdapter.notifyDataSetChanged();
        }
    }
}
