package com.huc.android_ble_monitor.models.AttProtocol;


public class AttErrorRsp extends BaseAttPacket {
    public AttErrorRsp(Byte[] data) {
        super(data);
        this.requested_opcode_error = AttOpCodeMethod.getAttOpCodeMethod(data[1]);
        this.error_attribute_handle = decodeErrorAttHandle(data);
        this.error_code = AttErrorCode.getAttErrorCode(data[4]);
    }

    private short decodeErrorAttHandle(Byte[] data){
        Byte LSB = data[2];
        Byte MSB = data[3];
        return (short)((MSB << 8) + LSB);
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
        res += "Error Attribute Handle: " + this.error_attribute_handle + "\n";
        res += "Error: " + this.error_code.name() + "\n";
        return res;
    }
}
