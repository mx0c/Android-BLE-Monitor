package com.huc.android_ble_monitor.models.AttProtocol;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

class AttWriteReq extends BaseAttPacket {
    private short mHandle;
    private Byte[] mValue;

    public AttWriteReq(Byte[] data, int number) {
        super(data, number);
        mHandle = extractHandle(data);
        mValue = extractValue(data);
    }

    private Byte[] extractValue(Byte[] data){
        Byte[] temp = Arrays.copyOfRange(data, 3, this.packet_length - 3);
        ArrayUtils.reverse(temp);
        return temp;
    }

    private short extractHandle(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }
}
