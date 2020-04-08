package com.huc.android_ble_monitor;

import android.util.Pair;

import com.huc.android_ble_monitor.Models.ParsedAdvertisingPacket;

import java.util.ArrayList;
import java.util.Arrays;

public class Helper {
    public static String BondIntToString(int bondInt) {
        switch (bondInt) {
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

    /**
     * more informations at: <a href="https://www.silabs.com/community/wireless/bluetooth/knowledge-base.entry.html/2017/02/10/bluetooth_advertisin-hGsf></a>
     *
     * @param data
     * @return
     */
    public static ArrayList<ParsedAdvertisingPacket> parseAdvPacket(byte[] data) {
        ArrayList<ParsedAdvertisingPacket> advPacketList = new ArrayList<>();
        int index = 0;
        while (index < data.length) {
            int length = data[index++];
            //Done once we run out of records
            if (length == 0) break;

            byte type = data[index];
            //Done if our record isn't a valid type
            if (type == 0) break;

            byte[] advData = Arrays.copyOfRange(data, index + 1, index + length);

            advPacketList.add(new ParsedAdvertisingPacket(length, type, data));
            index += length;
        }
        return advPacketList;
    }

    public static String StringifyAdvType(byte adType) {
        for (Pair<Integer, String> pair : Static.ADTypes) {
            if (pair.first == adType) {
                return pair.second;
            }
        }
        return "Unknown";
    }
}
