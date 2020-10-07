package com.huc.android_ble_monitor.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.hciLogActivity.HciPacketListAdapter;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.HciSnoopLogUtil;
import com.huc.android_ble_monitor.util.IPacketReceptionCallback;
import com.huc.android_ble_monitor.viewmodels.HciLogViewModel;

public class HciLogActivity extends BaseActivity<HciLogViewModel> implements IPacketReceptionCallback {
    private static final String TAG = "BLEM_HciLogAct";
    private Spinner mProtocolSpinner;
    public Spinner mTypeSpinner;
    public ListView mListView;
    public ArrayAdapter mAdapter;
    public TextView mMethodTextView;
    public Spinner mMethodSpinner;
    public TextView mTypeHeaderTextview;
    public TextView mMethodHeaderTextview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hci_log);
        ActivityUtil.setToolbar(this, false);
        ActivityUtil.setToolbarTitle(this, "BLE Protocol Log");

        mMethodSpinner = findViewById(R.id.methodSpinner);
        mMethodTextView = findViewById(R.id.methodTextview);
        mTypeHeaderTextview = findViewById(R.id.header_tv_1);
        mMethodHeaderTextview = findViewById(R.id.header_tv_2);

        mListView = findViewById(R.id.hci_log_listView);
        mAdapter = new HciPacketListAdapter(this, mViewModel.getSnoopPackets().getValue());
        mListView.setAdapter(mAdapter);
        HciSnoopLogUtil snoopLog = new HciSnoopLogUtil(this, HciLogActivity.this);

        mProtocolSpinner = findViewById(R.id.protocolSpinner);
        mProtocolSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.ProtocolArray, android.R.layout.simple_spinner_item));
        mProtocolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String protocol = parent.getItemAtPosition(position).toString();
                mViewModel.changeProtocol(protocol, HciLogActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mTypeSpinner = findViewById(R.id.typeSpinner);
    }

    @Override
    protected void onServiceBinded() {}

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(HciLogViewModel.class);
        mViewModel.init();
    }

    @Override
    public void onHciFrameReceived(final String snoopFrame, final String hciFrame) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewModel.addSnoopPacket(new HciPacket(snoopFrame, hciFrame));
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFinishedPacketCount(int packetCount) {
        Log.d(TAG, "onFinishedPacketCount: " + packetCount);
    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        Log.e(TAG, "onError: " + errorMessage + " code: " + errorCode);
    }
}
