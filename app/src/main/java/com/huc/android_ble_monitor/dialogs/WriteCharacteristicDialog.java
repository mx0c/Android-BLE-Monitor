package com.huc.android_ble_monitor.dialogs;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.services.BluetoothLeService;
import com.huc.android_ble_monitor.util.BinaryUtil;


public class WriteCharacteristicDialog {
    final static String TAG = "BLEM_WriteCharacDialog";
    public EditText mEditText;
    private MaterialAlertDialogBuilder mBuilder;
    private Spinner mSpinner;

    public WriteCharacteristicDialog(Activity context, final BluetoothGattCharacteristic characteristic, final BluetoothLeService service) {
        LayoutInflater inflater = context.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.dialog_write, null);

        mSpinner = dialoglayout.findViewById(R.id.encoding_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.encodings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinner.setAdapter(adapter);

        mEditText = (EditText) dialoglayout.findViewById(R.id.write_edit_text);
        mBuilder = new MaterialAlertDialogBuilder(context)
                .setView(dialoglayout)
                .setTitle("Write to Characteristic")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mSpinner.getSelectedItem().toString().equals("Hex"))
                            characteristic.setValue(BinaryUtil.hexStringToByteArray(mEditText.getText().toString()));
                        else
                            characteristic.setValue(mEditText.getText().toString().getBytes());
                        service.writeCharacteristic(characteristic);
                    }
                })
                .setNegativeButton("CANCEL", (DialogInterface.OnClickListener) null);
    }

    public void show(){
        mBuilder.show();
    }
}
