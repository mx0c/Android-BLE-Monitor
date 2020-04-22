package com.huc.android_ble_monitor.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huc.android_ble_monitor.models.BleDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class BleUtility {
    public static final int REQUEST_ENABLE_BT_RESULT = 313;

    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothLeScanner mBleScanner;

    private static final UUID NAME_CHARACTERISTIC_UUID = UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB");
    private static final UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString("00001800-0000-1000-8000-00805F9B34FB");

    static public void checkIsBluetoothEnabled(AppCompatActivity ctx){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ctx.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT_RESULT);
        }
    }

    public static void scanForDevices(boolean enable, @Nullable ScanCallback callback){
        if(enable && callback != null) {
            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
            List<ScanFilter> filters = new ArrayList<ScanFilter>();
            BleUtility.mBleScanner.startScan(filters, scanSettings, callback);
        } else {
            BleUtility.mBleScanner.stopScan(new ScanCallback() {});
        }
    }

    public static void checkBleAvailability(Activity ctx){
        if (!ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(ctx, "Seems like your device doesn\\'t support BLE!", Toast.LENGTH_SHORT ).show();
        }
    }

    public static boolean containsDevice(List<BleDevice> resList, ScanResult res) {
        for (BleDevice dev : resList) {
            if (dev.mScanResult.getDevice().getAddress().equals(res.getDevice().getAddress())) {
                return true;
            }
        }
        return false;
    }

    public static List<BleDevice> updateDevice(List<BleDevice> resList, BleDevice update){
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
