package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttReadReq extends BaseAttPacket {
    public short mHandle;

    public AttReadReq(L2capPacket p) {
        super(p);
        mHandle = decode16BitValue(packet_data[1], packet_data[2]);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Requested Handle: " + BinaryUtil.shortToHexString(mHandle) + "\n";
        return res;
    }
}
