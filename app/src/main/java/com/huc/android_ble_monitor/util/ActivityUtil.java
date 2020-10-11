package com.huc.android_ble_monitor.util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BluLeDevice;

import java.io.Serializable;
import java.util.Objects;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class ActivityUtil {
    /**
     * Helper Method to start a new activity
     *
     * @param cls
     * @param message Message passed as EXTRA_MESSAGE to new activity
     */
    public static void startNewActivity(Context ctx, Class<?> cls, @Nullable String message) {
        Intent intent = new Intent(ctx, cls);
        if (message != null) {
            intent.putExtra(EXTRA_MESSAGE, message);
        }

        ctx.startActivity(intent);
    }

    /**
     * Helper Method to start a new activity passing an object of <code>{@link BluLeDevice}</code>
     * @param ctx
     * @param cls
     * @param extraName
     * @param obj
     */
    public static void startNewActivity(Context ctx, Class<?> cls, String extraName, Serializable obj) {
        Intent intent = new Intent(ctx, cls);
        intent.putExtra(extraName, obj);

        ctx.startActivity(intent);
    }

    /**
     * @param activity
     * @param isHomeEnabled
     */
    public static void setToolbar(AppCompatActivity activity, boolean isHomeEnabled) {
        MaterialToolbar toolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        if (isHomeEnabled) {
            Objects.requireNonNull(activity.getSupportActionBar()).setHomeAsUpIndicator(R.drawable.baseline_menu_white_24dp); // Workaround because setDisplayHome did not work
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * sets the appbar title
     * @param activity
     * @param title
     */
    public static void setToolbarTitle(AppCompatActivity activity, String title){
        Toolbar tb = (Toolbar)activity.findViewById(R.id.toolbar);
        tb.setTitle(title);
    }
}
