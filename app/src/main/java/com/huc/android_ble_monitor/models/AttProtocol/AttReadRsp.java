package com.huc.android_ble_monitor.models.AttProtocol;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class AttReadRsp extends BaseAttPacket {
    public Byte[] mValue;

    public AttReadRsp(Byte[] data) {
        super(data);
        mValue = decodeValue(data, 1, this.packet_length - 2);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Server Responded with: \n";
        for(byte b: this.mValue)
            res += String.format("%02x ", b);
        return res;
    }
}
