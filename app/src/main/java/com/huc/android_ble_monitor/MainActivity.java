package com.huc.android_ble_monitor;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huc.android_ble_monitor.adapters.ScanResultRecyclerAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.BleUtility;
import com.huc.android_ble_monitor.util.PermissionsUtil;
import com.huc.android_ble_monitor.viewmodels.MainActivityViewModel;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements ScanResultRecyclerAdapter.OnDeviceConnectListener {
    private static final String TAG = "BLEM_MainActivity";

    private SwitchCompat mBluetoothSwitch;
    private MainActivityViewModel mMainActivityViewModel;
    private RecyclerView mScanResultRecyclerView;
    private ScanResultRecyclerAdapter mScanResultRecyclerAdapter;
    private BluetoothLeService mBluetoothLeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        setContentView(R.layout.activity_main);
        mScanResultRecyclerView = findViewById(R.id.scan_result_recycler_view);
        ActivityUtil.setToolbar(this, true);

        PermissionsUtil.requestLocationPermission(this);
        BleUtility.checkIsBluetoothEnabled(this);
        BleUtility.checkBleAvailability(this);

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        mMainActivityViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.init();

        mMainActivityViewModel.getToast().observe(this, new Observer<ToastModel>() {
            @Override
            public void onChanged(ToastModel toastModel) {
                Toast.makeText(MainActivity.this, toastModel.getMessage(), toastModel.getDuration()).show();
            }
        });

        mMainActivityViewModel.getmBleDevices().observe(MainActivity.this, new Observer<List<BleDevice>>() {
            @Override
            public void onChanged(List<BleDevice> bleDevices) {
                mScanResultRecyclerAdapter.notifyDataSetChanged();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView(){
        mScanResultRecyclerAdapter = new ScanResultRecyclerAdapter(this, mMainActivityViewModel.getmBleDevices().getValue(), this);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mScanResultRecyclerView.setLayoutManager(linearLayoutManager);
        mScanResultRecyclerView.setAdapter(mScanResultRecyclerAdapter);
    }

    public final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.d(TAG, "Unable to initialize Bluetooth");
            }else{
                mBluetoothLeService.getScanResult().observe(MainActivity.this, new Observer<ScanResult>() {
                    @Override
                    public void onChanged(ScanResult scanResult) {
                        mMainActivityViewModel.registerScanResult(scanResult);
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBluetoothSwitch.setChecked(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        BleUtility.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        BleUtility.mBleScanner = BleUtility.mBluetoothAdapter.getBluetoothLeScanner();
                        mMainActivityViewModel.setBluetoothEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        // unregisterReceiver(mReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /*
        EventHandlers related to the toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch_item);
        View view = menuItem.getActionView();
        mBluetoothSwitch = view.findViewById(R.id.switch_compat_element);

        mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(mMainActivityViewModel.isBluetoothEnabled()) {
                        Log.d(TAG, "BLE Switch checked. Scanning BLE Devices.");
                        mBluetoothLeService.scanForDevices(true);
                    } else {
                        BleUtility.checkIsBluetoothEnabled(MainActivity.this);
                        buttonView.setChecked(false);
                    }
                } else {
                    Log.d(TAG, "BLE Switch unchecked. Stopped scanning BLE Devices.");
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(this, "Item Selected", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeviceClick(int position) {
        final BleDevice bleDevice = mMainActivityViewModel.getmBleDevices().getValue().get(position);


        BleDeviceOverviewActivity.staticBleDevice = bleDevice;
        Intent intent = new Intent(this, BleDeviceOverviewActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + "Request code " + requestCode +  ", Result Code " + resultCode);
        switch (requestCode) {
            case PermissionsUtil.REQUEST_ENABLE_BT_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    mMainActivityViewModel.setBluetoothEnabled(true);
                } else {
                    mMainActivityViewModel.setBluetoothEnabled(false);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
