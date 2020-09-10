package com.huc.android_ble_monitor.models.AttProtocol;

import android.util.Pair;

import java.util.ArrayList;
import java.util.UUID;

class AttFindInformationRes extends BaseAttPacket{
    public enum UuidFormat {
        UUID_16_BIT(0x01),
        UUID_128_BIT(0x02),
        UUID_FORMAT_UNKNOWN(0x00);
        private int format;

        UuidFormat(int format) {
            this.format = format;
        }
        public int getValue(){
            return this.format;
        }

        private static UuidFormat[] values = UuidFormat.values();
        public static UuidFormat getUuidFormat(int i) {
            for (UuidFormat e: UuidFormat.values()) {
                if(e.getValue() == i)
                    return e;
            }
            return UuidFormat.UUID_FORMAT_UNKNOWN;
        }
    }

    private UuidFormat mFormat;
    private ArrayList<Pair<Short, UUID>> mHandleUuidList;
    private final String BLE_BASE_UUID_16_BIT_MNEMONIC = "0000xxxx-0000-1000-8000-00805F9B34FB";

    public AttFindInformationRes(ArrayList<Byte> data, int number) {
        super(data, number);
        mFormat = UuidFormat.getUuidFormat(data.get(1));

        if(mFormat == UuidFormat.UUID_128_BIT){
            this.mHandleUuidList = decode128BitUuidTuples(data);
        }else if(mFormat == UuidFormat.UUID_16_BIT){
            mHandleUuidList = decode16BitTuples(data);
        }
    }

    /**
     * Decodes a list consisting of Handle and 128 Bit UUID tuples from the packet
     * @param data
     * @return
     */
    private ArrayList<Pair<Short, UUID>> decode128BitUuidTuples(ArrayList<Byte> data){
        ArrayList<Pair<Short, UUID>> result = new ArrayList();
        int length = this.packet_length;
        int i = 2;

        while(length >= getTupleSize()) {
            // decode handle
            Short handle = (short) ((data.get(i + 1) << 8) + data.get(i));
            // convert arraylist<Byte> to byte[] and decode 128 bit UUID
            byte[] byteArray = new byte[16];
            int j = 0;
            for (Byte b : data.subList(i + 2, i + 16)) {
                byteArray[j++] = b.byteValue();
            }
            UUID uuid = UUID.nameUUIDFromBytes(byteArray);
            result.add(new Pair(handle, uuid));
            i += getTupleSize();
        }
        return result;
    }

    /**
     * Decodes a list consisting of Handle and 16 Bit UUID tuples from the packet
     * @param data
     * @return
     */
    private ArrayList<Pair<Short, UUID>> decode16BitTuples(ArrayList<Byte> data){
        ArrayList<Pair<Short, UUID>> result = new ArrayList();
        int length = this.packet_length;
        int i = 2;

        while(length >= getTupleSize()) {
            // decode handle
            Short handle = (short) ((data.get(i + 1) << 8) + data.get(i));
            // decode 16 bit UUID and insert into base UUID mnemonic
            String LSBHex = String.format("%02X ", data.get(i + 2));
            String MSBHex = String.format("%02X ", data.get(i + 3));
            String uuid16Bit = MSBHex + LSBHex;

            // convert 16 Bit UUID to 128 Bit UUID
            String uuid128Bit = BLE_BASE_UUID_16_BIT_MNEMONIC.replace("xxxx", uuid16Bit);
            result.add(new Pair(handle, UUID.fromString(uuid128Bit)));
            i += getTupleSize();
        }
        return result;
    }

    /**
     * element size of tuples where:
     * tuple = { short handle, UUID value }, with a UUID of 16 Bit or 128 Bit
     * @return element-size in Byte
     */
    private int getTupleSize() {
        return  mFormat == UuidFormat.UUID_128_BIT ? 2 + (128/8) : 2 + (16/2);
    }
}
