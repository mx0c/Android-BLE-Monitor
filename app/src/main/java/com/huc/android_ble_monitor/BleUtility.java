package com.huc.android_ble_monitor;

import android.util.Pair;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class BleUtility {
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
}
