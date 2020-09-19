package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class AttHandleValueNtf extends BaseAttPacket{
    public Short mHandle;
    public Byte[] mValue;

    public AttHandleValueNtf(Byte[] data) {
        super(data);
        mHandle = (short) ((data[2] << 8) + data[1]);
        mValue = Arrays.copyOfRange(data, 3, this.packet_length - 1);
        ArrayUtils.reverse(mValue);
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handle: " + BinaryUtil.shortToHexString(mHandle) + "\n";
        res += "Value: \n";
        for(Byte b : mValue){
            res += String.format("%02x ", b);
        }
        return res;
    }
}
