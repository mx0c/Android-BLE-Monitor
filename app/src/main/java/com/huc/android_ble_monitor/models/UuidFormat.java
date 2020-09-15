package com.huc.android_ble_monitor.models;

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

