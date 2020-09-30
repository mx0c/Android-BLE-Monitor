package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;
import com.huc.android_ble_monitor.util.DataUtil;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.UUID;

public class AttReadByGroupTypeReq extends BaseAttPacket{
    public short mStartingHandle;
    public short mEndingHandle;
    public String mAttributeGroupType;

    public AttReadByGroupTypeReq(L2capPacket p) {
        super(p);
        mStartingHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mEndingHandle = decode16BitValue(packet_data[3], packet_data[4]);
        mAttributeGroupType = decodeUuid(packet_data);
    }

    private String decodeUuid(Byte[] data){
        // subtract length of opcode (1 Byte) + both handles (4 Byte) from packet length
        int uuid_length = super.packet_length - 5;
        // 16 Bit UUID
        if(uuid_length == 2){
            // decode 16 bit UUID and insert into base UUID mnemonic
            String LSBHex = String.format("%02X", data[5]);
            String MSBHex = String.format("%02X", data[6]);
            return MSBHex + LSBHex;
        }
        // 128 Bit UUID
        else if(uuid_length == 16){
            byte[] uuidByteArr =  ArrayUtils.toPrimitive(Arrays.copyOfRange(data, 5, this.packet_length - 1));
            ArrayUtils.reverse(uuidByteArr);
            return UUID.nameUUIDFromBytes(uuidByteArr).toString();
        }
        return null;
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(mEndingHandle) + "\n";
        res += "Attribute Group Type UUID: (" + DataUtil.resolveUuidToNameInformation(mAttributeGroupType).name + ") 0x"+ mAttributeGroupType +"\n";
        return res;
    }
}
