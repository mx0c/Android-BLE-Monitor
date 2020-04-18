package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import com.huc.android_ble_monitor.Models.BleDevice;
import java.util.List;
import java.util.UUID;


public class BleUtility {
    private static final int REQUEST_ENABLE_BT = 0;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBleScanner;
    private MainActivity mCtx;

    private static final UUID NAME_CHARACTERISTIC_UUID = UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB");
    private static final UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString("0000180a-0000-1000-8000-00805F9B34FB");

    BleUtility(MainActivity ctx){
        this.mCtx = ctx;
    }

    public void connectToDevice(final BleDevice device, final int position){
        //Check for connectability if api version >= 26
        if (Build.VERSION.SDK_INT >= 26) {
            if(!device.mScanResult.isConnectable()){
                Toast.makeText(mCtx, "Device is not connectable.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        device.mScanResult.getDevice().connectGatt(mCtx, false, new BluetoothGattCallback() {
            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (status == BluetoothGatt.GATT_SUCCESS){
                    //Retrieve Services and add to list (needs to be tested)
                    List<BluetoothGattService> services = gatt.getServices();
                    device.mServices.addAll(services);
                    mCtx.mScanResultList.set(position, device);
                    mCtx.mScanResultAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                characteristic.getValue();
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                Toast.makeText(mCtx, "onConnectionStateChange", Toast.LENGTH_SHORT).show();
                switch (newState){
                    case BluetoothProfile.STATE_CONNECTING:
                        Toast.makeText(mCtx, "Connecting...", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothProfile.STATE_CONNECTED:
                        Toast.makeText(mCtx, "Connected!", Toast.LENGTH_SHORT).show();
                        gatt.discoverServices();
                        BluetoothGattCharacteristic charac = gatt.getService(DEVICE_INFO_SERVICE_UUID).getCharacteristic(NAME_CHARACTERISTIC_UUID);

                        if(gatt.readCharacteristic(charac))
                            Toast.makeText(mCtx, "Reading Characteristic", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mCtx, "Failed Reading Characteristic", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        Toast.makeText(mCtx, "Disconnected!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            if(!containsDevice(mCtx.mScanResultList, result)){
                mCtx.mScanResultList.add(new BleDevice(result, null));
                mCtx.mScanResultAdapter.notifyDataSetChanged();
            }else{
                mCtx.mScanResultList = updateDevice(mCtx.mScanResultList, new BleDevice(result, null));
                mCtx.mScanResultAdapter.notifyDataSetChanged();
            }
        }
    };

    public void scanBleDevices(final boolean enable){
        if(enable){
            mBleScanner.startScan(mScanCallback);
        }else{
            mBleScanner.stopScan(mScanCallback);
        }
    }

    public void checkBleAvailability(){
        if (!mCtx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(mCtx, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkForBluetoothEnabled(){
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (this.mBluetoothAdapter == null || !this.mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mCtx.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }
        return true;
    }

    private boolean containsDevice(List<BleDevice> resList, ScanResult res) {
        for (BleDevice dev : resList) {
            if (dev.mScanResult.getDevice().getAddress().equals(res.getDevice().getAddress())) {
                return true;
            }
        }
        return false;
    }

    private List<BleDevice> updateDevice(List<BleDevice> resList, BleDevice update){
        int i = 0;
        for (BleDevice dev: resList) {
            if(dev.mScanResult.getDevice().getAddress().equals(update.mScanResult.getDevice().getAddress())){
                resList.set(i, update);
                return resList;
            }
            i++;
        }
        return resList;
    }

    public static String BondIntToString(int bondInt) {
        switch (bondInt) {
            case 10:
                return "NOT CONNECTED";
            case 11:
                return "BONDING";
            case 12:
                return "BONDED";
            default:
                return "NOT RECOGNIZED";
        }
    }
}
