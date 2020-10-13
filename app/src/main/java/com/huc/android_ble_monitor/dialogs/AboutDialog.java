package com.huc.android_ble_monitor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.huc.android_ble_monitor.R;

public class AboutDialog extends AlertDialog{
    public AboutDialog(Context context) {
        super(context);

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about, null);
        setView(dialogLayout);

        TextView copyright = dialogLayout.findViewById(R.id.who_made_it);
        TextView description = dialogLayout.findViewById(R.id.description);
        TextView github_link = dialogLayout.findViewById(R.id.github_link);

        copyright.setText("\u00a9 2019-2020 Marius Bauer, Giuseppe Ferrera");
        description.setText("This App was developed as part of a Master Project at the University Reutlingen, Germany. " +
                "It is supposed to support the process of using not standardized BLE Devices in a medical context.");
        github_link.setText("https://github.com/mx0c/Android-BLE-Monitor");

        setTitle("About this App");
        setButton(DialogInterface.BUTTON_NEUTRAL, "OK", (OnClickListener) null);
    }
}