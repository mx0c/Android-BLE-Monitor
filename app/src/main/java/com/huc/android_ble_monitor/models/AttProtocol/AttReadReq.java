package com.huc.android_ble_monitor.models.AttProtocol;

class AttReadReq extends BaseAttPacket{
    public short mHandle;

    public AttReadReq(Byte[] data, int number) {
        super(data, number);
        mHandle = decodeHandle(data);
    }

    private short decodeHandle(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }
}
