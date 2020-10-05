package com.huc.android_ble_monitor.adapters.hciLogActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.HciPacket;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class HciPacketListAdapter extends ArrayAdapter<HciPacket> {
    private TextView mPacket_num;
    private TextView mPacket_timestamp;
    private TextView mPacket_type;
    private TextView mPacket_info;
    private TextView mPacket_len;
    private SimpleDateFormat mTimestampFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS");
    private Context ctx;

    public HciPacketListAdapter(@NonNull Context context, @NonNull List<HciPacket> objects) {
        super(context, 0, objects);
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final HciPacket packet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_hci_log_list_item, parent, false);
        }

        initializeViews(convertView);

        mPacket_type.setText(packet.packet_type);
        mPacket_timestamp.setText(mTimestampFormat.format(packet.timestamp));
        mPacket_num.setText(String.valueOf(packet.packet_number));
        mPacket_info.setText(packet.packet_info);
        mPacket_len.setText(packet.original_length + " Byte");

        //display raw hci json on item click
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                JSONObject jsonObj = new JSONObject(packet.packet_hci_json);
                new MaterialAlertDialogBuilder(ctx)
                        .setTitle("Raw JSON:")
                        .setMessage(jsonObj.toString(4))
                        .setNeutralButton("OK", null)
                        .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return convertView;
    }

    private void initializeViews(View view) {
        mPacket_info = view.findViewById(R.id.packet_info);
        mPacket_num = view.findViewById(R.id.packet_num);
        mPacket_timestamp = view.findViewById(R.id.packet_timestamp);
        mPacket_type = view.findViewById(R.id.packet_type);
        mPacket_len = view.findViewById(R.id.packet_length);
    }
}
