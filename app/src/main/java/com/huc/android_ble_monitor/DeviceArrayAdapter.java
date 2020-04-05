package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class DeviceArrayAdapter extends ArrayAdapter<BluetoothDevice> {
    public DeviceArrayAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, 0, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BluetoothDevice dev = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.textView_name);
        TextView tvAddress = convertView.findViewById(R.id.textView_address);
        TextView tvBonded = convertView.findViewById(R.id.textView_bonded);

        tvName.setText("Name: " + dev.getName());
        tvAddress.setText("Address: " + dev.getAddress());
        tvBonded.setText("Bondstate: " + Helper.BondIntToString(dev.getBondState()));

        return convertView;
    }
}
