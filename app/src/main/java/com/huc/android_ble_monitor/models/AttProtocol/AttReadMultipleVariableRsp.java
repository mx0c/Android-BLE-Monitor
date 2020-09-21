package com.huc.android_ble_monitor.models.AttProtocol;

import android.util.Pair;

import com.huc.android_ble_monitor.models.L2capPacket;

import java.util.ArrayList;

public class AttReadMultipleVariableRsp extends BaseAttPacket {
    public ArrayList<Pair<Short, Byte[]>> mLengthValueList;

    public AttReadMultipleVariableRsp(L2capPacket p) {
        super(p);
        mLengthValueList = decodeLengthValueList(packet_data);
    }

    private ArrayList<Pair<Short, Byte[]>> decodeLengthValueList(Byte[] data){
        int i = 1;
        int len = packet_length;
        ArrayList<Pair<Short, Byte[]>> res = new ArrayList<>();

        while(len >= 2){
            short length = decode16BitValue(data[i], data[i+1]);
            Byte[] value = decodeValue(data, i+2, i+2+length);
            res.add(new Pair(length, value));
            i += 2 + length;
            len -= 2 + length;
        }

        return res;
    }

    @Override
    public String toString() {
        String res = super.toString() + "\n";
        res += "List of { Length, Value } tuples: \n";
        for(Pair<Short, Byte[]> p : mLengthValueList){
            res += p.first + ", ";
            for(Byte b : p.second){
                res += String.format("%02x ", b);
            }
            res += "\n";
        }
        return res;
    }
}
