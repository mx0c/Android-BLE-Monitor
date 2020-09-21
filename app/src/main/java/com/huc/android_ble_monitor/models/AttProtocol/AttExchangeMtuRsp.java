package com.huc.android_ble_monitor.models.AttProtocol;

public class AttExchangeMtuRsp extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuRsp(Byte[] data) {
        super(data);
        mReceivedAttMTU = decode16BitValue(data[1], data[2]);

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
