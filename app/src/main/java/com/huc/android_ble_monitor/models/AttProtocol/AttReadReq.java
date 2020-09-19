package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttReadReq extends BaseAttPacket {
    public short mHandle;

    public AttReadReq(Byte[] data) {
        super(data);
        mHandle = decodeHandle(data);
    }

    private short decodeHandle(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Requested Handle: " + BinaryUtil.shortToHexString(mHandle) + "\n";
        return res;
    }
}
