package com.huc.android_ble_monitor.models.AttProtocol;

import android.util.Pair;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.models.UuidFormat;
import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AttFindInformationRsp extends BaseAttPacket{
    public UuidFormat mFormat;
    public ArrayList<Pair<Short, UUID>> mHandleUuidList;

    public AttFindInformationRsp(L2capPacket p) {
        super(p);
        mFormat = UuidFormat.getUuidFormat(packet_data[1]);

        if(mFormat == UuidFormat.UUID_128_BIT){
            this.mHandleUuidList = decode128BitUuidTuples(packet_data);
        }else if(mFormat == UuidFormat.UUID_16_BIT){
            mHandleUuidList = decode16BitTuples(packet_data);
        }
    }

    /**
     * Decodes a list consisting of Handle and 128 Bit UUID tuples from the packet
     * @param data
     * @return
     */
    private ArrayList<Pair<Short, UUID>> decode128BitUuidTuples(Byte[] data){
        ArrayList<Pair<Short, UUID>> result = new ArrayList();
        int length = this.packet_length;
        int i = 2;

        while(length >= getTupleSize()) {
            // decode handle
            Short handle = decode16BitValue(data[i], data[i+1]);

            // convert arraylist<Byte> to byte[] and decode 128 bit UUID
            byte[] byteArray = new byte[16];
            int j = 0;
            for (Byte b : Arrays.copyOfRange(data, i + 2, i + 16)) {
                byteArray[j++] = b.byteValue();
            }
            //Reverse Array because of Little Endian formatting
            ArrayUtils.reverse(byteArray);

            UUID uuid = UUID.nameUUIDFromBytes(byteArray);
            result.add(new Pair(handle, uuid));
            i += getTupleSize();
            length -= getTupleSize();
        }
        return result;
    }

    /**
     * Decodes a list consisting of Handle and 16 Bit UUID tuples from the packet
     * @param data
     * @return
     */
    private ArrayList<Pair<Short, UUID>> decode16BitTuples(Byte[] data){
        ArrayList<Pair<Short, UUID>> result = new ArrayList();
        int length = this.packet_length;
        int i = 2;

        while(length >= getTupleSize()) {
            // decode handle
            Short handle = decode16BitValue(data[i], data[i+1]);

            // decode 16 bit UUID and insert into base UUID mnemonic
            String LSBHex = String.format("%02X", data[i + 2]);
            String MSBHex = String.format("%02X", data[i + 3]);
            String uuid16Bit = MSBHex + LSBHex;

            // convert 16 Bit UUID to 128 Bit UUID
            String uuid128Bit = BLE_BASE_UUID_16_BIT_MNEMONIC.replace("xxxx", uuid16Bit);
            result.add(new Pair(handle, UUID.fromString(uuid128Bit)));
            i += getTupleSize();
            length -= getTupleSize();
        }
        return result;
    }

    /**
     * element size of tuples where:
     * tuple = { short handle, UUID value }, with a UUID of 16 Bit or 128 Bit
     * @return element-size in Byte
     */
    private int getTupleSize() {
        return  mFormat == UuidFormat.UUID_128_BIT ? 2 + (128/8) : 2 + (16/8);
    }

    @Override
    public String toString(){
        String listString = "";
        for(Pair<Short, UUID> p : mHandleUuidList){
            listString += "Handle: " + BinaryUtil.shortToHexString(p.first) + ", " + "UUID: " + p.second.toString() + "\n";
        }

        String res = super.toString() + "\n";
        res += "UUID Format: " + mFormat.name() + "\n";
        res += "List of Handle + UUID tuples: \n";
        res += listString;

        return res;
    }
}
