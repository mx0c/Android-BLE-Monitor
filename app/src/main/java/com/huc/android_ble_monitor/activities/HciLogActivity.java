package com.huc.android_ble_monitor.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.BuildConfig;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.hciLogActivity.HciPacketListAdapter;
import com.huc.android_ble_monitor.dialogs.SetSnoopFilePathDialog;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.util.ActivityUtil;
import com.huc.android_ble_monitor.util.HciSnoopLogUtil;
import com.huc.android_ble_monitor.util.IPacketReceptionCallback;
import com.huc.android_ble_monitor.viewmodels.HciLogViewModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

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
    private HciSnoopLogUtil mSnoopLogUtil;

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
        mSnoopLogUtil = new HciSnoopLogUtil(this, HciLogActivity.this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_appbar_snooplog_act, menu);

        // workaround to make iconTints white (because layout iconTint doesnt work... )
        Drawable drawable = menu.findItem(R.id.share_snoop_log_button).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.primaryTextColor));
        menu.findItem(R.id.share_snoop_log_button).setIcon(drawable);

        drawable = menu.findItem(R.id.snoop_log_path_button).getIcon();
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.primaryTextColor));
        menu.findItem(R.id.snoop_log_path_button).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.snoop_log_path_button:
                SetSnoopFilePathDialog dialog = new SetSnoopFilePathDialog(HciLogActivity.this, mSnoopLogUtil);
                dialog.show();
                break;
            case R.id.share_snoop_log_button:
                final CharSequence[] items = new CharSequence[]{ "ATT Log", "L2CAP Log", "HCI Log", "Raw Snoop Log" };
                final boolean[] selected = new boolean[]{ false, false, false, false };
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Select Logs to share")
                        .setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selected[which] = isChecked;
                            }
                        })
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ArrayList<Uri> uris = new ArrayList<>();

                                if (selected[0]) {
                                    String fileName = "attPackets.json";
                                    Log.d(TAG, "Sharing following packets " +  fileName);
                                    uris.add(mSnoopLogUtil.getSharableUriForBlePackets(Objects.requireNonNull(getApplicationContext()), fileName, mViewModel.getmAttPackets().getValue()));
                                }

                                if (selected[1]) {
                                    String fileName = "l2capPackets.json";
                                    Log.d(TAG, "Sharing following packets " +  fileName);
                                    uris.add(mSnoopLogUtil.getSharableUriForBlePackets(Objects.requireNonNull(getApplicationContext()), fileName, mViewModel.getmL2capPackets().getValue()));
                                }

                                if (selected[2]) {
                                    String fileName = "hciPackets.json";
                                    Log.d(TAG, "Sharing following packets " +  fileName);
                                    uris.add(mSnoopLogUtil.getSharableUriForBlePackets(Objects.requireNonNull(getApplicationContext()), fileName, mViewModel.getmHciPackets().getValue()));
                                }

                                if (selected[3]) {
                                    Log.d(TAG, "Sharing following packets " +  HciSnoopLogUtil.BTSNOOP_PATH);
                                    uris.add(FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider",  new File(HciSnoopLogUtil.BTSNOOP_PATH)));
                                }

                                Intent share = new Intent(Intent.ACTION_SEND_MULTIPLE);
                                share.setType("text/plain");
                                share.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                                startActivity(Intent.createChooser(share, "Share packages"));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
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
