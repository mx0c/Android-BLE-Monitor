package com.huc.android_ble_monitor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.ListView;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.adapters.OpenSourceComponentAdapter;

public class OpenSourceComponentsDialog extends AlertDialog {
    public OpenSourceComponentsDialog(Context context) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        ListView listview = (ListView) inflater.inflate(R.layout.dialog_open_source_component_list, null);
        listview.setAdapter(new OpenSourceComponentAdapter(context));

        setView(listview);
        setTitle("Used Open Source Components");
        setButton(DialogInterface.BUTTON_NEUTRAL, "OK", (OnClickListener) null);
    }
}
