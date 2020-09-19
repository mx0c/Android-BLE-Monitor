package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttFindInformationReq extends BaseAttPacket {
    public short mStartingHandle;
    public short mEndingHandle;

    public AttFindInformationReq(Byte[] data) {
        super(data);
        mStartingHandle = (short) ((data[2] << 8) + data[1]);
        mEndingHandle = (short) ((data[4] << 8) + data[3]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(this.mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(this.mEndingHandle) + "\n";
        return res;
    }
}
