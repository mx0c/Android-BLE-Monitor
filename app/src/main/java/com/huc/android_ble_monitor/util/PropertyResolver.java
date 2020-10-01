package com.huc.android_ble_monitor.util;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.ScanResult;
import android.os.Build;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.SparseArray;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.models.NameInformation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PropertyResolver {
    private static final String TAG = "BLEM_PropertyToViewReso";
    private final static int BONDING_IMG_ID = R.drawable.round_bluetooth_searching_white_48;
    private final static int BONDED_IMG_ID = R.drawable.round_bluetooth_connected_white_48;
    private final static int NOT_BONDED_IMG_ID = R.drawable.round_bluetooth_disabled_white_48;
    private final static int NOT_CONNECTED_IMG_ID = R.drawable.round_power_off_white_48;
    private final static int CONNECTED_IMG_ID = R.drawable.round_power_white_48;
    private final static int CONNECTING_IMG_ID = R.drawable.round_settings_ethernet_white_48dp;
    public final static String SIG_UNKNOWN_SERVICE_NAME = "Unknown Service";
    public final static String SIG_UNKNOWN_CHARACTERISTIC_NAME = "Unknown Characteristic";
    public final static String SIG_UNKNOWN_CHARACTERISTIC_IDENTIFIER = "Unknown Characteristic Identifier";
    public final static String SIG_UNKNOWN_SERVICE_IDENTIFIER = "Unknown Service Identifier";
    private final static String LEGACY_SCAN_RES = "Legacy Scan Result";
    private final static String NOT_AVAIL = "n/a";
    private final static String WHITESPACE = " ";
    private final static String COLON = ":";
    private final static String ADV_INTERVAL = "Advertising Interval";
    private final static String MILLISECONDS_SHORT = "ms";
    private final static String TIMESTAMP = "Timestamp";
    private final static String CONNECTABLE = "Connectable";

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

    public static String BondIntToString(int bondInt) {
        switch (bondInt) {
            case 10:
                return "NOT BONDED";
            case 11:
                return "BONDING";
            case 12:
                return "BONDED";
            default:
                return "NOT RECOGNIZED";
        }
    }

    public String bondStateTextResolver(ScanResult scanResult) {
        int state = scanResult.getDevice().getBondState();
        return BondIntToString(state);
    }

    public String deviceNameResolver(String name) {
        return name == null ? "unknown" : name;
    }

    public String deviceNameResolver(ScanResult result) {
        String name = result.getScanRecord().getDeviceName();
        return this.deviceNameResolver(name);
    }

    public String deviceRssiResolver(ScanResult result) {
        return result.getRssi() + " dBm";
    }

    public String deviceRssiResolver(Integer rssi) {
        return Integer.toString(rssi) + " dBm";
    }

    public String deviceManufacturerResolver(ScanResult result) {
        SparseArray<byte[]> manufacturerData = result.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        return DataUtil.resolveManufacturerId(manufacturerId) + " (" + manufacturerId + ")";
    }

    public String deviceConnectabilityResolver(ScanResult result) {
        //only possible with api level >= 26
        int buildVersion = Build.VERSION.SDK_INT;
        String connectabilityText = "";
        if (buildVersion >= 26) {
            connectabilityText = CONNECTABLE + COLON + WHITESPACE + result.isConnectable();
        } else {
            connectabilityText = CONNECTABLE + COLON + WHITESPACE + NOT_AVAIL;
        }

        return connectabilityText;
    }

    public ArrayList<String> deviceServiceResolver(BluLeDevice item, ScanResult result) {
        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids(); // ToDo Remove Logic to viewmodel
        ArrayList<String> uuidStrings = new ArrayList<>();

        if(uuids != null) {
            for (ParcelUuid uuid : uuids) {
                String uuidString = uuid.getUuid().toString();
                String name = " (" + DataUtil.resolveUuidToNameInformation(uuidString) + ")";
                uuidStrings.add(name + uuidString);
            }
        }
        return uuidStrings;
    }

    public String serviceNameResolver(BluetoothGattService bluetoothGattService) {
        String servicename = DataUtil.resolveUuidToNameInformation(bluetoothGattService.getUuid().toString().substring(4,8).toUpperCase()).name;
        if(servicename == DataUtil.UNKNOWN_UUID) {
            return SIG_UNKNOWN_SERVICE_NAME;
        } else {
            return servicename;
        }
    }

    public String characteristicNameResolver(BluetoothGattCharacteristic bluetoothGattCharacteristic){
        UUID uuid = bluetoothGattCharacteristic.getUuid();
        String sUuid = uuid.toString();
        sUuid = sUuid.substring(4,8).toUpperCase();
        NameInformation characteristicNi = DataUtil.resolveUuidToNameInformation(sUuid);
        return characteristicNi.name == DataUtil.UNKNOWN_UUID ? SIG_UNKNOWN_CHARACTERISTIC_NAME : characteristicNi.name;
    }

    public String characteristicIdentifierResolver(BluetoothGattCharacteristic bluetoothGattCharacteristic){
        UUID uuid = bluetoothGattCharacteristic.getUuid();
        String sUuid = uuid.toString();
        sUuid = sUuid.substring(4,8).toUpperCase();
        NameInformation characteristicNi = DataUtil.resolveUuidToNameInformation(sUuid);
        return characteristicNi.identifier == DataUtil.UNKNOWN_UUID ? SIG_UNKNOWN_CHARACTERISTIC_IDENTIFIER : characteristicNi.identifier;
    }

    public String serviceUuidResolver(BluetoothGattService bluetoothGattService) {
        return bluetoothGattService.getUuid().toString();
    }

    public String serviceIdentifierResolver(BluetoothGattService bluetoothGattService) {
        String sUuid = bluetoothGattService.getUuid().toString();
        sUuid = sUuid.substring(4,8).toUpperCase();
        NameInformation knownSigService = DataUtil.resolveUuidToNameInformation(sUuid);

        if(knownSigService.identifier == DataUtil.UNKNOWN_UUID) {
            return SIG_UNKNOWN_SERVICE_IDENTIFIER;
        } else {
            return  knownSigService.identifier;
        }
    }

    public String legacyScanResultResolver(ScanResult scanResult) {
        String strLegacy;
        int buildVersion = Build.VERSION.SDK_INT;
        if (buildVersion >= 26) {
            strLegacy = LEGACY_SCAN_RES + COLON + WHITESPACE + scanResult.isLegacy();
        } else {
            strLegacy = LEGACY_SCAN_RES + COLON + WHITESPACE + NOT_AVAIL;
        }

        return strLegacy;
    }

    public String advertisingIntervalResolver(ScanResult scanResult) {
        double unit = 1.25;
        String interval;
        int buildVersion = Build.VERSION.SDK_INT;
        if (buildVersion >= 26) {
            interval = ADV_INTERVAL +
                    COLON +
                    WHITESPACE +
                    unit * scanResult.getPeriodicAdvertisingInterval() +
                    MILLISECONDS_SHORT;
        } else {
            interval = ADV_INTERVAL + COLON + WHITESPACE + NOT_AVAIL;
        }
        return interval;
    }

    public String timestampResolver(ScanResult scanResult) {
        long timestampNanos = scanResult.getTimestampNanos();
        long rxTimestampMillis = System.currentTimeMillis() -
                SystemClock.elapsedRealtime() +
                timestampNanos / 1000000;
        Date rxDate = new Date(rxTimestampMillis);
        String sDate = new SimpleDateFormat("HH:mm:ss.SSS").format(rxDate);
        return TIMESTAMP + COLON + WHITESPACE + sDate;
    }
}

