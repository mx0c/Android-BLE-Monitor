package com.huc.android_ble_monitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 0;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 30000;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;
    private Handler mHandler = new Handler();
    private List<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.deviceList);

        DeviceArrayAdapter devAdapter = new DeviceArrayAdapter(this, mDeviceList);
        mListView.setAdapter(devAdapter);

        checkBleAvailability();
        if(checkForBluetoothEnabled()){
            scanBleDevices(true);
        }
    }

    private void checkBleAvailability(){
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean checkForBluetoothEnabled(){
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            // if bluetooth enabled
            scanBleDevices(true);
        }
    }

    private void scanBleDevices(final boolean enable){
        if(enable){
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(leScanCallback);
        }else{
            mScanning = false;
            mBluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    private BluetoothAdapter.LeScanCallback leScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDeviceList.add(device);
                        }
                    });
                }
            };
}
