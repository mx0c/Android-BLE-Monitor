package com.huc.android_ble_monitor.models.AttProtocol;

import android.util.Pair;
import com.huc.android_ble_monitor.util.BinaryUtil;

import java.util.ArrayList;

public class AttFindByTypeValueRsp extends BaseAttPacket {
    public ArrayList<Pair<Short, Short>> mHandleInformationList;

    public AttFindByTypeValueRsp(Byte[] data) {
        super(data);
        mHandleInformationList = decodeHandleInformationList(data);
    }

    private ArrayList<Pair<Short, Short>> decodeHandleInformationList(Byte[] data){
        ArrayList<Pair<Short, Short>> result = new ArrayList();
        int packet_length = this.packet_length;
        int i = 1;

        while(packet_length >= 4){
            Short startHandle = (short) ((data[i + 1] << 8) + data[i]);
            Short groupEndHandle = (short) ((data[i + 3] << 8) + data[i + 2]);
            result.add(new Pair(startHandle, groupEndHandle));
            i += 4;
            packet_length -= 4;
        }
        return result;
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "List of { Found Handle, Group End Handle } tuples: \n";

        for(Pair<Short, Short> p : mHandleInformationList){
            res += BinaryUtil.shortToHexString(p.first) + ", ";
            res += BinaryUtil.shortToHexString(p.second);
            res += "\n";
        }

        return res;
    }
}
