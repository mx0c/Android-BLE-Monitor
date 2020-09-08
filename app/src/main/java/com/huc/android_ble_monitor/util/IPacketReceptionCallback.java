package com.huc.android_ble_monitor.util;

public interface IPacketReceptionCallback {
    void onHciFrameReceived(final String snoopFrame, final String hciFrame);
    void onFinishedPacketCount(int packetCount);
    void onError(int errorCode, String errorMessage);
}
