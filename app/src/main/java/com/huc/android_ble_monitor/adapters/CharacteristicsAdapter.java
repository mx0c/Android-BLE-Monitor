package com.huc.android_ble_monitor.adapters;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;
import com.huc.android_ble_monitor.util.BleUtility;

import java.util.List;

public class CharacteristicsAdapter extends ArrayAdapter<BluetoothGattCharacteristic> {
    private Context mContext;
    private List<BluetoothGattCharacteristic> mCharacteristics;
    private BLEPropertyToViewResolver mBlePropertyToViewResolver;
    public CharacteristicsAdapter(@NonNull Context context, List<BluetoothGattCharacteristic> characteristics){
        super(context, 0, characteristics);
        mContext = context;
        mCharacteristics = characteristics;
        mBlePropertyToViewResolver = new BLEPropertyToViewResolver(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BluetoothGattCharacteristic characteristic = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.characteristic_list_item, parent, false);
        }

        TextView characteristicName = convertView.findViewById(R.id.tv_characteristic_item_name);
        TextView characteristicIdentifier = convertView.findViewById(R.id.tv_characteristic_item_identifier);
        TextView characteristicUuid = convertView.findViewById(R.id.tv_characteristic_item_uuid);

        characteristicName.setText(mBlePropertyToViewResolver.characteristicNameResolver(characteristic));
        characteristicIdentifier.setText(mBlePropertyToViewResolver.characteristicIdentifierResolver(characteristic));
        characteristicUuid.setText(characteristic.getUuid().toString());

        Button readBtn = convertView.findViewById(R.id.readBtn);
        Button writeBtn = convertView.findViewById(R.id.writeBtn);
        Button notifyBtn = convertView.findViewById(R.id.notifyBtn);

        readBtn.setClickable(BleUtility.isCharacteristicReadable(characteristic));
        writeBtn.setClickable(BleUtility.isCharacteristicWritable(characteristic));
        notifyBtn.setClickable(BleUtility.isCharacteristicNotifiable(characteristic));

        return convertView;
    }
}
