package com.huc.android_ble_monitor.adapters;

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
import com.huc.android_ble_monitor.models.AttProtocol.BaseAttPacket;

import java.util.List;

public class AttPacketListAdapter extends ArrayAdapter<BaseAttPacket> {
    private TextView mPacket_num;
    private TextView mPacket_type;
    private TextView mPacket_info;
    private TextView mPacket_length;
    private Context mCtx;

    public AttPacketListAdapter(@NonNull Context context, @NonNull List<BaseAttPacket> objects) {
        super(context, 0, objects);
        mCtx = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BaseAttPacket packet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_hci_log_list_item, parent, false);
        }

        initializeViews(convertView);

        mPacket_type.setText(packet.packet_type);
        mPacket_num.setText(String.valueOf(packet.l2capPacket.packet_number));
        mPacket_info.setText(packet.packet_method.name());
        mPacket_length.setText(packet.packet_length + " Byte");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(mCtx)
                        .setTitle("Packet Informations:")
                        .setMessage(packet.toString())
                        .setNeutralButton("OK", null)
                        .show();
            }
        });
        return convertView;
    }

    private void initializeViews(View view) {
        mPacket_num = view.findViewById(R.id.packet_num);
        mPacket_info = view.findViewById(R.id.packet_info);
        mPacket_type = view.findViewById(R.id.packet_type);
        mPacket_length = view.findViewById(R.id.packet_length);
    }
}
