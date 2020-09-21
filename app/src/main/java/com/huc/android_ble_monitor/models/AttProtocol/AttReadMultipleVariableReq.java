package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;
import java.util.ArrayList;

public class AttReadMultipleVariableReq extends BaseAttPacket {
    public ArrayList<Short> mHandles;

    public AttReadMultipleVariableReq(Byte[] data) {
        super(data);
        mHandles = decodeHandles(data);
    }

    private ArrayList<Short> decodeHandles(Byte[] data){
        int i = 1;
        int len = packet_length - 1;
        ArrayList<Short> res = new ArrayList<>();

        while(len >= 2){
            res.add(decode16BitValue(data[i], data[i+1]));
            len -= 2;
            i += 2;
        }

        return res;
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "Handles to read: \n";
        for(short b : mHandles){
            res += BinaryUtil.shortToHexString(b);
        }
        return res;
    }
}
