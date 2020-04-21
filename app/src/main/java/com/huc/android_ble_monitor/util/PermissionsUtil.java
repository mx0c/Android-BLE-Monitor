package com.huc.android_ble_monitor.util;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Toast;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionsUtil {
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final int REQUEST_ENABLE_BT_RESULT = 313;

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public static void requestLocationPermission(Activity activity) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(activity, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }
}
