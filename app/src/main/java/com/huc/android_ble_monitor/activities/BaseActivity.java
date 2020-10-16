package com.huc.android_ble_monitor.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModel;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.dialogs.AboutDialog;
import com.huc.android_ble_monitor.dialogs.OpenSourceComponentsDialog;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.BinaryUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Base ActivityClass which sets Theme and binds to the BluetoothLeService
 */
public abstract class BaseActivity<T extends ViewModel> extends AppCompatActivity {
    protected static final String TAG = "";
    protected T mViewModel;
    protected BluetoothLeService mBluetoothLeService;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            //bound to service
            mBluetoothLeService = ((BluetoothLeService.LocalBinder)service).getService();
            onServiceBinded();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //not bounded to service
            mBluetoothLeService.disconnect();
            mBluetoothLeService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        setTheme(R.style.AppTheme); // Resets default theme after app was loaded
        initializeViewModel();

        //Bind to BluetoothLeService
        Intent serviceIntent = new Intent(getApplicationContext(), BluetoothLeService.class);
        boolean success = getApplicationContext().bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        Log.d(TAG,"bindService returned: " + Boolean.toString(success));

        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, MainActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(item));
    }

    /**
     * Used to initialize the navigation of the menu items in the navigation drawer menu
     */
    protected void initNavigationDrawerItemListeners(){
        NavigationView navigationView = this.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.action_hci_snoop:
                        Intent i = new Intent(BaseActivity.this, HciLogActivity.class);
                        startActivity(i);
                        return true;
                    case R.id.action_logging:
                        Intent j = new Intent(BaseActivity.this, ApplicationLogActivity.class);
                        startActivity(j);
                        break;
                    case R.id.action_about:
                        AboutDialog aboutDialog = new AboutDialog(BaseActivity.this);
                        aboutDialog.show();
                        break;
                    case R.id.action_components:
                        OpenSourceComponentsDialog oscDialog = new OpenSourceComponentsDialog(BaseActivity.this);
                        oscDialog.show();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * Initializes the ActionBarDrawerToggle which is used to retrieve the state of the navigation
     * drawer / open close it
     * @return Instance of the ActionBarDrawerToggle
     */
    protected void initActionBarDrawerToggle() {
        DrawerLayout drawerLayout = this.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_closed);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mActionBarDrawerToggle = actionBarDrawerToggle;
        initNavigationDrawerItemListeners();
    }

    protected abstract void onServiceBinded();
    protected abstract void initializeViewModel();
}
