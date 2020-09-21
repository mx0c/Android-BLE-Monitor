package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttSignedWriteCmd extends BaseAttPacket {
    public short mHandle;
    public Byte[] mValue;
    public Byte[] mAuthSig;

    public AttSignedWriteCmd(Byte[] data) {
        super(data);
        mHandle = decode16BitValue(data[1], data[2]);
        mValue = decodeValue(data, 3, this.packet_length - 13);
        mAuthSig = decodeValue(data, this.packet_length - 13, this.packet_length - 1);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handle to write: " + BinaryUtil.shortToHexString(this.mHandle) + "\n";
        res += "Value to write: ";
        for(byte b : this.mValue){
            res += String.format("%02x ", b);
        }
        return res;
    }
}
