package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;
import java.util.UUID;

public class AttFindByTypeValueReq extends BaseAttPacket {
    public Short mStartingHandle;
    public Short mEndingHandle;
    public UUID mType;
    public Byte[] mValue;

    public AttFindByTypeValueReq(L2capPacket p) {
        super(p);
        mStartingHandle = decode16BitValue(packet_data[1], packet_data[2]);
        mEndingHandle = decode16BitValue(packet_data[3], packet_data[4]);
        mType = decodeTypeUUID(packet_data);
        mValue = decodeValue(packet_data,7, this.packet_length - 1);
    }

    private UUID decodeTypeUUID(Byte[] data){
        String LSBHex = String.format("%02X", data[5]);
        String MSBHex = String.format("%02X", data[6]);
        String uuid16Bit = MSBHex + LSBHex;
        // convert 16 Bit UUID to 128 Bit UUID
        String uuid128Bit = BLE_BASE_UUID_16_BIT_MNEMONIC.replace("xxxx", uuid16Bit);
        return UUID.fromString(uuid128Bit);
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + BinaryUtil.shortToHexString(this.mStartingHandle) + "\n";
        res += "Ending Handle: " + BinaryUtil.shortToHexString(this.mEndingHandle) + "\n";
        res += "Type UUID: " + mType.toString() + "\n";
        res += "Value: \n";
        for(Byte b : mValue){
            res += String.format("%02X ", b);
        }
        return res;
    }
}
