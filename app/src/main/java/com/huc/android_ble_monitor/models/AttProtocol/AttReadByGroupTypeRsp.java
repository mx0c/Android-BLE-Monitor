package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.models.Triplet;
import com.huc.android_ble_monitor.util.BinaryUtil;
import com.huc.android_ble_monitor.util.DataUtil;

import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;

public class AttReadByGroupTypeRsp extends BaseAttPacket {
    public byte mLength;
    public ArrayList<Triplet<Short, Short, Byte[]>> mHandleValueTriplets;

    public AttReadByGroupTypeRsp(L2capPacket p) {
        super(p);
        mLength = packet_data[1];
        mHandleValueTriplets = decodeHandleValueTriplets(packet_data);
    }

    private ArrayList<Triplet<Short, Short, Byte[]>> decodeHandleValueTriplets(Byte[] data){
        ArrayList<Triplet<Short, Short, Byte[]>> result = new ArrayList();
        int packet_length = this.packet_length;
        int i = 2;

        while(packet_length >= mLength){
            short attHandle = decode16BitValue(data[i], data[i + 1]);
            short endGroupHandle = decode16BitValue(data[i + 2], data[i + 3]);

            Byte[] value = Arrays.copyOfRange(data, i + 4, i + mLength);
            ArrayUtils.reverse(value);
            result.add(new Triplet(attHandle, endGroupHandle, value));
            i += mLength;
            packet_length -= mLength;
        }
        return result;
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "List of { Handle, End Group Handle, Value } tuples: \n";

        String list = "";
        for(Triplet<Short, Short, Byte[]> t : mHandleValueTriplets){
            list += BinaryUtil.shortToHexString(t.getFirst()) + ", ";
            list += BinaryUtil.shortToHexString(t.getSecond()) + ", ";

            String data = "";
            for(Byte b : t.getThird()){
                data += String.format("%02X", b);
            }
            data += " (" + DataUtil.resolveUuidToNameInformation(data).name + ")";
            list += "0x" + data + "\n";
        }

        res += list;
        return res;
    }
}
