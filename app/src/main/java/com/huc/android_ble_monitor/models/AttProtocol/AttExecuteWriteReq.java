package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;

public class AttExecuteWriteReq extends BaseAttPacket {
    private String mFlagInfo;
    private byte mFlag;

    public AttExecuteWriteReq(L2capPacket p) {
        super(p);
        mFlag = packet_data[1];
        if(mFlag == 0x00){
            mFlagInfo = "Cancel all prepared writes";
        }else if(mFlag == 0x01){
            mFlagInfo = "Immediately write all pending prepared values";
        }else{
            mFlagInfo = "Flag not recognized";
        }
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Flag: " + String.format("%02x ", mFlag) + "\n";
        res += "Flag Info: " + mFlagInfo;
        return res;
    }
}
