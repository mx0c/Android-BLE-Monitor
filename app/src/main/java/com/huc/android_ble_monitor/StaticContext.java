package com.huc.android_ble_monitor;

import android.app.Application;
import android.content.Context;

/**
 * Static Context which is necessary to read in JSON Files static without the need of a context
 */
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
