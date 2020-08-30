package com.huc.android_ble_monitor.models;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class L2capPacket {
    public L2capPacket(ArrayList<Byte> hciPayload, int number){
        this.packet_length = hciPayload.size();
        this.packet_channel_id = extractChannelId(hciPayload);
        this.packet_data = (ArrayList)hciPayload.subList(4, packet_length);
        this.packet_number = number;
    }

    private int extractChannelId(ArrayList<Byte> data){
        byte[] bytes = { data.get(2), data.get(3) };
        return ByteBuffer.wrap(bytes).getInt();
    }

    public int packet_length;
    public int packet_channel_id;
    public int packet_number;
    public ArrayList<Byte> packet_data;
}
