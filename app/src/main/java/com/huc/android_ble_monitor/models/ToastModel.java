package com.huc.android_ble_monitor.models;

import android.widget.Toast;

public class ToastModel {
    int duration;
    String message;

    public ToastModel(int duration, String message) {
        this.duration = duration;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        if(duration == Toast.LENGTH_LONG) {
            this.duration = 1;
        } else {
            this.duration = 0;
        }
    }
}
