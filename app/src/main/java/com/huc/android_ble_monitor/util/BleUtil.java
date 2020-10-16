package com.huc.android_ble_monitor.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.huc.android_ble_monitor.models.BluLeDevice;

import java.util.List;
import java.util.UUID;


public class  BleUtil {
    public static final int REQUEST_ENABLE_BT_RESULT = 313;

    public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothLeScanner mBleScanner;

    static public void checkIsBluetoothEnabled(AppCompatActivity ctx){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBleScanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            ctx.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT_RESULT);
        }
    }

    public static boolean getHciSnoopLogEnabled(Context ctx){
        try {
            return Settings.Secure.getInt(ctx.getContentResolver(), "bluetooth_hci_log") == 1 ? true : false;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void checkBleAvailability(Activity ctx){
        if (!ctx.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(ctx, "Seems like your device doesn\\'t support BLE!", Toast.LENGTH_SHORT ).show();
        }
    }

    public static boolean containsDevice(List<BluLeDevice> resList, ScanResult res) {
        for (BluLeDevice dev : resList) {
            if (dev.mScanResult.getDevice().getAddress().equals(res.getDevice().getAddress())) {
                return true;
            }
        }
        return false;
    }

    public static List<BluLeDevice> updateDevice(List<BluLeDevice> resList, BluLeDevice update){
        int i = 0;
        for (BluLeDevice dev: resList) {
            if(dev.mScanResult.getDevice().getAddress().equals(update.mScanResult.getDevice().getAddress())){
                resList.set(i, update);
                return resList;
            }
            i++;
        }
        return resList;
    }

    /**
     * @return Returns <b>true</b> if property is writable
     */
    public static boolean isCharacteristicWritable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }

    /**
     * @return Returns <b>true</b> if property is Readable
     */
    public static boolean isCharacteristicReadable(BluetoothGattCharacteristic pChar) {
        return ((pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }

    /**
     * @return Returns <b>true</b> if property is supports notification
     */
    public static boolean isCharacteristicNotifiable(BluetoothGattCharacteristic pChar) {
        return (pChar.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }
}
