package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttFindInformationReq extends BaseAttPacket {
    public short mStartingHandle;
    public short mEndingHandle;

    public AttFindInformationReq(L2capPacket p) {
        super(p);
        mStartingHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mEndingHandle = decode16BitValue(packet_data[3], packet_data[4]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(this.mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(this.mEndingHandle) + "\n";
        return res;
    }
}
