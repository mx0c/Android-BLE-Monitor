package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttReadReq extends BaseAttPacket {
    public short mHandle;

    public AttReadReq(Byte[] data) {
        super(data);
        mHandle = decode16BitValue(data[1], data[2]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Requested Handle: " + BinaryUtil.shortToHexString(mHandle) + "\n";
        return res;
    }
}
