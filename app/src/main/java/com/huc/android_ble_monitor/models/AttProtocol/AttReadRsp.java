package com.huc.android_ble_monitor.models.AttProtocol;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

class AttReadRsp extends BaseAttPacket {
    public Byte[] mValue;

    public AttReadRsp(Byte[] data, int number) {
        super(data, number);
        mValue = decodeValue(data);
    }

    private Byte[] decodeValue(Byte[] data){
        Byte[] res = Arrays.copyOfRange(data, 1, this.packet_length - 2);
        ArrayUtils.reverse(res);
        return res;
    }
}
