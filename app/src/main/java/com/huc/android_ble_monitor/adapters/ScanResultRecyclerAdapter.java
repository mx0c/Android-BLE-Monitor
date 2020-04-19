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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huc.android_ble_monitor.util.DataIO;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BleUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ScanResultRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BLEM_ScanResultRecyclerAdapt";

    final int BOND_STATE_BONDING = R.drawable.round_bluetooth_searching_white_48;
    final int BOND_STATE_BONDED = R.drawable.round_bluetooth_connected_white_48;
    final int BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED = R.drawable.round_bluetooth_disabled_white_48;

    private OnDeviceConnectListener mOnDeviceConnectListener;

    private List<BleDevice> mBleDevices = new ArrayList<>();
    private HashMap<Integer, String> mManufacturerIdToStringMap;
    private Context mContext;

    public ScanResultRecyclerAdapter(Context context, List<BleDevice> bleDevices, OnDeviceConnectListener onDeviceConnectListener) {
        mBleDevices = bleDevices;
        mContext = context;
        mManufacturerIdToStringMap = DataIO.loadManufacturerIdToStringMap(context);
        mOnDeviceConnectListener = onDeviceConnectListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view, mOnDeviceConnectListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ScanResult scanResult = mBleDevices.get(position).mScanResult;

        ((ViewHolder)holder).tvBonded.setText(this.bondStateTextResolver(scanResult));
        ((ViewHolder)holder).ivBondstate.setImageResource(this.bondStateImageResolver(scanResult));
        ((ViewHolder)holder).tvName.setText(this.deviceNameResolver(scanResult));
        ((ViewHolder)holder).tvAddress.setText(this.deviceAddressResolver(scanResult));
        ((ViewHolder)holder).tvRssi.setText(this.deviceRssiResolver(scanResult));
        ((ViewHolder)holder).tvCompanyIdentifier.setText(this.deviceManufacturerResolver(scanResult));
        ((ViewHolder)holder).tvConnectability.setText(this.deviceConnectabilityResolver(scanResult));
        ((ViewHolder)holder).tvServices.setText(this.deviceServiceResolver(mBleDevices.get(position), scanResult));
    }

    @Override
    public int getItemCount() {
        return mBleDevices.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvName;
        private TextView tvAddress;
        private TextView tvBonded;
        private TextView tvRssi;
        private TextView tvConnectability;
        private TextView tvCompanyIdentifier;
        private TextView tvServices;
        private ImageView ivBondstate;
        private ListView servicesListView;
        private OnDeviceConnectListener onDeviceConnectListener;

        public ViewHolder(@NonNull View itemView, OnDeviceConnectListener onDeviceConnectListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.DeviceName_TextView);
            tvAddress = itemView.findViewById(R.id.DeviceUUID_TextView);
            tvBonded = itemView.findViewById(R.id.BondState_TextView);
            tvRssi = itemView.findViewById(R.id.RSSI_TextView);
            tvConnectability = itemView.findViewById(R.id.Connectability_TextView);
            tvCompanyIdentifier = itemView.findViewById(R.id.CompanyIdentifier_TextView);
            ivBondstate = itemView.findViewById(R.id.BondState_ImageView);
            tvServices = itemView.findViewById(R.id.Services_TextView);
            servicesListView = itemView.findViewById(R.id.serviceUUIDs_ListView);
            this.onDeviceConnectListener = onDeviceConnectListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDeviceConnectListener.onDeviceClick(getAdapterPosition());
        }
    }

    public interface OnDeviceConnectListener {
        void onDeviceClick(int position);
    }

    private int bondStateImageResolver(ScanResult scanResult) {
        int state = scanResult.getDevice().getBondState();
        int id = 0;
        if (state == 11) {
            id = BOND_STATE_BONDING;
        } else if (state == 12) {
            id = BOND_STATE_BONDED;
        } else {
            id = BOND_STATE_NOT_CONNECTED_OR_RECOGNIZED;
        }

        return id;
    }

    private String bondStateTextResolver(ScanResult scanResult) {
        int state =  scanResult.getDevice().getBondState();

        return BleUtility.BondIntToString(state);
    }

    private String deviceNameResolver(ScanResult result) {
        String name = result.getScanRecord().getDeviceName();
        return name == null ? "unknown" : name;
    }

    private String deviceAddressResolver(ScanResult result) {
        return result.getDevice().getAddress();
    }

    private String deviceRssiResolver(ScanResult result) {
       return Integer.toString(result.getRssi()) + " dBm";
    }

    private String deviceManufacturerResolver(ScanResult result) {
        SparseArray<byte[]> manufacturerData = result.getScanRecord().getManufacturerSpecificData();
        int manufacturerId = 0;
        for(int i = 0; i < manufacturerData .size(); i++){
            manufacturerId = manufacturerData.keyAt(i);
        }
        return mManufacturerIdToStringMap.get(manufacturerId) + "(" + manufacturerId + ")";
    }

    private String deviceConnectabilityResolver(ScanResult result) {
        //only possible with api level >= 26
        String connectabilityText = "NOT CONNECTABLE";
        if (Build.VERSION.SDK_INT >= 26) {
            connectabilityText = "Connectable: " + Boolean.toString(result.isConnectable());
        }

        return connectabilityText;

        /*
        else{
            tvConnectability.setVisibility(View.GONE); // ToDo is this needed? Just display not connectable
        }
         */
    }

    private String deviceServiceResolver(BleDevice item, ScanResult result) {
        List<ParcelUuid> uuids = result.getScanRecord().getServiceUuids(); // ToDo Remove Logic to viewmodel
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

        return new StringBuilder().append("Services ").append("(").append(uuidStrings.size()).append(")").toString();

        /*
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mCtx, R.layout.advertised_service_list_item, uuidStrings);
        servicesListView.setAdapter(adapter);

         */
    }

}
