package com.huc.android_ble_monitor.models.AttProtocol;

import java.util.ArrayList;

class AttFindInformationReq extends BaseAttPacket {
    private int mStartingHandle;
    private int mEndingHandle;

    public AttFindInformationReq(ArrayList<Byte> data, int number) {
        super(data, number);
        mStartingHandle = (data.get(2) << 8) + data.get(1);
        mEndingHandle = (data.get(4) << 8) + data.get(3);
    }
}
