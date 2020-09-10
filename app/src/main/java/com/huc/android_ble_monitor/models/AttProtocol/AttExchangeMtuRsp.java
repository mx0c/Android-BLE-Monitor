package com.huc.android_ble_monitor.models.AttProtocol;
import java.util.ArrayList;

class AttExchangeMtuRsp extends BaseAttPacket{
    private short mReceivedAttMTU;

    public AttExchangeMtuRsp(ArrayList<Byte> data, int number) {
        super(data, number);
        mReceivedAttMTU = extractMtu(data);

        // Set ATT MTU to received Value
        BaseAttPacket.MTU_SIZE = mReceivedAttMTU;
    }

    private short extractMtu(ArrayList<Byte> data){
        byte LSB = data.get(1);
        byte MSB = data.get(2);
        return (short)((MSB << 8) + LSB);
    }
}
