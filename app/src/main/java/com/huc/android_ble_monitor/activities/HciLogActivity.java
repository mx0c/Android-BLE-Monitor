package com.huc.android_ble_monitor.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.HciPacketListAdapter;
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
    public HciPacketListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hci_logging_activity);
        ActivityUtil.setToolbar(this, false);
        ActivityUtil.setToolbarTitle(this, "HCI Snoop log");

        mListView = findViewById(R.id.hci_log_listView);
        mAdapter = new HciPacketListAdapter(this, mViewModel.getSnoopPackets().getValue());
        mListView.setAdapter(mAdapter);
        HciSnoopLogUtil snoopLog = new HciSnoopLogUtil(this);

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
        mTypeSpinner.setAdapter(ArrayAdapter.createFromResource(this, R.array.HciTypeArray, android.R.layout.simple_spinner_item));
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = parent.getItemAtPosition(position).toString();
                mAdapter = new HciPacketListAdapter(HciLogActivity.this, mViewModel.getFilteredHciPackets(type));
                mListView.setAdapter(mAdapter);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
