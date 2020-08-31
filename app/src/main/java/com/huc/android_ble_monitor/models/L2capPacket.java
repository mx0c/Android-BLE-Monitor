package com.huc.android_ble_monitor.models;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class L2capPacket {
    public L2capPacket(HciPacket p, int number){
        this.packet_hci_frames.add(p);
        this.packet_length = p.packet_data.size();
        this.packet_data = new ArrayList<>(p.packet_data.subList(4, packet_length));
        this.packet_channel_id = extractChannelId();
        this.packet_number = number;
    }

    private String extractChannelId(){
        String cid = "0x";
        cid += String.format("%02X", this.packet_data.get(2));
        cid += String.format("%02X", this.packet_data.get(3));
        return cid;
    }

    /**
     * add subsequent hci frames to this l2cap packet
     * @param p
     */
    public void addHciFrame(HciPacket p){
        this.packet_hci_frames.add(p);
        this.packet_data.addAll(p.packet_data);
    }

    public int packet_length;
    public String packet_channel_id;
    public int packet_number;
    public ArrayList<HciPacket> packet_hci_frames = new ArrayList<>();
    public ArrayList<Byte> packet_data;
}
