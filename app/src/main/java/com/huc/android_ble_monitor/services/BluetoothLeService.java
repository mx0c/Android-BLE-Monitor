package com.huc.android_ble_monitor.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
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

public class BluetoothLeService extends Service {
    public final static String TAG = "BLEM_BLuetoothLeService";

    public final static String EXTRA_DATA = "CHARACTERISTIC_DATA";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "ACTION_DATA_AVAILABLE";
    public final static String ACTION_CONNECTION_STATE_CHANGED = "ACTION_CONNECTION_STATE_CHANGED";

    private final IBinder mBinder = new LocalBinder();
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BleDevice mBluetoothDevice;
    private MutableLiveData<List<BleDevice>> mScannedDevices;
    private MutableLiveData<ScanResult> mScanResult;
    private MutableLiveData<Integer> mConnectionState;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public LiveData<Integer> getConnectionState(){
        return mConnectionState;
    }

    public LiveData<List<BleDevice>> getScannedDevices() {
        return mScannedDevices;
    }

    public void scanForDevices(boolean enable){
        if(enable) {
            ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();
            List<ScanFilter> filters = new ArrayList<ScanFilter>();
            BleUtility.mBleScanner.startScan(filters, scanSettings, new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    mScanResult.postValue(result);
                }

                //not working, but would be better
                /*@Override
                public void onBatchScanResults(List<ScanResult> results) {
                    for (ScanResult result : results) {
                        if(!BleUtility.containsDevice(mScannedDevices.getValue(), result)){
                            List<BleDevice> devices = mScannedDevices.getValue();
                            devices.add(new BleDevice(result, null));
                            mScannedDevices.postValue(devices);
                        }else{
                            mScannedDevices.setValue(BleUtility.updateDevice(mScannedDevices.getValue(), new BleDevice(result, null)));
                        }
                    }
                }*/
            });
        } else {
            BleUtility.mBleScanner.stopScan(new ScanCallback() {});
        }
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            switch (newState){
                case BluetoothProfile.STATE_CONNECTED:
                    Log.d(TAG, "Connected to GATT server.");
                    // Attempts to discover services after successful connection.
                    Log.d(TAG, "Attempting to start service discovery:" +
                            mBluetoothGatt.discoverServices());
                    mConnectionState.setValue(BluetoothProfile.STATE_CONNECTED);
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.d(TAG, "Disconnected from GATT server.");
                    mConnectionState.setValue(BluetoothProfile.STATE_DISCONNECTED);
                    break;
                case BluetoothProfile.STATE_CONNECTING:
                    Log.d(TAG, "Connecting to GATT server.");
                    mConnectionState.setValue(BluetoothProfile.STATE_CONNECTING);
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.d(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        Log.d(TAG, "broadcastUpdate(action, characteristic): characteristics.getUuid = " + characteristic.getUuid());

        final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            intent.putExtra(EXTRA_DATA, new String(data));
        }
        sendBroadcast(intent);
    }

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

        if(mConnectionState == null){
            mConnectionState = new MutableLiveData<>();
            mConnectionState.setValue(BluetoothProfile.STATE_DISCONNECTED);
        }

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

    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.d(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    public boolean connect(final BleDevice device) {
        if (mBluetoothAdapter == null || device == null) {
            Log.d(TAG, "BluetoothAdapter not initialized or unspecified device.");
            return false;
        }

        // Previously connected device.  Try to reconnect.
        if (mBluetoothDevice != null && device.mScanResult.getDevice().getAddress().equals(mBluetoothDevice.mScanResult.getDevice().getAddress())
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState.setValue(BluetoothProfile.STATE_CONNECTING);
                return true;
            } else {
                return false;
            }
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.mScanResult.getDevice().connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDevice = device;
        mConnectionState.setValue(BluetoothProfile.STATE_CONNECTING);
        return true;
    }

    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.d(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
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
}
