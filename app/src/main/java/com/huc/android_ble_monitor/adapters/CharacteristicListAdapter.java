package com.huc.android_ble_monitor.adapters;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.PropertyResolver;
import com.huc.android_ble_monitor.util.BleUtil;
import com.huc.android_ble_monitor.util.IdentifierXmlResolver;

import java.util.List;

public class CharacteristicListAdapter extends ArrayAdapter<BluetoothGattCharacteristic> {
    private Context mContext;
    private List<BluetoothGattCharacteristic> mCharacteristics;
    private PropertyResolver mPropertyResolver;
    private BluetoothLeService mService;

    public CharacteristicListAdapter(@NonNull Context context, List<BluetoothGattCharacteristic> characteristics, BluetoothLeService service){
        super(context, 0, characteristics);
        mContext = context;
        mCharacteristics = characteristics;
        mPropertyResolver = new PropertyResolver();
        mService = service;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final BluetoothGattCharacteristic characteristic = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_service_detail_characteristic_list_item, parent, false);
        }

        TextView characteristicName = convertView.findViewById(R.id.tv_characteristic_item_name);
        TextView characteristicIdentifier = convertView.findViewById(R.id.tv_characteristic_item_identifier);
        TextView characteristicUuid = convertView.findViewById(R.id.tv_characteristic_item_uuid);
        TextView characteristicIdentifierLink = convertView.findViewById(R.id.tv_identifier_link);

        characteristicUuid.setText(characteristic.getUuid().toString());
        characteristicName.setText(mPropertyResolver.characteristicNameResolver(characteristic));

        String characteristicIdString = mPropertyResolver.characteristicIdentifierResolver(characteristic);
        characteristicIdentifier.setText(characteristicIdString);

        if(!characteristicIdString.equals(PropertyResolver.SIG_UNKNOWN_CHARACTERISTIC_IDENTIFIER)) {
            characteristicIdentifierLink.setText(IdentifierXmlResolver.getCharacteristicXmlLink(characteristicIdString));
        }else{
            characteristicIdentifierLink.setVisibility(View.GONE);
        }

        Button readBtn = convertView.findViewById(R.id.readBtn);
        Button writeBtn = convertView.findViewById(R.id.writeBtn);
        Button notifyBtn = convertView.findViewById(R.id.notifyBtn);

        if(!BleUtil.isCharacteristicReadable(characteristic)){
            readBtn.setEnabled(false);
            readBtn.setAlpha(0.6f);
        }
        if(!BleUtil.isCharacteristicWritable(characteristic)){
            writeBtn.setEnabled(false);
            writeBtn.setAlpha(0.6f);
        }
        if(!BleUtil.isCharacteristicNotifiable(characteristic)){
            notifyBtn.setEnabled(false);
            notifyBtn.setAlpha(0.6f);
        }

        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.readCharacteristic(characteristic);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(mContext);
                new MaterialAlertDialogBuilder(mContext)
                        .setTitle("Write to Characteristic")
                        .setMessage("Enter value to write:")
                        .setView(et)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                characteristic.setValue(et.getText().toString());
                                mService.writeCharacteristic(characteristic);
                            }
                        })
                        .show();
            }
        });

        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mService.setCharacteristicNotification(characteristic, true);
            }
        });

        return convertView;
    }
}
