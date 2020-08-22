package com.huc.android_ble_monitor.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.SnoopPacket;
import java.text.SimpleDateFormat;
import java.util.List;

public class HciPacketListAdapter extends ArrayAdapter<SnoopPacket> {
    private TextView mPacket_num;
    private TextView mPacket_timestamp;
    private TextView mPacket_type;
    private TextView mPacket_info;
    private SimpleDateFormat mTimestampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public HciPacketListAdapter(@NonNull Context context, @NonNull List<SnoopPacket> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SnoopPacket packet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hci_logging_list_item, parent, false);
        }

        initializeViews(convertView);

        mPacket_type.setText(packet.packet_type);
        mPacket_timestamp.setText(mTimestampFormat.format(packet.timestamp));
        mPacket_num.setText(String.valueOf(packet.packet_number));
        mPacket_info.setText(packet.packet_info);

        return convertView;
    }

    private void initializeViews(View view) {
        mPacket_info = view.findViewById(R.id.packet_info);
        mPacket_num = view.findViewById(R.id.packet_num);
        mPacket_timestamp = view.findViewById(R.id.packet_timestamp);
        mPacket_type = view.findViewById(R.id.packet_type);
    }
}
