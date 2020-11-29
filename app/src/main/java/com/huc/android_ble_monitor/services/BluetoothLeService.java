package com.huc.android_ble_monitor.services;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
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
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.util.BleUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Bluetooth Low Energy service that enables Components of the App to use BLE functionalities
 */
public class BluetoothLeService extends Service {
    public final static String TAG = "BLEM_BluetoothLeService";

    private final IBinder mBinder = new LocalBinder();
    private ScheduledExecutorService mRssiRequestScheduler;
    private BluetoothGatt mBluetoothGatt;
    private MutableLiveData<BluLeDevice> mBleDevice;
    private MutableLiveData<List<BluLeDevice>> mScannedDevices;
    private MutableLiveData<ScanResult> mScanResult;
    private MutableLiveData<Integer> mCurrentRssi = new MutableLiveData<>();
    public IBLeCharacteristicCallbacks mCharacteristicCallbacks;
    public IBLeDescriptorCallbacks mDescriptorCallbacks;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * starts or stops scanning for devices
     * @param enable
     */
    public void scanForDevices(boolean enable){
        Log.i(TAG, "BLE Scan for devices: " + (enable ? "activated" : "deactivated"));
        if(enable) {
            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
            List<ScanFilter> filters = new ArrayList<ScanFilter>();
            BleUtil.mBleScanner.startScan(filters, scanSettings, new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    mScanResult.postValue(result);
                }
            });
        } else {
            BleUtil.mBleScanner.stopScan(new ScanCallback() {});
        }
    }

    /**
     * activates or disables the actualizing of the rssi every 2 seconds
     * @param enable
     */
    public void requestRssi(boolean enable){
        if(enable) {
            Log.i(TAG, "requestRssi: Requesting RSSI Information in a 2 second interval");
            mRssiRequestScheduler = Executors.newSingleThreadScheduledExecutor();
            mRssiRequestScheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mBluetoothGatt.readRemoteRssi();
                }
            }, 0, 2, TimeUnit.SECONDS);
        }else{
            mRssiRequestScheduler.shutdown();
        }
    }

    /**
     * Registers the characteristic callbacks (IBleCharacteristicCallbacks) of an activity
     * @param activity
     */
    public void registerActivityCharacteristicCallbacks(Activity activity){
        mCharacteristicCallbacks = (IBLeCharacteristicCallbacks) activity;
    }

    /**
     * Registers the descriptor callbacks (IBleDescriptorCallbacks) of an activity
     * @param activity
     */
    public void registerActivityDescriptorCallbacks(Activity activity){
        mDescriptorCallbacks = (IBLeDescriptorCallbacks) activity;
    }

    /**
     * Updates the GATT Object of the currently selected device
     * @param gatt
     */
    void updateBleDeviceGatt(BluetoothGatt gatt){
        BluLeDevice bleDevice = mBleDevice.getValue();
        bleDevice.mBluetoothGatt = gatt;
        mBleDevice.postValue(bleDevice);
    }

    /**
     * Updates the Device State of the currently selected device
     * @param state
     */
    void updateBleDeviceState(int state){
        BluLeDevice bleDevice = mBleDevice.getValue();
        bleDevice.mConnectionState = state;
        mBleDevice.postValue(bleDevice);
    }

    /**
     * OnConnectionStateChanged Eventhandler
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onConnectionStateChange: Operation was successful for device " + gatt.getDevice().getAddress());
            } else {
                Log.i(TAG, "onConnectionStateChange: Operation failed " + gatt.getDevice().getAddress());
            }

            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i(TAG, "onConnectionStateChange: Connected to GATT server of device " + gatt.getDevice().getAddress());
                    // Attempts to discover services after successful connection.
                    Log.i(TAG, "onConnectionStateChange: Attempting to start service discovery:" +
                    mBluetoothGatt.discoverServices());
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i(TAG, "onConnectionStateChange: Disconnected from GATT server of device " + gatt.getDevice().getAddress());
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.i(TAG, "onConnectionStateChange: Connecting to GATT server of device " + gatt.getDevice().getAddress());
                    break;
            }
            updateBleDeviceGatt(gatt);
            updateBleDeviceState(newState);
        }

        /**
         * Callback when RSSI has been read
         * @param gatt
         * @param rssi
         * @param status
         */
        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if(status == BluetoothGatt.GATT_SUCCESS){
                Log.v(TAG, "onReadRemoteRssi: received rssi " + rssi + " from device " + gatt.getDevice().getAddress());
                mCurrentRssi.postValue(rssi);
            }else{
                Log.e(TAG, "onReadRemoteRssi: receiving RSSI not successful");
            }
        }

        /**
         * Callback when all Services of a Device are discovered
         * @param gatt
         * @param status
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered: Sucessfully read all services from device " + gatt.getDevice().getAddress());
                updateBleDeviceGatt(gatt);
            } else {
                Log.e(TAG, "onServicesDiscovered failed with statuscode: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if(status == BluetoothGatt.GATT_SUCCESS) {
                mCharacteristicCallbacks.onCharacteristicRead(characteristic);
            } else {
                Log.e(TAG, "onCharacteristicRead failed with statuscode: " + status);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mCharacteristicCallbacks.onCharacteristicWrite(characteristic);
            } else {
                Log.e(TAG, "onCharacteristicWrite for device " + gatt.getDevice().getAddress() + " and characteristic " + characteristic.getUuid() + " failed with statuscode: " + status);
            }
        }

        /**
         * Callback when characteristic notification is received
         * @param gatt
         * @param characteristic
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(mCharacteristicCallbacks != null) {
                Log.i(TAG, "onCharacteristicChanged: Received notificaiton from characteristic " + characteristic.getUuid() + " of device " + gatt.getDevice().getAddress());
                mCharacteristicCallbacks.onCharacteristicNotify(characteristic);
            }
        }

        /**
         * Callback when descriptor read callback is received
         * @param gatt
         * @param descriptor
         * @param status
         */
        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                     int status) {
            if(mDescriptorCallbacks != null && status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onDescriptorRead: Received read callback of descriptor " + descriptor.getUuid() + " of device " + gatt.getDevice().getAddress());
                mDescriptorCallbacks.onDescriptorRead(descriptor);
            }else {
                Log.e(TAG, "onDescriptorRead failed with statuscode: " + status);
            }
        }

        /**
         * Callback when descriptor write callback is received
         * @param gatt
         * @param descriptor
         * @param status
         */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor,
                                     int status) {
            if(mDescriptorCallbacks != null && status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onDescriptorWrite: Received write callback of descriptor " + descriptor.getUuid() + " of device " + gatt.getDevice().getAddress());
                mDescriptorCallbacks.onDescriptorWrite(descriptor);
            }else {
                Log.e(TAG, "onDescriptorWrite failed with statuscode: " + status);
            }
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
            mScannedDevices.setValue(new ArrayList<BluLeDevice>());
        }
        if(mBleDevice == null) mBleDevice = new MutableLiveData<>();
        if(mScanResult == null) mScanResult = new MutableLiveData<>();
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

        Log.i(TAG, "disconnect: Disconnecting from device " + mBluetoothGatt.getDevice().getAddress());
        mBluetoothGatt.disconnect();
        mBluetoothGatt = null;
    }

    /**
     * Connects to GATT Server of a BLE Device
     * @param device
     * @return if successful returns true
     */
    public boolean connect(final BluLeDevice device) {
        mBleDevice.postValue(device);
        if (BleUtil.mBluetoothAdapter == null || device == null) {
            Log.e(TAG, "Connect to GattServer failed. Bluetooth Adapter Sate: "+ BleUtil.mBluetoothAdapter +
                    ", Device State: " + device);
            return false;
        } else {
            //if previously connected
            Log.i(TAG, "connect: Trying to connect to device " + device.mScanResult.getDevice().getAddress());
            if (mBleDevice.getValue() != null && device.mScanResult.getDevice().getAddress().equals(mBleDevice.getValue().mScanResult.getDevice().getAddress())
                    && mBluetoothGatt != null) {
                Log.i(TAG, "Trying to use an existing mBluetoothGatt for connection.");
                if (mBluetoothGatt.connect()) {
                    updateBleDeviceState(BluetoothProfile.STATE_CONNECTED);
                    return true;
                }
            }

            // We want to directly connect to the device, so we are setting the autoConnect
            // parameter to false.
            mBluetoothGatt = device.mScanResult.getDevice().connectGatt(this, false, mGattCallback);
            Log.i(TAG, "Trying to create a new connection to device with address " + device.mScanResult.getDevice().getAddress());
            return true;
        }
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (BleUtil.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "readCharacteristic failed: BluetoothAdapter not initialized");
            return;
        }
        Log.i(TAG, "readCharacteristic: Reading characteristic " + characteristic.getUuid().toString());
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic){
        if (BleUtil.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "writeCharacteristic failed: BluetoothAdapter not initialized");
            return;
        }
        Log.i(TAG, "writeCharacteristic: Writing to characteristic " + characteristic.getUuid().toString() + " value " + characteristic.getValue().toString());
        mBluetoothGatt.writeCharacteristic(characteristic);
    }

    public boolean readDescriptor(BluetoothGattDescriptor descriptor) {
        if (BleUtil.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "readDescriptor failed: BluetoothAdapter not initialized");
            return false;
        } else {
            Log.i(TAG, "readDescriptor: Reading descriptor " + descriptor.getUuid().toString());
            return mBluetoothGatt.readDescriptor(descriptor);
        }
    }

    public boolean writeDescriptor(BluetoothGattDescriptor descriptor) {
        if (BleUtil.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "writeDescriptor failed: BluetoothAdapter not initialized");
            return false;
        } else {
            Log.i(TAG, "writeDescriptor: Writing to descriptor " + descriptor.getUuid().toString() + " value " + descriptor.getValue().toString());
            return mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    /**
     * Enables or disables notification for a characteristic
     * @param characteristic
     * @param enabled
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (BleUtil.mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.e(TAG, "setCharacteristicNotification failed: BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        // https://stackoverflow.com/questions/27068673/subscribe-to-a-ble-gatt-notification-android
        // 0x2902 = org.bluetooth.descriptor.gatt.client_characteristic_configuration
        UUID uuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(uuid);
        descriptor.setValue(enabled ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);
        Log.i(TAG, "setCharacteristicNotification: " + enabled + " for characteristic (UUID): " + characteristic.getUuid().toString());
    }

    public LiveData<ScanResult> getScanResult() {
        return mScanResult;
    }

    public LiveData<Integer> getCurrentRssi(){ return mCurrentRssi; }

    public LiveData<BluLeDevice> getBluetoothDevice() {
        return mBleDevice;
    }
}
