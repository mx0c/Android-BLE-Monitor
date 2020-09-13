package com.huc.android_ble_monitor.models.AttProtocol;

public class AttFindInformationReq extends BaseAttPacket {
    public int mStartingHandle;
    public int mEndingHandle;

    public AttFindInformationReq(Byte[] data) {
        super(data);
        mStartingHandle = (data[2] << 8) + data[1];
        mEndingHandle = (data[4] << 8) + data[3];
    }

    @Override
    public String toString(){
        String res = super.toString() + "\n";
        res += "Starting Handle: " + this.mStartingHandle + "\n";
        res += "Ending Handle: " + this.mEndingHandle + "\n";
        return res;
    }
}
