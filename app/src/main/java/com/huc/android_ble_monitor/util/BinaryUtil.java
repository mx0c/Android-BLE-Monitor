package com.huc.android_ble_monitor.util;

public class BinaryUtil {
    public static String shortToHexString(Short s){
        byte msb = (byte) ((s & 0xff00) >> 8);
        byte lsb = (byte) (s & 0x00ff);

        String res = "0x" + String.format("%02X", msb);
        res += String.format("%02X", lsb);
        return res;
    }

    public static String byteArrToHexString(byte[] data){
        String hexString = "";
        for(byte b : data){
            hexString += String.format("%02X ", b);
        }
        return hexString;
    }
}
