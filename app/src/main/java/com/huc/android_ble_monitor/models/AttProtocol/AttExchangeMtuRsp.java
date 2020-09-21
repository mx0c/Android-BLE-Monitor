package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;

public class AttExchangeMtuRsp extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuRsp(L2capPacket p) {
        super(p);
        mReceivedAttMTU = decode16BitValue(packet_data[1], packet_data[2]);

        // Set ATT MTU to received Value
        BaseAttPacket.MTU_SIZE = mReceivedAttMTU;
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "MTU Response from Server: " + this.mReceivedAttMTU + " Byte\n";
        return res;
    }
}
