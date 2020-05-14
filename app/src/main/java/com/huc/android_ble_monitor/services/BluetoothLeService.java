package com.huc.android_ble_monitor.services;

import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BleUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BluetoothLeService extends Service {
    public final static String TAG = "BLEM_BLuetoothLeService";

    private final IBinder mBinder = new LocalBinder();
    private BluetoothGatt mBluetoothGatt;
    private MutableLiveData<BleDevice> mBleDevice;
    private MutableLiveData<List<BleDevice>> mScannedDevices;
    private MutableLiveData<ScanResult> mScanResult;
    private MutableLiveData<ScanResult> mFilteredScanResult;
    private MutableLiveData<BluetoothGattCharacteristic> mReadCharacteristic = new MutableLiveData<>();
    private MutableLiveData<BluetoothGattCharacteristic> mWriteCharacteristic = new MutableLiveData<>();
    private MutableLiveData<BluetoothGattCharacteristic> mNotifyCharacteristic = new MutableLiveData<>();
    private ScanCallback mScanCallback;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void scanForDevices(boolean enable){
        if(enable) {
            if(mScanCallback != null) {
                BleUtility.mBleScanner.stopScan(mScanCallback);
                mScanCallback = null;
            }

            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
            List<ScanFilter> filters = new ArrayList<ScanFilter>();
            Log.i(TAG, "scanForDevices: Starting to scan for ble devices");
            BleUtility.mBleScanner.startScan(filters, scanSettings, mScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    if(mScanCallback != null) {
                        mScanResult.setValue(result);
                    }
                }
                @Override
                public void onScanFailed(int errorCode) {
                    Log.e(TAG, "onScanFailed: Scanning of devices failed with error code " +  errorCode);
                }
            });

        } else {
            Log.i(TAG, "scanForDevices: Stopping to scan for ble devices");
            BleUtility.mBleScanner.stopScan(mScanCallback);
            mScanCallback = null;
        }
    }

    public void scanForDevice(final BleDevice bleDevice) {
        if (mScanCallback != null) {
            BleUtility.mBleScanner.stopScan(mScanCallback);
            mScanCallback = null;
        }

        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
        List<ScanFilter> filters = new ArrayList<>();
        filters.add(new ScanFilter.Builder().setDeviceAddress(bleDevice.mScanResult.getDevice().getAddress()).build());
        Log.d(TAG, "scanForDevice: Started scanning for device " + bleDevice.mScanResult.getDevice().getAddress());
        BleUtility.mBleScanner.startScan(filters, scanSettings, mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                mFilteredScanResult.setValue(result);
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e(TAG, "onScanFailed: Scanning of device " + bleDevice.mScanResult.getDevice().getAddress() + " failed with error code " +  errorCode);
            }
        });

    }

    void updateBleDeviceGatt(BluetoothGatt gatt){
        BleDevice bleDevice = mBleDevice.getValue();
        bleDevice.mBluetoothGatt = gatt;
        mBleDevice.postValue(bleDevice);
    }

    void updateBleDeviceState(int state){
        BleDevice bleDevice = mBleDevice.getValue();
        bleDevice.mConnectionState = state;
        mBleDevice.postValue(bleDevice);
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "onConnectionStateChange: Operation was successful");
            } else {
                Log.e(TAG, "onConnectionStateChange: Operation failed");
            }

            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    Log.d(TAG, "onConnectionStateChange: Connected to GATT server.");
                    // Attempts to discover services after successful connection.
                    Log.d(TAG, "onConnectionStateChange: Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.d(TAG, "onConnectionStateChange: Disconnected from GATT server.");
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.d(TAG, "onConnectionStateChange: Connecting to GATT server.");
                    break;
            }
            updateBleDeviceGatt(gatt);
            updateBleDeviceState(newState);
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                updateBleDeviceGatt(gatt);
            } else {
                Log.d(TAG, "onServicesDiscovered failed with statuscode: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                mReadCharacteristic.postValue(characteristic);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                mWriteCharacteristic.postValue(characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    public class LocalBinder extends Binder {
         public BluetoothLeService getService() {
            return BluetoothLeService.this;
         }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if(mScannedDevices == null) {
            mScannedDevices = new MutableLiveData<>();
            mScannedDevices.setValue(new ArrayList<BleDevice>());
        }

        if(mBleDevice == null) {
            mBleDevice = new MutableLiveData<>();
        }

        if(mScanResult == null) mScanResult = new MutableLiveData<>();
        if(mFilteredScanResult == null) mFilteredScanResult = new MutableLiveData<>();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Close GATT Connection
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
        return super.onUnbind(intent);
    }

    public void disconnect() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.disconnect();

        mBluetoothGatt = null;
    }

    public boolean connect(final BleDevice device) {
        mBleDevice.postValue(device);
        if (BleUtility.mBluetoothAdapter == null || device == null) {
            return false;
        } else {
            //if previously connected
            if (mBleDevice.getValue() != null && device.mScanResult.getDevice().getAddress().equals(mBleDevice.getValue().mScanResult.getDevice().getAddress())
                    && mBluetoothGatt != null) {
                Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
                if (mBluetoothGatt.connect()) {
                    updateBleDeviceState(BluetoothProfile.STATE_CONNECTED);
                    return true;
                }
            }

            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            mBluetoothGatt = device.mScanResult.getDevice().connectGatt(this, false, mGattCallback);
            Log.d(TAG, "Trying to create a new connection.");
            return true;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (BleUtility.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
        if (BleUtility.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (BleUtility.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // https://stackoverflow.com/questions/27068673/subscribe-to-a-ble-gatt-notification-android
        // 0x2902 = org.bluetooth.descriptor.gatt.client_characteristic_configuration
        UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(uuid);
        descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This should be
     * invoked only after {@code BluetoothGatt#discoverServices()} completes successfully.
     *
     * @return {@code List} of discovered services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null) return null;
        return mBluetoothGatt.getServices();
    }

    public LiveData<ScanResult> getScanResult() {
        return mScanResult;
    }

    public LiveData<ScanResult> getFilteredScanResult() {
        return mFilteredScanResult;
    }

    public LiveData<BleDevice> getBluetoothDevice() {
        return mBleDevice;
    }

    public LiveData<BluetoothGattCharacteristic> getWriteCharacteristic(){ return mWriteCharacteristic; }

    public LiveData<BluetoothGattCharacteristic> getNotifiedCharacteristic(){ return mNotifyCharacteristic; }

    public LiveData<BluetoothGattCharacteristic> getReadCharacteristic(){ return mReadCharacteristic; }
}
