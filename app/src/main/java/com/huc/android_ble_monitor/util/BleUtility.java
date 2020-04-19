package com.huc.android_ble_monitor.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.huc.android_ble_monitor.MainActivity;
import com.huc.android_ble_monitor.models.ToastModel;

import java.util.UUID;


public class BleUtility {
    private static final int REQUEST_ENABLE_BT = 0;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBleScanner;

    private MutableLiveData<ToastModel> toastMessage;

    private static final UUID NAME_CHARACTERISTIC_UUID = UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB");
    private static final UUID DEVICE_INFO_SERVICE_UUID = UUID.fromString("00001800-0000-1000-8000-00805F9B34FB");

    public BleUtility(MainActivity ctx){
    }

    /*
    public void connectToDevice(final BleDevice device, final int position){
        //Check for connectability if api version >= 26
        if (Build.VERSION.SDK_INT >= 26) {
            if(!device.mScanResult.isConnectable()){
                toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT,"Device is not connectable." ));
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

                    BluetoothGattService service = gatt.getService(DEVICE_INFO_SERVICE_UUID);
                    BluetoothGattCharacteristic charac = service.getCharacteristic(NAME_CHARACTERISTIC_UUID);

                    if(gatt.readCharacteristic(charac))
                        toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT,"Reading Characteristic"));
                    else
                        toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT,"Failed Reading Characteristic"));

                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                String byteString = new String(characteristic.getValue());
                toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT, byteString));
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                switch (newState){
                    case BluetoothProfile.STATE_CONNECTING:
                        toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT, "Connecting..."));
                        break;
                    case BluetoothProfile.STATE_CONNECTED:
                        toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT, "Connected!"));
                        gatt.discoverServices();
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT, "Disconnected!"));
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
            toastMessage.setValue(new ToastModel(Toast.LENGTH_SHORT, "Seems like your device doesn\\'t support BLE!"));
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


     */
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


    public LiveData<ToastModel> getToastBroadcast() {
        return toastMessage;
    }
}
