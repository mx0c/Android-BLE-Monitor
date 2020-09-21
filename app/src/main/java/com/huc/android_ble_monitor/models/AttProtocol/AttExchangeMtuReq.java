package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;

public class AttExchangeMtuReq extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuReq(L2capPacket p) {
        super(p);
        mReceivedAttMTU = decode16BitValue(packet_data[1], packet_data[2]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Maximum Client MTU: " + this.mReceivedAttMTU + " Byte\n";
        return res;
    }
}
