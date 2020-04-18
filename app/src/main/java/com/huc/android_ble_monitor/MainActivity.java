package com.huc.android_ble_monitor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import com.google.android.material.appbar.MaterialToolbar;
import com.huc.android_ble_monitor.Models.BleDevice;
import com.huc.android_ble_monitor.Util.ActivityUtil;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BLEM_MainActivity";
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private ListView mListView;
    private MaterialToolbar mToolbar;
    private SwitchCompat mBluetoothSwitch;

    private BleUtility mBleUtility;
    public List<BleDevice> mScanResultList = new ArrayList<>();
    public ScanResultArrayAdapter mScanResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        setContentView(R.layout.activity_main);
        ActivityUtil.setToolbar(this, true);

        initViews();

        mScanResultAdapter = new ScanResultArrayAdapter(this, mScanResultList);
        mListView.setAdapter(mScanResultAdapter);
        mListView.setOnItemClickListener(mOnListViewItemClick);

        requestLocationPermission();

        mBleUtility = new BleUtility(this);
        mBleUtility.checkBleAvailability();
        mBleUtility.checkForBluetoothEnabled();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    /**
     * Helper to initialize views at once place
     */
    public void initViews() {
        mListView = findViewById(R.id.deviceList);
        mBluetoothSwitch = findViewById(R.id.switch_compat_element);
    }

    private AdapterView.OnItemClickListener mOnListViewItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            final BleDevice item = mScanResultList.get(position);
            mBleUtility.connectToDevice(item, position);
            ActivityUtil.startNewActivity(MainActivity.this, BleDeviceOverviewActivity.class, null);
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
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBleUtility.scanBleDevices(false);
        unregisterReceiver(mReceiver);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch_item);
        View view = menuItem.getActionView();
        mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mBleUtility.checkForBluetoothEnabled()) {
                    if (isChecked) {
                        Log.d(TAG, "BLE Switch checked. Scanning BLE Devices.");
                        mBleUtility.scanBleDevices(true);
                    } else {
                        Log.d(TAG, "BLE Switch unchecked. Stopped scanning BLE Devices.");
                        mBleUtility.scanBleDevices(false);
                    }
                } else {
                    buttonView.setChecked(false);
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
}
