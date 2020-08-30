package com.huc.android_ble_monitor.models;

import java.util.ArrayList;

public class AttPacket {
    public int packet_opcode;
    public int packet_length;
    public ArrayList<Byte> packet_data;
}
