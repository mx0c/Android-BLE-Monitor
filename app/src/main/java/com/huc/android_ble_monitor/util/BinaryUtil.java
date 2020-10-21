package com.huc.android_ble_monitor.util;

public class BinaryUtil {
    /**
     * Util function to convert Short values to a Hex string.
     * @param Short to Convert
     * @return Converted String
     */
    public static String shortToHexString(Short s){
        byte msb = (byte) ((s & 0xff00) >> 8);
        byte lsb = (byte) (s & 0x00ff);

        String res = "0x" + String.format("%02X", msb);
        res += String.format("%02X", lsb);
        return res;
    }

    /**
     * Util function to convert an even hex encoded String to a ByteArray.
     * @param String to Convert
     * @return converted byte[]
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /**
     * Util function to convert an ByteArray to an hex encoded String.
     * @param byte[] to convert
     * @return converted String
     */
    public static String byteArrToHexString(byte[] data){
        String hexString = "";
        for(byte b : data){
            hexString += String.format("%02X ", b);
        }
        return hexString;
    }
}
