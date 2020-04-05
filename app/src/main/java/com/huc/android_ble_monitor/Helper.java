package com.huc.android_ble_monitor;

public class Helper {
    public static String BondIntToString(int bondInt){
        switch(bondInt){
            case 10:
                return "NONE";
            case 11:
                return "BONDING";
            case 12:
                return "BONDED";
            default:
                return "NOT RECOGNIZED";
        }
    }
}
