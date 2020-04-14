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
import android.widget.ListView;
import android.widget.TextView;

import com.huc.android_ble_monitor.Models.BleDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScanResultArrayAdapter extends ArrayAdapter<BleDevice> {
    private Context mCtx;
    private HashMap<Integer, String> mManufacturerIdToStringMap;

    public ScanResultArrayAdapter(Context context, List<BleDevice> devices) {
        super(context, 0, devices);
        mCtx = context;
        mManufacturerIdToStringMap = DataIO.loadManufacturerIdToStringMap(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BleDevice item = getItem(position);
        ScanResult res = item.mScanResult;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.textView_name);
        TextView tvAddress = convertView.findViewById(R.id.textView_address);
        TextView tvBonded = convertView.findViewById(R.id.textView_bonded);
        TextView tvRssi = convertView.findViewById(R.id.textView_rssi);
        TextView tvConnectable = convertView.findViewById(R.id.textView_conectability);
        TextView tvVendor = convertView.findViewById(R.id.textView_vendor);

        String name = res.getScanRecord().getDeviceName();
        tvName.setText("Name: " + (name == null ? "unknown" : name));
        tvAddress.setText("Address: " + res.getDevice().getAddress());
        tvBonded.setText("Bondstate: " + BleUtility.BondIntToString(res.getDevice().getBondState()));
        tvRssi.setText("RSSI: " + res.getRssi());

        SparseArray<byte[]> manufacturerData = res.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        tvVendor.setText("Vendor: " + mManufacturerIdToStringMap.get(manufacturerId));

        //only possible with api level >= 26
        if (Build.VERSION.SDK_INT >= 26) {
            tvConnectable.setText("Connectable: " + Boolean.toString(res.isConnectable()));
        }else{
            tvConnectable.setVisibility(View.GONE);
        }

        ListView servicesListView = convertView.findViewById(R.id.service_uuids_listView);
        List<ParcelUuid> uuids = res.getScanRecord().getServiceUuids();
        ArrayList<String> uuidStrings = new ArrayList<String>();

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
