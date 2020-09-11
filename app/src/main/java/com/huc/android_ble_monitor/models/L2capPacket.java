package com.huc.android_ble_monitor.models;

import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.*;

public class L2capPacket {
    public L2capPacket(HciPacket p, int number){
        this.packet_hci_frames.add(p);
        this.packet_length = p.packet_data.length;
        this.packet_data = Arrays.copyOfRange(p.packet_data, 4, packet_length);
        this.packet_channel_id = extractChannelId(Arrays.copyOfRange(p.packet_data,0, 4));
        this.packet_number = number;
    }

    private String extractChannelId(Byte[] data){
        String cid = "0x";
        cid += String.format("%02X", data[3]); //MSB
        cid += String.format("%02X", data[2]); //LSB
        return cid;
    }

    /**
     * add subsequent hci frames to this l2cap packet
     * @param p
     */
    public void addHciFrame(HciPacket p){
        this.packet_hci_frames.add(p);
        this.packet_data = ArrayUtils.addAll(this.packet_data, p.packet_data);
    }

    public int packet_length;
    public String packet_channel_id;
    public int packet_number;
    public ArrayList<HciPacket> packet_hci_frames = new ArrayList<>();
    public Byte[] packet_data;
}
