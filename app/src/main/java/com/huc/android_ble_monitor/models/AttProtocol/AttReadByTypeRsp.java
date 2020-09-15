package com.huc.android_ble_monitor.models.AttProtocol;

import android.util.Pair;
import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class AttReadByTypeRsp extends BaseAttPacket {
    public byte mLength;
    public ArrayList<Pair<Short, Byte[]>> mHandleValuePairs;

    public AttReadByTypeRsp(Byte[] data) {
        super(data);
        mLength = data[1];
        mHandleValuePairs = decodeHandleValuePairs(data);
    }

    private ArrayList<Pair<Short, Byte[]>> decodeHandleValuePairs(Byte[] data){
        ArrayList<Pair<Short, Byte[]>> result = new ArrayList();
        int packet_length = this.packet_length;
        int i = 2;

        while(packet_length >= mLength){
            short handle = decodeHandle(data[i], data[i + 1]);
            Byte[] value = Arrays.copyOfRange(data, i + 2, i + mLength);
            ArrayUtils.reverse(value);
            result.add(new Pair(handle, value));
            i += mLength;
            packet_length -= mLength;
        }
        return result;
    }

    private Short decodeHandle(Byte LSB, Byte MSB){
        return (short)((MSB << 8) + LSB);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "List of { Handle, Value } tuples: \n";

        String list = "";
        for(Pair<Short, Byte[]> p : mHandleValuePairs){
            list += BinaryUtil.shortToHexString(p.first) + ", ";
            for(Byte b : p.second){
                list += String.format("%02X", b);
            }
            list += "\n";
        }

        res += list;
        return res;
    }
}
