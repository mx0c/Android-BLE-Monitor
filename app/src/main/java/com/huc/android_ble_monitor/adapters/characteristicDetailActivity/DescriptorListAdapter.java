package com.huc.android_ble_monitor.adapters.characteristicDetailActivity;

import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.PropertyResolver;

import java.util.List;

public class DescriptorListAdapter extends ArrayAdapter<BluetoothGattDescriptor> {
    private Context mContext;
    private List<BluetoothGattDescriptor> mDescriptors;
    private PropertyResolver mPropertyResolver;
    private BluetoothLeService mService;

    public DescriptorListAdapter(@NonNull Context context, List<BluetoothGattDescriptor> descriptors, BluetoothLeService service){
        super(context, 0, descriptors);
        mContext = context;
        mDescriptors = descriptors;
        mPropertyResolver = new PropertyResolver();
        mService = service;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BluetoothGattDescriptor descriptor = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_characteristic_detail_view_descriptor_list_item, parent, false);
        }

        TextView descriptorIdentifier = convertView.findViewById(R.id.tv_descriptor_identifier);
        TextView descriptorUUID = convertView.findViewById(R.id.tv_descriptor_uuid);
        TextView descriptorName = convertView.findViewById(R.id.tv_descriptor_name);


        descriptorIdentifier.setText(mPropertyResolver.descriptorIdentifierResolver(descriptor));
        descriptorUUID.setText(descriptor.getUuid().toString());
        descriptorName.setText(mPropertyResolver.descriptorNameResolver(descriptor));

        return convertView;
    }


}
