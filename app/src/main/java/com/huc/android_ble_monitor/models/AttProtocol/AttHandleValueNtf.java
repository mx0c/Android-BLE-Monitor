package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class AttHandleValueNtf extends BaseAttPacket{
    public Short mHandle;
    public Byte[] mValue;

    public AttHandleValueNtf(L2capPacket p) {
        super(p);
        mHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mValue = decodeValue(packet_data, 3, this.packet_length - 1);
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
