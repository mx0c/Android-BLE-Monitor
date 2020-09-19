package com.huc.android_ble_monitor.models.AttProtocol;

public class AttExchangeMtuReq extends BaseAttPacket{
    public short mReceivedAttMTU;

    public AttExchangeMtuReq(Byte[] data) {
        super(data);
        mReceivedAttMTU = decodeMtu(data);
    }

    private short decodeMtu(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Maximum Client MTU: " + this.mReceivedAttMTU + " Byte\n";
        return res;
    }
}
