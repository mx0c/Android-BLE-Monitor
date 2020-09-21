package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;

public class AttReadMultipleRsp extends BaseAttPacket {
    public Byte[] mConcatedValues;

    public AttReadMultipleRsp(L2capPacket p) {
        super(p);
        mConcatedValues = decodeValue(packet_data, 1, packet_length-1);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Received Concated Values: \n";
        for(byte b : this.mConcatedValues){
            res += String.format("%02x ", b);
        }
        return res;
    }
}
