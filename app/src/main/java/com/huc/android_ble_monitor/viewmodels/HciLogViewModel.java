package com.huc.android_ble_monitor.viewmodels;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.R;
import com.huc.android_ble_monitor.activities.HciLogActivity;
import com.huc.android_ble_monitor.adapters.hciLogActivity.AttPacketListAdapter;
import com.huc.android_ble_monitor.adapters.hciLogActivity.HciPacketListAdapter;
import com.huc.android_ble_monitor.adapters.hciLogActivity.L2capPacketListAdapter;
import com.huc.android_ble_monitor.models.AttMethod;
import com.huc.android_ble_monitor.models.AttProtocol.AttErrorRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttExchangeMtuReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttExchangeMtuRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttExecuteWriteReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttFindByTypeValueReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttFindByTypeValueRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttFindInformationReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttFindInformationRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttHandleValueInd;
import com.huc.android_ble_monitor.models.AttProtocol.AttHandleValueNtf;
import com.huc.android_ble_monitor.models.AttProtocol.AttPrepareWriteReqRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadBlobReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadBlobRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadByGroupTypeReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadByGroupTypeRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadByTypeReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadByTypeRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadMultipleReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadMultipleRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadMultipleVariableReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadMultipleVariableRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadReq;
import com.huc.android_ble_monitor.models.AttProtocol.AttReadRsp;
import com.huc.android_ble_monitor.models.AttProtocol.AttSignedWriteCmd;
import com.huc.android_ble_monitor.models.AttProtocol.AttWriteCmd;
import com.huc.android_ble_monitor.models.AttProtocol.AttWriteReq;
import com.huc.android_ble_monitor.models.AttProtocol.BaseAttPacket;
import com.huc.android_ble_monitor.models.AttType;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.models.HciType;
import com.huc.android_ble_monitor.models.L2capPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HciLogViewModel extends ViewModel {
    private MutableLiveData<ArrayList<HciPacket>> mHciPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<L2capPacket>> mL2capPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<BaseAttPacket>> mAttPackets = new MutableLiveData<>();

    private AttType mSelecetedAttType = AttType.ALL;
    private AttMethod mSelecetedAttMethod = AttMethod.ALL;

    public void init(){
        mHciPackets.setValue(new ArrayList<HciPacket>());
        mL2capPackets.setValue(new ArrayList<L2capPacket>());
        mAttPackets.setValue(new ArrayList<BaseAttPacket>());
    }

    public void addSnoopPacket(HciPacket packet){
        ArrayList hciPackets = mHciPackets.getValue();
        packet.packet_number = hciPackets.size() + 1;
        hciPackets.add(packet);
        Collections.sort(hciPackets);
        mHciPackets.postValue(hciPackets);

        //convert to L2CAP
        if(packet.packet_type.equals("ACL_DATA")){
            if(packet.packet_boundary_flag == HciPacket.boundary.COMPLETE_PACKET ||
                    packet.packet_boundary_flag == HciPacket.boundary.FIRST_PACKET_FLUSHABLE ||
                    packet.packet_boundary_flag == HciPacket.boundary.FIRST_PACKET_NON_FLUSHABLE){
                ArrayList L2capPackets = mL2capPackets.getValue();
                L2capPacket l2capPacket = new L2capPacket(packet, L2capPackets.size() + 1);
                L2capPackets.add(l2capPacket);
                Collections.sort(L2capPackets);
                mL2capPackets.postValue(L2capPackets);
                //convert to Base ATT Packet
                ArrayList AttPackets = mAttPackets.getValue();
                BaseAttPacket decodedAttPacket = new BaseAttPacket(l2capPacket);
                //convert to specific ATT Packet
                switch (decodedAttPacket.packet_method){
                    case ATT_ERROR_RSP: decodedAttPacket = new AttErrorRsp(l2capPacket); break;
                    case ATT_EXCHANGE_MTU_REQ: decodedAttPacket = new AttExchangeMtuReq(l2capPacket); break;
                    case ATT_EXCHANGE_MTU_RSP: decodedAttPacket = new AttExchangeMtuRsp(l2capPacket); break;
                    case ATT_FIND_INFORMATION_REQ: decodedAttPacket = new AttFindInformationReq(l2capPacket); break;
                    case ATT_FIND_INFORMATION_RSP: decodedAttPacket = new AttFindInformationRsp(l2capPacket); break;
                    case ATT_READ_RSP: decodedAttPacket = new AttReadRsp(l2capPacket); break;
                    case ATT_WRITE_REQ: decodedAttPacket = new AttWriteReq(l2capPacket); break;
                    case ATT_READ_REQ: decodedAttPacket = new AttReadReq(l2capPacket); break;
                    case ATT_READ_BY_TYPE_REQ: decodedAttPacket = new AttReadByTypeReq(l2capPacket); break;
                    case ATT_READ_BY_TYPE_RSP: decodedAttPacket = new AttReadByTypeRsp(l2capPacket); break;
                    case ATT_READ_BY_GROUP_TYPE_REQ: decodedAttPacket = new AttReadByGroupTypeReq(l2capPacket); break;
                    case ATT_READ_BY_GROUP_TYPE_RSP: decodedAttPacket = new AttReadByGroupTypeRsp(l2capPacket); break;
                    case ATT_FIND_BY_TYPE_VALUE_REQ: decodedAttPacket = new AttFindByTypeValueReq(l2capPacket); break;
                    case ATT_FIND_BY_TYPE_VALUE_RSP: decodedAttPacket = new AttFindByTypeValueRsp(l2capPacket); break;
                    case ATT_HANDLE_VALUE_IND: decodedAttPacket = new AttHandleValueInd(l2capPacket); break;
                    case ATT_HANDLE_VALUE_NTF: decodedAttPacket = new AttHandleValueNtf(l2capPacket); break;
                    case ATT_SIGNED_WRITE_CMD: decodedAttPacket = new AttSignedWriteCmd(l2capPacket); break;
                    case ATT_READ_BLOB_REQ: decodedAttPacket = new AttReadBlobReq(l2capPacket); break;
                    case ATT_READ_BLOB_RSP: decodedAttPacket = new AttReadBlobRsp(l2capPacket); break;
                    case ATT_READ_MULTIPLE_REQ: decodedAttPacket = new AttReadMultipleReq(l2capPacket); break;
                    case ATT_READ_MULTIPLE_RSP: decodedAttPacket = new AttReadMultipleRsp(l2capPacket); break;
                    case ATT_READ_MULTIPLE_VARIABLE_REQ: decodedAttPacket = new AttReadMultipleVariableReq(l2capPacket); break;
                    case ATT_READ_MULTIPLE_VARIABLE_RSP: decodedAttPacket = new AttReadMultipleVariableRsp(l2capPacket); break;
                    case ATT_WRITE_CMD: decodedAttPacket = new AttWriteCmd(l2capPacket); break;
                    case ATT_EXECUTE_WRITE_REQ: decodedAttPacket = new AttExecuteWriteReq(l2capPacket); break;
                    case ATT_PREPARE_WRITE_REQ: decodedAttPacket = new AttPrepareWriteReqRsp(l2capPacket); break;
                    case ATT_PREPARE_WRITE_RSP: decodedAttPacket = new AttPrepareWriteReqRsp(l2capPacket); break;
                }
                ArrayList tmp = mAttPackets.getValue();
                tmp.add(decodedAttPacket);
                // Sort by Timestamp
                Collections.sort(tmp);
                mAttPackets.postValue(tmp);
            }
        }

    }

    public void changeProtocol(String protocol, final HciLogActivity ctx){
        //disable type spinner when L2CAP is selected and change header
        if(protocol.equals("L2CAP")){
            ctx.mTypeSpinner.setEnabled(false);
            ctx.mTypeSpinner.setClickable(false);
            ctx.mTypeHeaderTextview.setText("Channel ID");
            ctx.mMethodHeaderTextview.setText("Data (0x)");
        }else {
            ctx.mTypeSpinner.setEnabled(true);
            ctx.mTypeHeaderTextview.setText("Type");
            ctx.mMethodHeaderTextview.setText("Method");
        }

        //disable method spinner and textview when ATT is not selected
        if(!protocol.equals("ATT")){
            ctx.mMethodTextView.setVisibility(View.GONE);
            ctx.mMethodSpinner.setVisibility(View.GONE);
        }else{
            ctx.mMethodSpinner.setVisibility(View.VISIBLE);
            ctx.mMethodTextView.setVisibility(View.VISIBLE);
        }

        switch (protocol){
            case "HCI":
                ctx.mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        HciType type = HciType.valueOf(parent.getItemAtPosition(position).toString().toUpperCase());
                        ctx.mAdapter = new HciPacketListAdapter(ctx, getFilteredHciPackets(type));
                        ctx.mListView.setAdapter(ctx.mAdapter);
                        ctx.mAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                ctx.mTypeSpinner.setAdapter(ArrayAdapter.createFromResource(ctx, R.array.HciTypeArray, android.R.layout.simple_spinner_item));
                ctx.mAdapter = new HciPacketListAdapter(ctx, mHciPackets.getValue());
                ctx.mListView.setAdapter(ctx.mAdapter);
                break;
            case "ATT":
                ctx.mTypeSpinner.setAdapter(ArrayAdapter.createFromResource(ctx, R.array.AttTypeArray, android.R.layout.simple_spinner_item));
                ctx.mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        AttType type = AttType.valueOf(parent.getItemAtPosition(position).toString().toUpperCase());
                        mSelecetedAttType = type;
                        ctx.mAdapter = new AttPacketListAdapter(ctx, getFilteredAttPackets(mSelecetedAttMethod, mSelecetedAttType));
                        ctx.mListView.setAdapter(ctx.mAdapter);
                        ctx.mAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                ctx.mAdapter = new AttPacketListAdapter(ctx, mAttPackets.getValue());
                ctx.mListView.setAdapter(ctx.mAdapter);
                ctx.mMethodSpinner.setAdapter(ArrayAdapter.createFromResource(ctx, R.array.AttMethodArray, android.R.layout.simple_spinner_item));
                ctx.mMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        AttMethod method = AttMethod.valueOf(parent.getItemAtPosition(position).toString());
                        mSelecetedAttMethod = method;
                        ctx.mAdapter = new AttPacketListAdapter(ctx, getFilteredAttPackets(mSelecetedAttMethod, mSelecetedAttType));
                        ctx.mListView.setAdapter(ctx.mAdapter);
                        ctx.mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });
                break;
            case "L2CAP":
                ctx.mAdapter = new L2capPacketListAdapter(ctx, mL2capPackets.getValue());
                ctx.mListView.setAdapter(ctx.mAdapter);
                break;
        }
    }

    public LiveData<ArrayList<HciPacket>> getSnoopPackets(){
        return mHciPackets;
    }

    public ArrayList<HciPacket> getFilteredHciPackets(HciType type){
        //return unfiltered HCI Packets
        if(type.equals(HciType.ALL)){
            return mHciPackets.getValue();
        }

        //filter packets by provided type
        ArrayList<HciPacket> filteredList = new ArrayList<>();
        for (HciPacket p: mHciPackets.getValue()) {
            if(p.packet_type.equals(type.getHciType().toUpperCase())){
                filteredList.add(p);
            }
        }
        return filteredList;
    }

    public ArrayList<BaseAttPacket> getFilteredAttPackets(AttMethod method, AttType type){
        ArrayList<BaseAttPacket> filteredList = new ArrayList<>();

        if(method == AttMethod.ALL && type == AttType.ALL)
            return mAttPackets.getValue();

        if(method == AttMethod.ALL) {
            //filter packets by provided method
            for (BaseAttPacket p : mAttPackets.getValue()) {
                if (p.packet_type == type.getAttType()) {
                    filteredList.add(p);
                }
            }
            return filteredList;
        } else if(type == AttType.ALL){
            for (BaseAttPacket p : mAttPackets.getValue()) {
                if (p.compareAttMethodString(method)) {
                    filteredList.add(p);
                }
            }
            return filteredList;
        }

        //filter packets by provided method and type
        for (BaseAttPacket p: mAttPackets.getValue()) {
            if(p.packet_type == type.getAttType() && p.compareAttMethodString(method)){
                filteredList.add(p);
            }
        }
        return filteredList;
    }

    public MutableLiveData<ArrayList<HciPacket>> getmHciPackets() {
        return mHciPackets;
    }

    public MutableLiveData<ArrayList<L2capPacket>> getmL2capPackets() {
        return mL2capPackets;
    }

    public MutableLiveData<ArrayList<BaseAttPacket>> getmAttPackets() {
        return mAttPackets;
    }
}
