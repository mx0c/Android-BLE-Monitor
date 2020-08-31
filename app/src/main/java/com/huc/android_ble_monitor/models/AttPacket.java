package com.huc.android_ble_monitor.models;

import java.util.ArrayList;

public class AttPacket {
    public boolean packet_authentication_signature_flag;
    public boolean packet_command_flag;
    public int packet_length;
    public ArrayList<Byte> packet_data;
}
