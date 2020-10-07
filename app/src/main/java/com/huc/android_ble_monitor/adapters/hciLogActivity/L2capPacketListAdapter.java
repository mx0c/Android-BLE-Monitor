package com.huc.android_ble_monitor.adapters.hciLogActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.L2capPacket;

import java.text.SimpleDateFormat;
import java.util.List;

public class L2capPacketListAdapter extends ArrayAdapter<L2capPacket> {
    private TextView mPacket_num;
    private TextView mPacket_timestamp;
    private TextView mPacket_type;
    private TextView mPacket_info;
    private TextView mPacket_len;
    private SimpleDateFormat mTimestampFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS");
    private Context ctx;

    public L2capPacketListAdapter(@NonNull Context context, @NonNull List<L2capPacket> objects) {
        super(context, 0, objects);
        ctx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final L2capPacket packet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_hci_log_list_item, parent, false);
        }

        initializeViews(convertView);

        mPacket_type.setText(packet.packet_channel_id);
        mPacket_timestamp.setText(mTimestampFormat.format(packet.packet_hci_frames.get(0).timestamp));
        mPacket_num.setText(String.valueOf(packet.packet_number));

        String dataInHex = "";
        for (byte b : packet.packet_data) {
            dataInHex += String.format("%02X ", b);
        }
        mPacket_info.setText(dataInHex);

        mPacket_len.setText(String.valueOf(packet.packet_length) + " Byte");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { }
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
