package com.huc.android_ble_monitor.util;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.activities.ApplicationLogActivity;
import com.huc.android_ble_monitor.activities.HciLogActivity;
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

    /**
     * Used to initialize the navigation of the menu items in the navigation drawer menu
     * @param activity Context of the activity where the drawer is used
     */
    public static void initNavigationDrawerItemListeners(final AppCompatActivity activity){
        NavigationView navigationView = activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_hci_snoop:
                        Intent i = new Intent(activity, HciLogActivity.class);
                        activity.startActivity(i);
                        return true;
                    case R.id.action_logging:
                        Intent j = new Intent(activity, ApplicationLogActivity.class);
                        activity.startActivity(j);
                        break;
                    case R.id.update_assigned_numbers:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Initializes the ActionBarDrawerToggle which is used to retrieve the state of the navigation
     * drawer / open close it
     * @param activity Context of the activity where the drawer is used
     * @return Instance of the ActionBarDrawerToggle
     */
    public static ActionBarDrawerToggle initActionBarDrawerToggle(final AppCompatActivity activity) {
        DrawerLayout drawerLayout = activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        return actionBarDrawerToggle;
    }
}
