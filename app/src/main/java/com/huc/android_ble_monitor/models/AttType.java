package com.huc.android_ble_monitor.models;

public enum AttType {
    ALL("All"),
    COMMAND("Command"),
    REQUEST("Request"),
    RESPONSE("Response"),
    NOTIFICATION("Notification"),
    INDICATION("Indication"),
    CONFIRMATION("Confirmation");

    private String attType;

    AttType(String type) {
        this.attType = type;
    }

    public String getAttType() {
        return attType;
    }

}

