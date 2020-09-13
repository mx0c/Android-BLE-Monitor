package com.huc.android_ble_monitor.models.AttProtocol;

class AttExchangeMtuReq extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuReq(Byte[] data, int number) {
        super(data, number);
        mReceivedAttMTU = decodeMtu(data);
    }

    private short decodeMtu(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }
}
