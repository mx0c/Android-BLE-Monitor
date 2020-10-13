package com.huc.android_ble_monitor.models;

public enum HciType {
    ALL("All"),
    COMMAND("Command"),
    EVENT("Event"),
    ACL_DATA("ACL_Data"),
    SCO_DATA("SCO_Data");

    private String hciType;

    HciType(String type) {
        this.hciType = type;
    }

    public String getHciType() {
        return hciType;
    }
}
