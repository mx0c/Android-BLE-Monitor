package com.huc.android_ble_monitor.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.mainActivity.ScanResultRecyclerAdapter;
import com.huc.android_ble_monitor.models.BluLeDevice;
import com.huc.android_ble_monitor.models.ToastModel;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.BleUtil;
import com.huc.android_ble_monitor.util.PermissionsUtil;
import com.huc.android_ble_monitor.viewmodels.MainActivityViewModel;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import java.util.List;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity<MainActivityViewModel> implements ScanResultRecyclerAdapter.OnDeviceConnectListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "BLEM_MainActivity";

    private SwitchCompat mBluetoothSwitch;
    private RecyclerView mScanResultRecyclerView;
    private ScanResultRecyclerAdapter mScanResultRecyclerAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    public StatesRecyclerViewAdapter mStatesRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScanResultRecyclerView = findViewById(R.id.scan_result_recycler_view);
        ActivityUtil.setToolbar(this, true);

        PermissionsUtil.requestLocationPermission(this);
        PermissionsUtil.requestReadStoragePermission(this);
        BleUtil.checkIsBluetoothEnabled(this);
        BleUtil.checkBleAvailability(this);

        // Initializes vertical Actionbar
        initActionBarDrawerToggle();

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

        setObservers();
        initRecyclerView();
    }

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.init();
    }

    @Override
    protected void onServiceBinded() {
        mBluetoothLeService.getScanResult().observe(MainActivity.this, new Observer<ScanResult>() {
            @Override
            public void onChanged(ScanResult scanResult) {
                mViewModel.registerScanResult(scanResult);
            }
        });
    }

    public void setObservers(){
        mViewModel.getToast().observe(this, new Observer<ToastModel>() {
            @Override
            public void onChanged(ToastModel toastModel) {
            Toast.makeText(MainActivity.this, toastModel.getMessage(), toastModel.getDuration()).show();
            }
        });
        mViewModel.getmBleDevices().observe(this, new Observer<List<BluLeDevice>>() {
            @Override
            public void onChanged(List<BluLeDevice> bleDevices) {
                mScanResultRecyclerView.getRecycledViewPool().clear();

                if(bleDevices.size() > 0){
                    mStatesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
                }else if(bleDevices.size() <= 0 && mViewModel.isScanEnabled()){
                    mStatesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
                }else if(bleDevices.size() <= 0 && !mViewModel.isScanEnabled()){
                    mStatesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
                }

                mScanResultRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView(){
        // Parent of Recycler View
        mSwipeRefreshLayout = findViewById(R.id.swipe_container_scan_result);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mScanResultRecyclerAdapter = new ScanResultRecyclerAdapter(this, mViewModel.getmBleDevices().getValue(), this);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mScanResultRecyclerView.setLayoutManager(linearLayoutManager);
        mScanResultRecyclerView.setAdapter(mScanResultRecyclerAdapter);

        mScanResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScanResultRecyclerView.setHasFixedSize(true);

        View loadingView = getLayoutInflater().inflate(R.layout.view_loading, mScanResultRecyclerView, false);
        View emptyView = getLayoutInflater().inflate(R.layout.view_empty, mScanResultRecyclerView, false);

        mStatesRecyclerViewAdapter = new StatesRecyclerViewAdapter(mScanResultRecyclerAdapter, loadingView, emptyView, null);
        mScanResultRecyclerView.setAdapter(mStatesRecyclerViewAdapter);
        mStatesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        mBluetoothSwitch.setChecked(false);
                        mViewModel.setBluetoothEnabled(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        BleUtil.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        BleUtil.mBleScanner = BleUtil.mBluetoothAdapter.getBluetoothLeScanner();
                        mViewModel.setBluetoothEnabled(true);
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    // EventHandlers related to the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appbar, menu);
        MenuItem menuItem = menu.findItem(R.id.app_bar_switch_item);
        View view = menuItem.getActionView();
        mBluetoothSwitch = view.findViewById(R.id.switch_compat_element);

        mBluetoothSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(mViewModel.isBluetoothEnabled()) {
                        Log.d(TAG, "BLE Switch checked. Scanning BLE Devices.");
                        mBluetoothLeService.scanForDevices(true);
                        mViewModel.setScanEnabled(true);
                        if(mStatesRecyclerViewAdapter.getState() != StatesRecyclerViewAdapter.STATE_NORMAL) {
                            mStatesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
                        }
                    } else {
                        BleUtil.checkIsBluetoothEnabled(MainActivity.this);
                        buttonView.setChecked(false);
                    }
                } else {
                    Log.d(TAG, "BLE Switch unchecked. Stopped scanning BLE Devices.");
                    mViewModel.setScanEnabled(false);
                }
            }
        });
        return true;
    }

    /**
     * Called when clicking on an option in the Toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mActionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_hci_snoop:
                Intent i = new Intent(this, HciLogActivity.class);
                startActivity(i);
                return true;
            case R.id.action_logging:
                Intent j = new Intent(this, ApplicationLogActivity.class);
                startActivity(j);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Called when clicking on a Device Item
     * @param position
     */
    @Override
    public void onDeviceClick(int position) {
        try {
            final BluLeDevice bleDevice = mViewModel.getmBleDevices().getValue().get(position);

            if (Build.VERSION.SDK_INT >= 26) {
                if (bleDevice.mScanResult.isConnectable()) {
                    mBluetoothLeService.connect(bleDevice);
                    DeviceDetailActivity.staticBleDevice = bleDevice;
                    Intent intent = new Intent(this, DeviceDetailActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Device is not connectable.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                mBluetoothLeService.connect(bleDevice);
                DeviceDetailActivity.staticBleDevice = bleDevice;
                Intent intent = new Intent(this, DeviceDetailActivity.class);
                startActivity(intent);
            }
        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "onDeviceClick: IndexOutOfBounds Exception: " + e.getStackTrace().toString());
        }
    }

    /**
     * called after location permission are requested
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: " + "Request code " + requestCode +  ", Result Code " + resultCode);
        switch (requestCode) {
            case PermissionsUtil.REQUEST_ENABLE_BT_RESULT:
                if (resultCode == Activity.RESULT_OK) {
                    mViewModel.setBluetoothEnabled(true);
                } else {
                    mViewModel.setBluetoothEnabled(false);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    @Override
    public void onRefresh() {
        Log.d(TAG, "onRefresh: ScanResult List refreshed.");
        mViewModel.clearBleDevices();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
