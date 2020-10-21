package com.huc.android_ble_monitor.adapters.serviceDetailActivity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.BleUtil;
import com.huc.android_ble_monitor.util.IdentifierXmlResolver;
import com.huc.android_ble_monitor.util.PropertyResolver;

import java.util.ArrayList;
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

        TextView characteristicName = convertView.findViewById(R.id.tv_service_item_name);
        TextView characteristicIdentifier = convertView.findViewById(R.id.tv_service_item_identifier);
        TextView characteristicUuid = convertView.findViewById(R.id.tv_service_item_uuid);

        characteristicUuid.setText(characteristic.getUuid().toString());
        characteristicName.setText(mPropertyResolver.characteristicNameResolver(characteristic));

        String characteristicIdString = mPropertyResolver.characteristicIdentifierResolver(characteristic);
        characteristicIdentifier.setText(characteristicIdString);

        this.handleLinkView(convertView, characteristicIdString);
        this.handleCharacteristicButtons(convertView, characteristic);
        this.handleDescriptorList(convertView, characteristic);

        return convertView;
    }


    void handleDescriptorList(View convertView, BluetoothGattCharacteristic characteristic) {
        ListView lv_descriptors = convertView.findViewById(R.id.lv_descriptors);
        TextView tv_descriptors = convertView.findViewById(R.id.tv_descriptors);
        List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();

        if(descriptorList.size() > 0) {
            List<Pair<String, String>> strDescriptorList = new ArrayList<>();
            for (BluetoothGattDescriptor descriptor :
                    descriptorList) {
                strDescriptorList.add(new Pair(mPropertyResolver.descriptorNameResolver(descriptor), String.valueOf(descriptor.getUuid())));
            }

            lv_descriptors.setAdapter(new DescriptorListAdapter(mContext, strDescriptorList));
        } else {
            tv_descriptors.setVisibility(View.GONE);
            lv_descriptors.setVisibility(View.GONE);
        }


    }

    void handleLinkView(final View convertView, String characteristicIdString) {
        ImageView characteristicIdentifierLink = convertView.findViewById(R.id.tv_service_identifier_link);

        if(!characteristicIdString.equals(PropertyResolver.SIG_UNKNOWN_CHARACTERISTIC_IDENTIFIER)) {
            characteristicIdentifierLink.setTag(IdentifierXmlResolver.getCharacteristicXmlLink(characteristicIdString));
        }else{
            characteristicIdentifierLink.setVisibility(View.GONE);
        }

        characteristicIdentifierLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(v.getTag().toString()));
                v.getContext().startActivity(intent);
            }
        });
    }

    void handleCharacteristicButtons(final View convertView, final BluetoothGattCharacteristic characteristic) {
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
    }
}
