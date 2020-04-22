package com.huc.android_ble_monitor.viewmodels;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BleUtility;
import java.util.Arrays;
import java.util.List;

public class BleDeviceOverviewViewModel extends ViewModel {
    private MutableLiveData<BleDevice> mBleDevice = new MutableLiveData<>();

    public void init(final BleDevice bleDevice) {
        if (bleDevice != null) {
            mBleDevice.setValue(bleDevice);

            ScanResult toBeScannedBleDevice = this.mBleDevice.getValue().mScanResult;

            List<ScanFilter> filter = Arrays.asList(new ScanFilter.Builder()
                    .setDeviceAddress(toBeScannedBleDevice.getDevice().getAddress()).build());
            final ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();

            BleUtility.mBleScanner.startScan(filter, scanSettings, new ScanCallback() {
                /**
                 * Callback when a BLE advertisement has been found.
                 *
                 * @param callbackType Determines how this callback was triggered. Could be one of {@link
                 *                     ScanSettings#CALLBACK_TYPE_ALL_MATCHES}, {@link ScanSettings#CALLBACK_TYPE_FIRST_MATCH} or
                 *                     {@link ScanSettings#CALLBACK_TYPE_MATCH_LOST}
                 * @param result       A Bluetooth LE scan result.
                 */
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    mBleDevice.postValue(new BleDevice(result, null));
                }
            });

        }
    }

    public LiveData<BleDevice> getmBleDevice() {
        return mBleDevice;
    }
}
