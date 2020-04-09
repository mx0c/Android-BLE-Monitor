package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huc.android_ble_monitor.Models.BleDevice;

import java.util.List;

public class DeviceArrayAdapter extends ArrayAdapter<BleDevice> {
    public DeviceArrayAdapter(Context context, List<BleDevice> devices) {
        super(context, 0, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BleDevice dev = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.textView_name);
        TextView tvAddress = convertView.findViewById(R.id.textView_address);
        TextView tvBonded = convertView.findViewById(R.id.textView_bonded);
        TextView tvRssi = convertView.findViewById(R.id.textView_rssi);

        String name = dev.mDevice.getName();
        if(name == null){
            name = dev.getNameFromAdvPackets();
        }

        tvName.setText("Name: " + name);
        tvAddress.setText("Address: " + dev.mDevice.getAddress());
        tvBonded.setText("Bondstate: " + Helper.BondIntToString(dev.mDevice.getBondState()));
        tvRssi.setText("RSSI:" + dev.rssi);

        return convertView;
    }
}
