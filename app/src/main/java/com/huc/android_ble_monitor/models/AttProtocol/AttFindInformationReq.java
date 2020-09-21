package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttFindInformationReq extends BaseAttPacket {
    public short mStartingHandle;
    public short mEndingHandle;

    public AttFindInformationReq(Byte[] data) {
        super(data);
        mStartingHandle = decode16BitValue(data[1], data[2]);
        mEndingHandle = decode16BitValue(data[3], data[4]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(this.mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(this.mEndingHandle) + "\n";
        return res;
    }
}
