package com.huc.android_ble_monitor.util;

public interface IHciDecoder {
    void startHciLogStream(String filePath, int lastPacketCount);
    void stopHciLogStream();
    void setPacketReceptionCb(IPacketReceptionCallback cb);
}
