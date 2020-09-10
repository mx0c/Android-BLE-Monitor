package com.huc.android_ble_monitor.models.AttProtocol;

import java.util.ArrayList;

class AttExchangeMtuReq extends BaseAttPacket{
    private short mReceivedAttMTU;

    public AttExchangeMtuReq(ArrayList<Byte> data, int number) {
        super(data, number);
        mReceivedAttMTU = extractMtu(data);
    }

    private short extractMtu(ArrayList<Byte> data){
        byte LSB = data.get(1);
        byte MSB = data.get(2);
        return (short)((MSB << 8) + LSB);
    }
}
