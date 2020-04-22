package com.huc.android_ble_monitor.util;

import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BleDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BLEPropertyToViewResolver {
    private static final String TAG = "BLEM_PropertyToViewReso";

    final static int BOND_STATE_BONDING = R.drawable.round_bluetooth_searching_white_48;
    final static int BOND_STATE_BONDED = R.drawable.round_bluetooth_connected_white_48;
    final static int BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED = R.drawable.round_bluetooth_disabled_white_48;

    static HashMap<Integer, String> mManufacturerIdToStringMap;

    public BLEPropertyToViewResolver(Context ctx) {
        mManufacturerIdToStringMap = DataIO.loadManufacturerIdToStringMap(ctx);
    }


    public int bondStateImageResolver(ScanResult scanResult) {
        int state = scanResult.getDevice().getBondState();
        int id = 0;
        if (state == 11) {
            id = BOND_STATE_BONDING;
        } else if (state == 12) {
            id = BOND_STATE_BONDED;
        } else {
            id = BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED;
        }

        return id;
    }

    public String bondStateTextResolver(ScanResult scanResult) {
        int state =  scanResult.getDevice().getBondState();

        return BleUtility.BondIntToString(state);
    }

    public String deviceNameResolver(ScanResult result) {
        String name = result.getScanRecord().getDeviceName();
        return name == null ? "unknown" : name;
    }

    public String deviceAddressResolver(ScanResult result) {
        return result.getDevice().getAddress();
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
        return mManufacturerIdToStringMap.get(manufacturerId) + "(" + manufacturerId + ")";
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

        //add BleDevice services to listview
        if(item.mServices.size() > 0 && item != null){
            for (BluetoothGattService service: item.mServices) {
                uuidStrings.add(service.getUuid().toString());
            }
        }

        if(uuids != null) {
            for (ParcelUuid uuid : uuids) {
                uuidStrings.add(uuid.getUuid().toString());
            }
        }else if(uuidStrings.size() <= 0){
            uuidStrings.add("- No advertised services found");
        }

        return uuidStrings;
    }

    public String serviceNameResolver(BluetoothGattService bluetoothGattService) {
        return "Name"; // ToDo: replace with real method
    }

    public String serviceUuidResolver(BluetoothGattService bluetoothGattService) {
        return "UUID"; // ToDo: replace with real method
    }

    public String serviceIdentifierResolver(BluetoothGattService bluetoothGattService) {
        return "Identifier"; // ToDo: replace with real method
    }
}

