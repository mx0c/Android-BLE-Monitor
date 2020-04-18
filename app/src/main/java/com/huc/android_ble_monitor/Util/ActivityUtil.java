package com.huc.android_ble_monitor.Util;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.huc.android_ble_monitor.R;

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
     * @param activity
     * @param isHomeEnabled
     */
    public static void setToolbar(AppCompatActivity activity, boolean isHomeEnabled) {
        MaterialToolbar mToolbar = activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(mToolbar);
        if (isHomeEnabled) {
            Objects.requireNonNull(activity.getSupportActionBar()).setHomeAsUpIndicator(R.drawable.baseline_menu_white_24dp); // Workaround because setDisplayHome did not work
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            Objects.requireNonNull(activity.getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
    }
}
