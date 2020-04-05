package com.huc.android_ble_monitor;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Static {
    //from https://github.com/tessel/bleadvertise/blob/master/lib/packet.js
    static List<Pair<Integer, String>> ADTypes = Arrays.asList(
            new Pair<Integer, String>(0x01, "Flags"),
            new Pair<Integer, String>(0x02, "Incomplete List of 16-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x03, "Complete List of 16-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x04, "Incomplete List of 32-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x05, "Complete List of 32-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x06, "Incomplete List of 128-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x07, "Complete List of 128-bit Service Class UUIDs"),
            new Pair<Integer, String>(0x08, "Shortened Local Name"),
            new Pair<Integer, String>(0x09, "Complete Local Name"),
            new Pair<Integer, String>(0x0A, "Tx Power Level"),
            new Pair<Integer, String>(0x0D, "Class of Device"),
            new Pair<Integer, String>(0x0E, "Simple Pairing Hash C"),
            new Pair<Integer, String>(0x0F, "Simple Pairing Randomizer R"),
            new Pair<Integer, String>(0x10, "Device ID"),
            new Pair<Integer, String>(0x11, "Security Manager Out of Band Flags"),
            new Pair<Integer, String>(0x12, "Slave Connection Interval Range"),
            new Pair<Integer, String>(0x14, "List of 16-bit Service Solicitation UUIDs"),
            new Pair<Integer, String>(0x1F, "List of 32-bit Service Solicitation UUIDs"),
            new Pair<Integer, String>(0x15, "List of 128-bit Service Solicitation UUIDs"),
            new Pair<Integer, String>(0x16, "Service Data"),
            new Pair<Integer, String>(0x17, "Public Target Address"),
            new Pair<Integer, String>(0x18, "Random Target Address"),
            new Pair<Integer, String>(0x19, "Appearance"),
            new Pair<Integer, String>(0x1A, "Advertising Interval"),
            new Pair<Integer, String>(0x1B, "LE Bluetooth Device Address"),
            new Pair<Integer, String>(0x1C, "LE Role"),
            new Pair<Integer, String>(0x1D, "Simple Pairing Hash C-256"),
            new Pair<Integer, String>(0x1E, "Simple Pairing Randomizer R-256"),
            new Pair<Integer, String>(0x20, "Service Data - 32-bit UUID"),
            new Pair<Integer, String>(0x21, "Service Data - 128-bit UUID"),
            new Pair<Integer, String>(0x3D, "3D Information Data"),
            new Pair<Integer, String>(0xFF, "Manufacturer Specific Data")
    );
}
