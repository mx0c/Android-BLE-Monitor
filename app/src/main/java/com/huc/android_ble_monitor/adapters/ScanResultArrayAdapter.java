package com.huc.android_ble_monitor.adapters;

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

import com.huc.android_ble_monitor.util.BleUtility;
import com.huc.android_ble_monitor.util.DataIO;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BleDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScanResultArrayAdapter extends ArrayAdapter<BleDevice> {
    final int BOND_STATE_BONDING = R.drawable.round_bluetooth_searching_white_48;
    final int BOND_STATE_BONDED = R.drawable.round_bluetooth_connected_white_48;
    final int BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED = R.drawable.round_bluetooth_disabled_white_48;

    private Context mCtx;
    private HashMap<Integer, String> mManufacturerIdToStringMap;

    private TextView tvName;
    private TextView tvAddress;
    private TextView tvBonded;
    private TextView tvRssi;
    private TextView tvConnectability;
    private TextView tvCompanyIdentifier;
    private TextView tvServices;
    private ImageView ivBondstate;
    private ListView servicesListView;

    public ScanResultArrayAdapter(Context context, List<BleDevice> devices) {
        super(context, 0, devices);
        mCtx = context;
        mManufacturerIdToStringMap = DataIO.loadManufacturerIdToStringMap(context);
    }

    private void initializeViewIds(View convertView, ViewGroup parent) {
        tvName = convertView.findViewById(R.id.DeviceName_TextView);
        tvAddress = convertView.findViewById(R.id.DeviceUUID_TextView);
        tvBonded = convertView.findViewById(R.id.BondState_TextView);
        tvRssi = convertView.findViewById(R.id.RSSI_TextView);
        tvConnectability = convertView.findViewById(R.id.Connectability_TextView);
        tvCompanyIdentifier = convertView.findViewById(R.id.CompanyIdentifier_TextView);
        ivBondstate = convertView.findViewById(R.id.BondState_ImageView);
        tvServices = convertView.findViewById(R.id.Services_TextView);
        servicesListView = convertView.findViewById(R.id.serviceUUIDs_ListView);
    }

    private void bondStateResolver(ScanResult result) {
        int state = result.getDevice().getBondState();
        int id = 0;
        if (state == 11) {
            id = BOND_STATE_BONDING;
        } else if (state == 12) {
            id = BOND_STATE_BONDED;
        } else {
            id = BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED;
        }
        ivBondstate.setImageResource(id);
        tvBonded.setText(BleUtility.BondIntToString(state));
    }

    private void deviceNameResolver(ScanResult result) {
        String name = result.getScanRecord().getDeviceName();
        tvName.setText((name == null ? "unknown" : name));
    }

    private void deviceAddressResolver(ScanResult result) {
        tvAddress.setText(result.getDevice().getAddress());
    }

    private void deviceRssiResolver(ScanResult result) {
        tvRssi.setText(Integer.toString(result.getRssi()) + " dBm");
    }

    private void deviceManufacturerResolver(ScanResult result) {
        SparseArray<byte[]> manufacturerData = result.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        tvCompanyIdentifier.setText(mManufacturerIdToStringMap.get(manufacturerId) + "(" + manufacturerId + ")");
    }

    private void deviceConnectabilityResolver(ScanResult result) {
        //only possible with api level >= 26
        if (Build.VERSION.SDK_INT >= 26) {
            tvConnectability.setText("Connectable: " + Boolean.toString(result.isConnectable()));
        }else{
            tvConnectability.setVisibility(View.GONE);
        }
    }

    private void deviceServicesResolver(BleDevice item, ScanResult result) {
        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids();
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

        tvServices.setText(new StringBuilder().append("Services ").append("(").append(uuidStrings.size()).append(")").toString());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCtx, R.layout.advertised_service_list_item, uuidStrings);
        servicesListView.setAdapter(adapter);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        BleDevice item = getItem(position);
        ScanResult res = item.mScanResult;

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.device_list_item, parent, false);
        }

        this.initializeViewIds(convertView, parent);

        this.deviceNameResolver(res);
        this.deviceAddressResolver(res);
        this.bondStateResolver(res);
        this.deviceRssiResolver(res);
        this.deviceManufacturerResolver(res);
        this.deviceConnectabilityResolver(res);
        this.deviceServicesResolver(item, res);

        return convertView;
    }
}
