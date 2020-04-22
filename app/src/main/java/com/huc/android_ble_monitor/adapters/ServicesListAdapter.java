package com.huc.android_ble_monitor.adapters;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;

import java.util.List;

public class ServicesListAdapter extends ArrayAdapter<BluetoothGattService> {

    BLEPropertyToViewResolver blePropertyToViewResolver;

    TextView tvServiceName;
    TextView tvServiceUuid;
    TextView tvServiceIdentifier;


    public ServicesListAdapter(@NonNull Context context, List<BluetoothGattService> bluetoothGattServices) {
        super(context, 0, bluetoothGattServices);

        blePropertyToViewResolver = new BLEPropertyToViewResolver(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BluetoothGattService bluetoothGattService = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.service_list_item, parent, false);
        }

        initializeViews(convertView);
        resolveViews(bluetoothGattService);

        return convertView;
    }

    private void initializeViews(View view) {
        tvServiceName = (TextView) view.findViewById(R.id.tv_service_item_name);
        tvServiceUuid = (TextView) view.findViewById(R.id.tv_service_item_uuid);
        tvServiceIdentifier = (TextView) view.findViewById(R.id.tv_service_item_identifier);
    }

    private void resolveViews(BluetoothGattService bluetoothGattService) {
        tvServiceName.setText(this.blePropertyToViewResolver.serviceNameResolver(bluetoothGattService));
        tvServiceUuid.setText(this.blePropertyToViewResolver.serviceUuidResolver(bluetoothGattService));
        tvServiceIdentifier.setText(this.blePropertyToViewResolver.serviceIdentifierResolver(bluetoothGattService));
    }
}
