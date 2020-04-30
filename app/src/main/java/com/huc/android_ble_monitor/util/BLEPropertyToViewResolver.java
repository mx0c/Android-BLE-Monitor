package com.huc.android_ble_monitor.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.NameInformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BLEPropertyToViewResolver {
    private static final String TAG = "BLEM_PropertyToViewReso";

    final static int BONDING_IMG_ID = R.drawable.round_bluetooth_searching_white_48;
    final static int BONDED_IMG_ID = R.drawable.round_bluetooth_connected_white_48;
    final static int NOT_BONDED_IMG_ID = R.drawable.round_bluetooth_disabled_white_48;
    final static int NOT_CONNECTED_IMG_ID = R.drawable.round_power_off_white_48;
    final static int CONNECTED_IMG_ID = R.drawable.round_power_white_48;
    final static int CONNECTING_IMG_ID = R.drawable.round_settings_ethernet_white_48dp;
    final static String SIG_UNKNOWN_SERVICE_NAME = "SIG unknown service";
    final static String SIG_UNKNOWN_SERVICE_IDENTIFIER = "SIG unknown service identifier";

    private HashMap<Integer, String> mManufacturerIdToStringMap;
    private HashMap<String, NameInformation> mServiceUUIDtoNameInformationsMap;

    public BLEPropertyToViewResolver(Context ctx) {
        mManufacturerIdToStringMap = DataIO.loadManufacturerIdToStringMap(ctx);
        mServiceUUIDtoNameInformationsMap = DataIO.loadServiceData(ctx);
    }

    public String connectionStateToStringResolver(int connState){
        String res = "";
        switch (connState){
            case BluetoothProfile.STATE_CONNECTED:
                res = "CONNECTED";
                break;
            case BluetoothProfile.STATE_CONNECTING:
                res = "CONNECTING";
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                res = "DISCONNECTED";
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                res = "DISCONNECTING";
                break;
        }
        return res;
    }

    public int connectionStateImageResolver(int connState) {
        int imgId = 0;
        switch (connState) {
            case BluetoothProfile.STATE_CONNECTED:
                imgId = CONNECTED_IMG_ID;
                break;
            case BluetoothProfile.STATE_CONNECTING:
                imgId = CONNECTING_IMG_ID;
                break;
            case BluetoothProfile.STATE_DISCONNECTED:
                imgId = NOT_CONNECTED_IMG_ID;
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                imgId = CONNECTING_IMG_ID;
                break;
        }

        return imgId;
    }

    public int bondStateImageResolver(int state) {
        int id = 0;
        switch(state){
            case BluetoothDevice.BOND_BONDING:
                id = BONDING_IMG_ID;
                break;
            case BluetoothDevice.BOND_BONDED:
                id = BONDED_IMG_ID;
                break;
            default:
            case BluetoothDevice.BOND_NONE:
                id = NOT_BONDED_IMG_ID;
                break;
        }
        return id;
    }

    public int bondStateImageResolver(ScanResult scanResult) {
        int state = scanResult.getDevice().getBondState();
        return bondStateImageResolver(state);
    }

    public String bondStateTextResolver(int state) {
        return BleUtility.BondIntToString(state);
    }

    public String bondStateTextResolver(ScanResult scanResult) {
        int state = scanResult.getDevice().getBondState();
        return this.bondStateTextResolver(state);
    }

    public String deviceNameResolver(String name) {
        return name == null ? "unknown" : name;
    }

    public String deviceNameResolver(ScanResult result) {
        String name = result.getScanRecord().getDeviceName();
        return this.deviceNameResolver(name);
    }

    public String deviceAddressResolver(String deviceAddress) {
        return deviceAddress;
    }

    public String deviceAddressResolver(BluetoothDevice bleDevice) {
        return this.deviceAddressResolver(bleDevice.getAddress());
    }

    public String deviceAddressResolver(ScanResult result) {
        return this.deviceAddressResolver(result.getDevice());
    }

    public String deviceRssiResolver(ScanResult result) {
        return Integer.toString(result.getRssi()) + " dBm";
    }

    public String deviceManufacturerResolver(ScanResult result) {
        SparseArray<byte[]> manufacturerData = result.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        return mManufacturerIdToStringMap.get(manufacturerId) + " (" + manufacturerId + ")";
    }

    public String deviceConnectabilityResolver(ScanResult result) {
        //only possible with api level >= 26
        int buildVersion = Build.VERSION.SDK_INT;
        String connectabilityText = "";
        if (buildVersion >= 26) {
            connectabilityText = "Connectable: " + result.isConnectable();
        } else {
            Log.v(TAG, "deviceConnectabilityResolver: Current api level is " + buildVersion + " Devices below API level 26 cannot detect if connectable");
            connectabilityText = "Connectable: UNKNOWN";
        }

        return connectabilityText;
    }

    public ArrayList<String> deviceServiceResolver(BleDevice item, ScanResult result) {
        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids(); // ToDo Remove Logic to viewmodel
        ArrayList<String> uuidStrings = new ArrayList<>();

        if(uuids != null) {
            for (ParcelUuid uuid : uuids) {
                String uuidString = uuid.getUuid().toString();
                NameInformation nameInfo = this.mServiceUUIDtoNameInformationsMap.get(uuidString);
                String name;

                if(nameInfo != null)
                    name = nameInfo.name;
                else
                    name = "SIG unknown service";

                uuidStrings.add(uuidString + " | " + name);
            }
        }else if(uuidStrings.size() <= 0){
            uuidStrings.add("- No advertised services found");
        }

        return uuidStrings;
    }

    public String serviceNameResolver(BluetoothGattService bluetoothGattService) {
        NameInformation knownSigService = mServiceUUIDtoNameInformationsMap.get(bluetoothGattService.getUuid().toString().substring(4,8));
        String serviceName;


        if(knownSigService == null) {
            serviceName =  SIG_UNKNOWN_SERVICE_NAME;
        } else {
            serviceName = knownSigService.name;
        }

        return serviceName;
    }

    public String serviceUuidResolver(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString();
    }

    public String serviceIdentifierResolver(BluetoothGattService bluetoothGattService) {
        NameInformation knownSigService = mServiceUUIDtoNameInformationsMap.get(bluetoothGattService.getUuid().toString().substring(4,8));
        String serviceIdentifier;

        if(knownSigService == null) {
            serviceIdentifier =  SIG_UNKNOWN_SERVICE_IDENTIFIER;
        } else {
            serviceIdentifier = knownSigService.identifier;
        }

        return serviceIdentifier;
    }
}

