package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class AttWriteReq extends BaseAttPacket {
    public short mHandle;
    public Byte[] mValue;

    public AttWriteReq(Byte[] data) {
        super(data);
        mHandle = decodeHandle(data);
        mValue = decodeValue(data);
    }

    private Byte[] decodeValue(Byte[] data){
        Byte[] temp = Arrays.copyOfRange(data, 3, this.packet_length - 1);
        ArrayUtils.reverse(temp);
        return temp;
    }

    private short decodeHandle(Byte[] data){
        byte LSB = data[1];
        byte MSB = data[2];
        return (short)((MSB << 8) + LSB);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Handle to write: " + BinaryUtil.shortToHexString(this.mHandle) + "\n";
        res += "Value to write: ";
        for(byte b : this.mValue){
            res += String.format("%02x ", b);
        }
        return res;
    }
}
