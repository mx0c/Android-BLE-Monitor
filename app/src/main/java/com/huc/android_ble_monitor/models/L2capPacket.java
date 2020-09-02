package com.huc.android_ble_monitor.models;

import java.util.ArrayList;
import java.util.List;

public class L2capPacket {
    public L2capPacket(HciPacket p, int number){
        this.packet_hci_frames.add(p);
        this.packet_length = p.packet_data.size();
        this.packet_data = new ArrayList<>(p.packet_data.subList(4, packet_length));
        this.packet_channel_id = extractChannelId(p.packet_data.subList(0, 4));
        this.packet_number = number;
    }

    private String extractChannelId(List<Byte> data){
        String cid = "0x";
        cid += String.format("%02X", data.get(3)); //MSB
        cid += String.format("%02X", data.get(2)); //LSB
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
