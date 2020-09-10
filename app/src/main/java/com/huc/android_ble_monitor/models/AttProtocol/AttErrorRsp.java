package com.huc.android_ble_monitor.models.AttProtocol;

import java.util.ArrayList;

class AttErrorRsp extends BaseAttPacket {
    public AttErrorRsp(ArrayList<Byte> data, int number) {
        super(data, number);
        this.requested_opcode_error = AttOpCodeMethod.getAttOpCodeMethod(data.get(1));
        this.error_attribute_handle = extractErrorAttHandle(data);
        this.error_code = AttErrorCode.getAttErrorCode(data.get(4));
    }

    private int extractErrorAttHandle(ArrayList<Byte> data){
        Byte LSB = data.get(2);
        Byte MSB = data.get(3);
        return (MSB << 8) + LSB;
    }

    /**
     * The request that generated this ATT_ERROR_RSP PDU.
     */
    public AttOpCodeMethod requested_opcode_error;

    /**
     * The attribute handle that generated this ATT_ERROR_RSP PDU.
     */
    public int error_attribute_handle;

    /**
     * The reason why the request has generated an ATT_ERROR_RSP PDU.
     */
    public AttErrorCode error_code;
}