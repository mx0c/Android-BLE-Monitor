package com.huc.android_ble_monitor.models.AttProtocol;

class AttFindInformationReq extends BaseAttPacket {
    public int mStartingHandle;
    public int mEndingHandle;

    public AttFindInformationReq(Byte[] data, int number) {
        super(data, number);
        mStartingHandle = (data[2] << 8) + data[1];
        mEndingHandle = (data[4] << 8) + data[3];
    }
}
