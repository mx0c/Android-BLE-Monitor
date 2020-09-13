package com.huc.android_ble_monitor.models.AttProtocol;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

class AttWriteReq extends BaseAttPacket {
    public short mHandle;
    public Byte[] mValue;

    public AttWriteReq(Byte[] data, int number) {
        super(data, number);
        mHandle = decodeHandle(data);
        mValue = decodeValue(data);
    }

    private Byte[] decodeValue(Byte[] data){
        Byte[] temp = Arrays.copyOfRange(data, 3, this.packet_length - 4);
        ArrayUtils.reverse(temp);
        return temp;
    }

    private short decodeHandle(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }
}
