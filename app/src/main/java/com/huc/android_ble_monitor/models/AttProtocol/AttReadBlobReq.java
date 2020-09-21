package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttReadBlobReq extends BaseAttPacket{
    public short mHandle;
    public short mOffset;

    public AttReadBlobReq(Byte[] data) {
        super(data);
        mHandle = decode16BitValue(data[1], data[2]);
        mOffset = decode16BitValue(data[3], data[4]);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handle to read: " + BinaryUtil.shortToHexString(this.mHandle) + "\n";
        res += "Value offset: " + mOffset;
        return res;
    }
}
