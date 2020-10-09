package com.huc.android_ble_monitor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.util.HciSnoopLogUtil;

public class SetSnoopFilePathDialog extends AlertDialog {
    final static String TAG = "BLEM_SoopFileDialog";
    private EditText mSnoopFileEditText;

    public SetSnoopFilePathDialog(Context context) {
        super(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_set_snoop_path, null);
        setView(dialoglayout);

        mSnoopFileEditText = (EditText) dialoglayout.findViewById(R.id.snoop_file_edit);
        mSnoopFileEditText.setText(HciSnoopLogUtil.BTSNOOP_PATH);
        mSnoopFileEditText.setSelection(mSnoopFileEditText.getText().length());
        setTitle("Select BTSnoop Log Path");

        setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // save selected path
                HciSnoopLogUtil.BTSNOOP_PATH = mSnoopFileEditText.getText().toString();
                Log.d(TAG, "onClick Positive Button: Saved BtSnoop Path " + HciSnoopLogUtil.BTSNOOP_PATH);
            }
        });

        setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", (DialogInterface.OnClickListener) null);
    }
}
