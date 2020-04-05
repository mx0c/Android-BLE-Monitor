package com.huc.android_ble_monitor.Models;

import java.io.UnsupportedEncodingException;

public class ParsedAdvertisingPacket {
    public int mAdvLength;
    public byte[] mAdvData;
    public byte mAdvType;
    public String mDecodedAdvData;

    public ParsedAdvertisingPacket(int len, byte advType, byte[] advData){
        this.mAdvData = advData;
        this.mAdvLength = len;
        this.mAdvType = advType;

        try {
            this.mDecodedAdvData = new String(advData,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            this.mDecodedAdvData = null;
            e.printStackTrace();
        }
    }
}
