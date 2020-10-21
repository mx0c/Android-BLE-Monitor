package com.huc.android_ble_monitor.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class L2capPacket implements Comparable<L2capPacket>{
    /**
     * Unix timestamp for this packet record
     */
    public Date timestamp;

    /**
     * Length of the complete L2CAP Packet
     */
    public int packet_length;

    /**
     * Channel ID (CID) of this L2CAP Packet
     */
    public String packet_channel_id;

    /**
     * subsequent number of this received L2CAP packet
     */
    public int packet_number;

    /**
     * HCI Frames that are contained in this L2CAP Packet
     */
    public ArrayList<HciPacket> packet_hci_frames = new ArrayList<>();

    /**
     * Data Payload of this L2CAP Packet
     */
    public Byte[] packet_data;

    public L2capPacket(HciPacket p, int number){
        this.packet_hci_frames.add(p);
        this.packet_length = p.packet_data.length;
        this.packet_data = Arrays.copyOfRange(p.packet_data, 4, packet_length);
        this.packet_channel_id = extractChannelId(Arrays.copyOfRange(p.packet_data,0, 4));
        this.packet_number = number;
        this.timestamp = p.timestamp;
    }

    private String extractChannelId(Byte[] data){
        String cid = "0x";
        cid += String.format("%02X", data[3]); //MSB
        cid += String.format("%02X", data[2]); //LSB
        return cid;
    }

    @Override
    public int compareTo(L2capPacket o) {
        return o.timestamp.compareTo(this.timestamp);
    }
}
