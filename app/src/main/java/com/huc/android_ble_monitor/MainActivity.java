package com.huc.android_ble_monitor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Debug;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import com.google.android.material.appbar.MaterialToolbar;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private ListView mListView;
    private MaterialToolbar mToolbar;
    private SwitchCompat mBluetoothSwitch;

    private BluetoothAdapter mBluetoothAdapter;
    private List<ScanResult> mScanResultList = new ArrayList<>();

    private ScanResultArrayAdapter mScanResultAdapter;
    private BluetoothLeScanner mBleScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.switchFromSplashToMainTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mListView = findViewById(R.id.deviceList);
        mScanResultAdapter = new ScanResultArrayAdapter(this, mScanResultList);
        mListView.setAdapter(mScanResultAdapter);
        mListView.setOnItemClickListener(mOnListViewItemClick);

        requestLocationPermission();
        checkBleAvailability();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    private AdapterView.OnItemClickListener mOnListViewItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ScanResult item = mScanResultList.get(position);
            item.getDevice().connectGatt(MainActivity.this, false, new BluetoothGattCallback() {
                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS){
                        //TODO: Retrieve Services and add to list
                    }
                }

                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    switch (newState){
                        case BluetoothProfile.STATE_CONNECTING:
                            Toast.makeText(MainActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothProfile.STATE_CONNECTED:
                            Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                            gatt.discoverServices();
                            break;
                        case BluetoothProfile.STATE_DISCONNECTED:
                            Toast.makeText(MainActivity.this, "Disconnected!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
        }
    };

    private void scanBleDevices(final boolean enable){
        if(enable){
            mBleScanner.startScan(mScanCallback);
        }else{
            mBleScanner.stopScan(mScanCallback);
        }
    }

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

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(!containsDevice(mScanResultList, result)){
                mScanResultList.add(result);
                mScanResultAdapter.notifyDataSetChanged();
            }else{
                mScanResultList = updateDevice(mScanResultList,result);
                mScanResultAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.scanBleDevices(false);
        unregisterReceiver(mReceiver);
    }

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void checkBleAvailability(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean checkForBluetoothEnabled(){
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    private boolean containsDevice(List<ScanResult> resList, ScanResult res) {
        for (ScanResult dev : resList) {
            if (dev.getDevice().getAddress().equals(res.getDevice().getAddress())) {
                return true;
            }
        }
        return false;
    }

    private List<ScanResult> updateDevice(List<ScanResult> resList, ScanResult update){
        int i = 0;
        for (ScanResult res: resList) {
            if(res.getDevice().getAddress().equals(update.getDevice().getAddress())){
                resList.set(i,update);
                return resList;
            }
            i++;
        }
        return resList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch_item);
        View view = menuItem.getActionView();
        mBluetoothSwitch = view.findViewById(R.id.switch_compat_element);
        mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkForBluetoothEnabled()) {
                    if(isChecked){
                        scanBleDevices(true);
                    }else {
                        scanBleDevices(false);
                    }
                }else {
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

    /**
     * Sets back main theme to default AppTheme
     */
    private void switchFromSplashToMainTheme() {
        setTheme(R.style.AppTheme); // Go back from splash screen to main theme on activity create
	}
}
