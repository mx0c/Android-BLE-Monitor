package com.huc.android_ble_monitor.models.AttProtocol;

public enum AttErrorCode {
    NOT_DEFINED(0x00),
    INVALID_HANDlE(0x01),
    READ_NOT_PERMITTED(0x02),
    WRITE_NOT_PERMITTED(0x03),
    INVALID_PDU(0x04),
    INSUFFICIENT_AUTHENTICATION(0x05),
    REQUEST_NOT_SUPPORTED(0x06),
    INVALID_OFFSET(0x07),
    INSUFFICIENT_AUTHORIZATION(0x08),
    PREPARE_QUEUE_FULL(0x09),
    ATTRIBUTE_NOT_FOUND(0x0A),
    ATTRIBUTE_NOT_LONG(0x0B),
    INSUFFICIENT_ENCRYPTION_KEY_SIZE(0x0C),
    INVALID_ATTRIBUTE_VALUE_LENGTH(0x0D),
    UNLIKELY_ERROR(0x0E),
    INSUFFICIENT_ENCRYPTION(0x0F),
    UNSUPPORTED_GROUP_TYPE(0x10),
    INSUFFICIENT_RESOURCES(0x11),
    DATABASE_OUT_OF_SYNC(0x12),
    VALUE_NOT_ALLOWED(0x13);

    private int errCode;

    AttErrorCode(int errCode) {
        this.errCode = errCode;
    }

    public int getValue(){
        return this.errCode;
    }

    private static AttErrorCode[] values = AttErrorCode.values();
    public static AttErrorCode getAttErrorCode(int i) {
        for (AttErrorCode e: AttErrorCode.values()) {
            if(e.getValue() == i)
                return e;
        }
        return AttErrorCode.NOT_DEFINED;
    }
}