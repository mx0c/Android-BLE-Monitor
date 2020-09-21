package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class AttHandleValueInd extends BaseAttPacket {
    public Short mHandle;
    public Byte[] mValue;

    public AttHandleValueInd(Byte[] data) {
        super(data);
        mHandle = decode16BitValue(data[1], data[2]);
        mValue = decodeValue(data, 3, this.packet_length - 1);
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
