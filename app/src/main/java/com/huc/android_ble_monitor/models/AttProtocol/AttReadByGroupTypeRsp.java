package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.Triplet;
import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class AttReadByGroupTypeRsp extends BaseAttPacket {
    public byte mLength;
    public ArrayList<Triplet<Short, Short, Byte[]>> mHandleValueTriplets;

    public AttReadByGroupTypeRsp(Byte[] data) {
        super(data);
        mLength = data[1];
        mHandleValueTriplets = decodeHandleValueTriplets(data);
    }

    private ArrayList<Triplet<Short, Short, Byte[]>> decodeHandleValueTriplets(Byte[] data){
        ArrayList<Triplet<Short, Short, Byte[]>> result = new ArrayList();
        int packet_length = this.packet_length;
        int i = 2;

        while(packet_length >= mLength){
            short attHandle = decodeHandle(data[i], data[i + 1]);
            short endGroupHandle = decodeHandle(data[i + 2], data[i + 3]);

            Byte[] value = Arrays.copyOfRange(data, i + 4, i + mLength);
            ArrayUtils.reverse(value);
            result.add(new Triplet(attHandle, endGroupHandle, value));
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
        res += "List of { Handle, End Group Handle, Value } tuples: \n";

        String list = "";
        for(Triplet<Short, Short, Byte[]> t : mHandleValueTriplets){
            list += BinaryUtil.shortToHexString(t.getFirst()) + ", ";
            list += BinaryUtil.shortToHexString(t.getSecond()) + ", ";
            for(Byte b : t.getThird()){
                list += String.format("%02X ", b);
            }
            list += "\n";
        }

        res += list;
        return res;
    }
}
