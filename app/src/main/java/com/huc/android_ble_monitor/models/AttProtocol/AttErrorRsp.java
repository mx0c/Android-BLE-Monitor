package com.huc.android_ble_monitor.models.AttProtocol;


import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.BinaryUtil;

public class AttErrorRsp extends BaseAttPacket {
    public AttErrorRsp(L2capPacket p) {
        super(p);
        this.requested_opcode_error = AttOpCodeMethod.getAttOpCodeMethod(packet_data[1]);
        this.error_attribute_handle = decode16BitValue(packet_data[2], packet_data[3]);
        this.error_code = AttErrorCode.getAttErrorCode(packet_data[4]);
    }

    /**
     * The request that generated this ATT_ERROR_RSP PDU.
     */
    public AttOpCodeMethod requested_opcode_error;

    /**
     * The attribute handle that generated this ATT_ERROR_RSP PDU.
     */
    public short error_attribute_handle;

    /**
     * The reason why the request has generated an ATT_ERROR_RSP PDU.
     */
    public AttErrorCode error_code;

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Error caused by: " + this.requested_opcode_error.name() + "\n";
        res += "Error Attribute Handle: " + BinaryUtil.shortToHexString(this.error_attribute_handle) + "\n";
        res += "Error: " + this.error_code.name() + "\n";
        return res;
    }
}
