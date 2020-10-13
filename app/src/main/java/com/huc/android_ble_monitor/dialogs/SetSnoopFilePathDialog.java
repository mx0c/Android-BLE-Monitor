package com.huc.android_ble_monitor.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.util.HciSnoopLogUtil;

public class SetSnoopFilePathDialog {
    final static String TAG = "BLEM_SoopFileDialog";
    private EditText mSnoopFileEditText;
    private MaterialAlertDialogBuilder mBuilder;

    public SetSnoopFilePathDialog(Activity context, final HciSnoopLogUtil snoopLog) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_set_snoop_path, null);

        mSnoopFileEditText = (EditText) dialoglayout.findViewById(R.id.snoop_file_edit);
        mSnoopFileEditText.setText(HciSnoopLogUtil.BTSNOOP_PATH);
        mSnoopFileEditText.setSelection(mSnoopFileEditText.getText().length());

        mBuilder = new MaterialAlertDialogBuilder(context)
                .setView(dialoglayout)
                .setTitle("Select BTSnoop Log Path")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // save selected path
                        HciSnoopLogUtil.BTSNOOP_PATH = mSnoopFileEditText.getText().toString();
                        Log.d(TAG, "onClick Positive Button: Saved BtSnoop Path " + HciSnoopLogUtil.BTSNOOP_PATH);
                        // restart streaming
                        snoopLog.restartStreaming();
                    }
                })
                .setNegativeButton("CANCEL", (DialogInterface.OnClickListener) null);
    }

    public void show(){
        mBuilder.show();
    }
}
