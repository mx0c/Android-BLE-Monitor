package com.huc.android_ble_monitor;

import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.os.Build;
import android.os.ParcelUuid;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.huc.android_ble_monitor.Models.BleDevice;

import java.util.ArrayList;
import java.util.List;

public class ScanResultArrayAdapter extends ArrayAdapter<BleDevice> {
    private Context mCtx;

    public ScanResultArrayAdapter(Context context, List<BleDevice> devices) {
        super(context, 0, devices);
        mCtx = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BleDevice item = getItem(position);
        ScanResult res = item.mScanResult;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.DeviceName_TextView);
        TextView tvAddress = convertView.findViewById(R.id.DeviceUUID_TextView);
        TextView tvBonded = convertView.findViewById(R.id.BondState_TextView);
        TextView tvRssi = convertView.findViewById(R.id.RSSI_TextView);
        TextView tvConnectability = convertView.findViewById(R.id.Connectability_TextView);
        TextView tvCompanyIdentifier = convertView.findViewById(R.id.CompanyIdentifier_TextView);
        ImageView ivBondstate = convertView.findViewById(R.id.BondState_ImageView);

        String name = res.getScanRecord().getDeviceName();
        tvName.setText((name == null ? "unknown" : name));
        tvAddress.setText(res.getDevice().getAddress());

        int state = res.getDevice().getBondState();

        int id = 0;
        if (state == 11) {
            id = R.drawable.round_bluetooth_searching_white_48;
        } else if (state == 12) {
            id = R.drawable.round_bluetooth_connected_white_48;
        } else {
            id = R.drawable.round_bluetooth_disabled_white_48;
        }
        ivBondstate.setImageResource(id);
        tvBonded.setText(BleUtility.BondIntToString(state));

        tvRssi.setText(Integer.toString(res.getRssi()));


        SparseArray<byte[]> manufacturerData = res.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        tvCompanyIdentifier.setText("Resolved Name (" + manufacturerId + ")"); // ToDo Resolve Name

        //only possible with api level >= 26
        if (Build.VERSION.SDK_INT >= 26) {
            tvConnectability.setText("Connectable: " + Boolean.toString(res.isConnectable()));
        }else{
            tvConnectability.setText("NOT CONNECTABLE");
            //tvConnectability.setVisibility(View.GONE);
        }

        ListView servicesListView = convertView.findViewById(R.id.serviceUUIDs_ListView);
        List<ParcelUuid> uuids = res.getScanRecord().getServiceUuids();
        ArrayList<String> uuidStrings = new ArrayList<>();

        //add BleDevice services to listview
        if(item.mServices.size() > 0 && item != null){
            for (BluetoothGattService service: item.mServices) {
                uuidStrings.add(service.getUuid().toString());
            }
        }

        if(uuids != null) {
            for (ParcelUuid uuid : uuids) {
                uuidStrings.add(uuid.getUuid().toString());
            }
        }else if(uuidStrings.size() <= 0){
            uuidStrings.add("- No advertised services found");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCtx, R.layout.advertised_service_list_item, uuidStrings);
        servicesListView.setAdapter(adapter);

        return convertView;
    }
}
