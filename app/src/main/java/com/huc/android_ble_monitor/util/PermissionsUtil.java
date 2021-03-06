package com.huc.android_ble_monitor.util;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class PermissionsUtil {
    private static final String TAG = "BLEM_PermissionsUTIL";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_READ_STORAGE_PERMISSION = 2;
    public static final int REQUEST_ENABLE_BT_RESULT = 313;

    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public static void requestLocationPermission(Activity activity) {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            Log.d(TAG, "requestLocationPermission: Application is requesting location permissions");
            EasyPermissions.requestPermissions(activity, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }

    @AfterPermissionGranted(REQUEST_READ_STORAGE_PERMISSION)
    public static void requestReadStoragePermission(Activity activity) {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            Log.d(TAG, "requestReadStoragePermission: Application is requestig external storage permissions");
            EasyPermissions.requestPermissions(activity, "Please grant the read external storage permission", REQUEST_READ_STORAGE_PERMISSION, perms);
        }
    }
}
