package com.huc.android_ble_monitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huc.android_ble_monitor.R;

public class OpenSourceComponentAdapter extends BaseAdapter {
    private static final String[][] COMPONENTS = new String[][]{
        {"btsnoop-decoder", "https://github.com/bertrandmartel/btsnoop-decoder"},
        {"bluetooth-hci-decoder", "https://github.com/bertrandmartel/bluetooth-hci-decoder"},
        {"rv-adapter-states", "https://github.com/rockerhieu/rv-adapter-states"},
        {"easypermissions", "https://github.com/googlesamples/easypermissions"}
    };

    private LayoutInflater mInflater;

    public OpenSourceComponentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return COMPONENTS.length;
    }

    @Override
    public Object getItem(int position) {
        return COMPONENTS[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.dialog_open_source_component_item, parent, false);
        }

        TextView title =  convertView.findViewById(R.id.title);
        TextView url = convertView.findViewById(R.id.url);

        title.setText(COMPONENTS[position][0]);
        url.setText(COMPONENTS[position][1]);

        return convertView;
    }
}
