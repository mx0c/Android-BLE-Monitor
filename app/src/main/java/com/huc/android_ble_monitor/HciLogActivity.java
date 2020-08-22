package com.huc.android_ble_monitor;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.huc.android_ble_monitor.adapters.HciPacketListAdapter;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.models.SnoopPacket;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.HciSnoopLog;
import com.huc.android_ble_monitor.util.IPacketReceptionCallback;
import com.huc.android_ble_monitor.viewmodels.HciLogViewModel;

import java.util.List;

public class HciLogActivity extends BaseActivity<HciLogViewModel> implements IPacketReceptionCallback {
    private static final String TAG = "BLEM_HciLogAct";
    private HciSnoopLog mSnoopLog;
    private ListView mListView;
    private HciPacketListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hci_logging_activity);
        ActivityUtil.setToolbar(this, false);
        ActivityUtil.setToolbarTitle(this, "HCI Snoop log");

        mListView = findViewById(R.id.hci_log_listView);
        mAdapter = new HciPacketListAdapter(this, mViewModel.getSnoopPackets().getValue());
        mListView.setAdapter(mAdapter);
        mSnoopLog = new HciSnoopLog(this);
    }

    @Override
    protected void onServiceBinded() {}

    @Override
    protected void initializeViewModel() {
        mViewModel = new ViewModelProvider(this).get(HciLogViewModel.class);
        mViewModel.init();

        mViewModel.getSnoopPackets().observe(this, new Observer<List<SnoopPacket>>() {
            @Override
            public void onChanged(List<SnoopPacket> packets) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onHciFrameReceived(String snoopFrame, String hciFrame) {
        this.mViewModel.addSnoopPacket(new SnoopPacket(snoopFrame, hciFrame));
    }

    @Override
    public void onFinishedPacketCount(int packetCount) {

    }

    @Override
    public void onError(int errorCode, String errorMessage) {
        Log.e(TAG, "onError: " + errorMessage + " code: " + errorCode);
    }
}
