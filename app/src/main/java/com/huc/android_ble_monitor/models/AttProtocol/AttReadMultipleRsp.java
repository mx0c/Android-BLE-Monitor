package com.huc.android_ble_monitor.models.AttProtocol;

public class AttReadMultipleRsp extends BaseAttPacket {
    public Byte[] mConcatedValues;

    public AttReadMultipleRsp(Byte[] data) {
        super(data);
        mConcatedValues = decodeValue(data, 1, packet_length-1);
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
