package com.huc.android_ble_monitor.adapters;

import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.models.BleDevice;
import com.huc.android_ble_monitor.util.BLEPropertyToViewResolver;

import java.util.ArrayList;
import java.util.List;

public class ScanResultRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "BLEM_ScanResultRecyclerAdapt";

    private OnDeviceConnectListener mOnDeviceConnectListener;

    private List<BleDevice> mBleDevices;
    private Context mContext;
    private BLEPropertyToViewResolver blePropertyToViewResolver;

    public ScanResultRecyclerAdapter(Context context, List<BleDevice> bleDevices, OnDeviceConnectListener onDeviceConnectListener) {
        mBleDevices = bleDevices;
        mContext = context;
        mOnDeviceConnectListener = onDeviceConnectListener;
        blePropertyToViewResolver = new BLEPropertyToViewResolver(context);
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

        ((ViewHolder)holder).tvBonded.setText(blePropertyToViewResolver.bondStateTextResolver(scanResult));
        ((ViewHolder)holder).ivBondstate.setImageResource(blePropertyToViewResolver.bondStateImageResolver(scanResult));
        ((ViewHolder)holder).tvName.setText(blePropertyToViewResolver.deviceNameResolver(scanResult));
        ((ViewHolder)holder).tvAddress.setText(blePropertyToViewResolver.deviceAddressResolver(scanResult));
        ((ViewHolder)holder).tvRssi.setText(blePropertyToViewResolver.deviceRssiResolver(scanResult));
        ((ViewHolder)holder).tvCompanyIdentifier.setText(blePropertyToViewResolver.deviceManufacturerResolver(scanResult));
        ((ViewHolder)holder).tvConnectability.setText(blePropertyToViewResolver.deviceConnectabilityResolver(scanResult));

        ArrayList<String> uuids = blePropertyToViewResolver.deviceServiceResolver(mBleDevices.get(position), scanResult);
        ((ViewHolder)holder).tvServices.setText("Advertised Services (" + mBleDevices.get(position).getServiceCount() + ")");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.advertised_service_list_item, uuids);
        ((ViewHolder)holder).servicesListView.setAdapter(adapter);
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
}
