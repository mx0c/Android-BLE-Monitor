package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttPrepareWriteReqRsp extends BaseAttPacket {
    public short mHandle;
    public short mOffset;
    public Byte[] mValue;

    public AttPrepareWriteReqRsp(Byte[] data) {
        super(data);
        mHandle = decode16BitValue(data[1], data[2]);
        mOffset = decode16BitValue(data[3], data[4]);
        mValue = decodeValue(data, 5, packet_length - 1);
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
