package com.huc.android_ble_monitor.dialogs;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;

public class AboutDialog {
    private MaterialAlertDialogBuilder mBuilder;

    public AboutDialog(Activity context) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_about, null);

        TextView copyright = dialogLayout.findViewById(R.id.who_made_it);
        TextView description = dialogLayout.findViewById(R.id.description);
        TextView github_link = dialogLayout.findViewById(R.id.github_link);
        TextView version = dialogLayout.findViewById(R.id.version);

        copyright.setText("\u00a9 2019-2020 Marius Bauer, Giuseppe Ferrera");
        description.setText("This App was developed as part of a Master Project at the University Reutlingen, Germany. " +
                "It is supposed to support the process of using not standardized BLE Devices in a medical context.");
        github_link.setText("https://github.com/mx0c/Android-BLE-Monitor");

        String VersionString = "Version: ";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            VersionString += pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            VersionString += "Error";
        }
        version.setText(VersionString);

        mBuilder = new MaterialAlertDialogBuilder(context)
                .setView(dialogLayout)
                .setTitle("About this App")
                .setNeutralButton("OK", null);
    }

    public void show(){
        mBuilder.show();
    }
}