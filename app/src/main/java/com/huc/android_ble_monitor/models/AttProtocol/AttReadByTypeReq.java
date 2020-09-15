package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.util.BinaryUtil;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.UUID;

public class AttReadByTypeReq extends BaseAttPacket {
    public short mStartingHandle;
    public short mEndingHandle;
    public UUID mTypeUuid;

    public AttReadByTypeReq(Byte[] data) {
        super(data);
        mStartingHandle = (short) ((data[2] << 8) + data[1]);
        mEndingHandle = (short) ((data[4] << 8) + data[3]);
        mTypeUuid = decodeUuid(data);
    }

    private UUID decodeUuid(Byte[] data){
        // subtract length of opcode (1 Byte) + both handles (4 Byte) from packet length
        int uuid_length = super.packet_length - 5;
        // 16 Bit UUID
        if(uuid_length == 2){
            // decode 16 bit UUID and insert into base UUID mnemonic
            String LSBHex = String.format("%02X", data[5]);
            String MSBHex = String.format("%02X", data[6]);
            String uuid16Bit = MSBHex + LSBHex;
            // convert 16 Bit UUID to 128 Bit UUID
            String uuid128Bit = BLE_BASE_UUID_16_BIT_MNEMONIC.replace("xxxx", uuid16Bit);
            return UUID.fromString(uuid128Bit);
        }
        // 128 Bit UUID
        else if(uuid_length == 16){
            byte[] uuidByteArr =  ArrayUtils.toPrimitive(Arrays.copyOfRange(data, 5, this.packet_length - 1));
            ArrayUtils.reverse(uuidByteArr);
            return UUID.nameUUIDFromBytes(uuidByteArr);
        }
        return null;
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(mEndingHandle) + "\n";
        res += "Type UUID: " + mTypeUuid.toString() + "\n";
        return res;
    }
}