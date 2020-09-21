package com.huc.android_ble_monitor.models.AttProtocol;

public class AttExchangeMtuReq extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuReq(Byte[] data) {
        super(data);
        mReceivedAttMTU = decode16BitValue(data[1], data[2]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Maximum Client MTU: " + this.mReceivedAttMTU + " Byte\n";
        return res;
    }
}
