package com.huc.android_ble_monitor;

import android.app.Application;
import android.content.Context;

public class StaticContext extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}
