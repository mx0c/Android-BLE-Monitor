package com.huc.android_ble_monitor.models;

public enum AttOpCodeMethod {
    NOT_DEFINED(0x00),
    ATT_ERROR_RSP(0x01),
    ATT_EXCHANGE_MTU_REQ(0x02),
    ATT_EXCHANGE_MTU_RSP(0x03),
    ATT_FIND_INFORMATION_REQ(0x04),
    ATT_FIND_INFORMATION_RSP(0x05),
    ATT_FIND_BY_TYPE_VALUE_REQ(0x06),
    ATT_FIND_BY_TYPE_VALUE_RSP(0x07),
    ATT_READ_BY_TYPE_REQ(0x08),
    ATT_READ_BY_TYPE_RSP(0x09),
    ATT_READ_REQ(0x0A),
    ATT_READ_RSP(0x0B),
    ATT_READ_BLOB_REQ(0x0C),
    ATT_READ_BLOB_RSP(0x0D),
    ATT_READ_MULTIPLE_REQ(0x0E),
    ATT_READ_MULTIPLE_RSP(0x0F),
    ATT_READ_BY_GROUP_TYPE_REQ(0x10),
    ATT_READ_BY_GROUP_TYPE_RSP(0x11),
    ATT_WRITE_REQ(0x12),
    ATT_WRITE_RSP(0x13),
    ATT_WRITE_CMD(0x52),
    ATT_PREPARE_WRITE_REQ(0x16),
    ATT_PREPARE_WRITE_RSP(0x17),
    ATT_EXECUTE_WRITE_REQ(0x18),
    ATT_EXECUTE_WRITE_RSP(0x19),
    ATT_READ_MULTIPLE_VARIABLE_REQ(0x20),
    ATT_READ_MULTIPLE_VARIABLE_RSP(0x21),
    ATT_MULTIPLE_HANDLE_VALUE_NTF(0x23),
    ATT_HANDLE_VALUE_NTF(0x1B),
    ATT_HANDLE_VALUE_IND(0x1D),
    ATT_HANDLE_VALUE_CFM(0x1E),
    ATT_SIGNED_WRITE_CMD(0xD2);

    private int opcode;

    private AttOpCodeMethod(int opcode) {
        this.opcode = opcode;
    }

    public int getValue(){
        return this.opcode;
    }

    private static AttOpCodeMethod[] values = AttOpCodeMethod.values();
    public static AttOpCodeMethod getAttOpCodeMethod(int i) {
        for (AttOpCodeMethod e: AttOpCodeMethod.values()) {
            if(e.getValue() == i)
                return e;
        }
        return AttOpCodeMethod.NOT_DEFINED;
    }
}