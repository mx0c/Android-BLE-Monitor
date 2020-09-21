package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttPrepareWriteReqRsp extends BaseAttPacket {
    public short mHandle;
    public short mOffset;
    public Byte[] mValue;

    public AttPrepareWriteReqRsp(L2capPacket p) {
        super(p);
        mHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mOffset = decode16BitValue(packet_data[3], packet_data[4]);
        mValue = decodeValue(packet_data, 5, packet_length - 1);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handle to write: " + BinaryUtil.shortToHexString(this.mHandle) + "\n";
        res += "Offset of the first octet to be written: " + BinaryUtil.shortToHexString(this.mOffset);
        res += "Value to write (Hex): ";
        for(byte b : this.mValue){
            res += String.format("%02x ", b);
        }
        return res;
    }
}
