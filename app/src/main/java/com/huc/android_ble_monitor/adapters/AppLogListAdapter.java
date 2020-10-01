package com.huc.android_ble_monitor.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huc.android_ble_monitor.R;

import java.util.List;

public class AppLogListAdapter extends ArrayAdapter<Pair<String, String>> {
    private TextView mTimeTv;
    private TextView mLogTv;
    private Context mCtx;

    public AppLogListAdapter(@NonNull Context context, @NonNull List<Pair<String, String>> objects) {
        super(context, 0, objects);
        mCtx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Pair<String, String> p = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_application_log_item, parent, false);
        }

        initializeViews(convertView);

        mLogTv.setText(p.second);
        mTimeTv.setText(p.first);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    private void initializeViews(View view) {
        mTimeTv = view.findViewById(R.id.log_time);
        mLogTv = view.findViewById(R.id.log_text);
    }
}
