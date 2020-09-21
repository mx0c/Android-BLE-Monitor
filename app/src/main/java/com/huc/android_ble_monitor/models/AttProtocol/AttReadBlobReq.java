package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttReadBlobReq extends BaseAttPacket{
    public short mHandle;
    public short mOffset;

    public AttReadBlobReq(L2capPacket p) {
        super(p);
        mHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mOffset = decode16BitValue(packet_data[3], packet_data[4]);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handle to read: " + BinaryUtil.shortToHexString(this.mHandle) + "\n";
        res += "Value offset: " + mOffset;
        return res;
    }
}
