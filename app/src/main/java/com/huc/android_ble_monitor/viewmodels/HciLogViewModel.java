package com.huc.android_ble_monitor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.activities.HciLogActivity;
import com.huc.android_ble_monitor.adapters.AttPacketListAdapter;
import com.huc.android_ble_monitor.adapters.L2capPacketListAdapter;
import com.huc.android_ble_monitor.models.AttPacket;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.HciSnoopLogUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HciLogViewModel extends ViewModel {
    private MutableLiveData<ArrayList<HciPacket>> mHciPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<L2capPacket>> mL2capPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<AttPacket>> mAttPackets = new MutableLiveData<>();
    private boolean mProtocolsInitialized = false;

    public void init(){
        mHciPackets.setValue(new ArrayList<HciPacket>());
        mL2capPackets.setValue(new ArrayList<L2capPacket>());
        mAttPackets.setValue(new ArrayList<AttPacket>());
    }

    public void addSnoopPacket(HciPacket packet){
        ArrayList hciPackets = mHciPackets.getValue();
        packet.packet_number = hciPackets.size() + 1;
        hciPackets.add(packet);
        mHciPackets.postValue(hciPackets);

        //convert to L2CAP
        if(packet.packet_type.equals("ACL_DATA")){
            if(packet.packet_boundary_flag == HciPacket.boundary.COMPLETE_PACKET ||
                    packet.packet_boundary_flag == HciPacket.boundary.FIRST_PACKET_FLUSHABLE ||
                    packet.packet_boundary_flag == HciPacket.boundary.FIRST_PACKET_NON_FLUSHABLE){
                ArrayList L2capPackets = mL2capPackets.getValue();
                L2capPacket l2capPacket = new L2capPacket(packet, L2capPackets.size() + 1);
                L2capPackets.add(l2capPacket);
                mL2capPackets.postValue(L2capPackets);
                //convert to ATT
                ArrayList AttPackets = mAttPackets.getValue();
                AttPacket attPacket = new AttPacket(l2capPacket.packet_data, AttPackets.size() + 1);
                AttPackets.add(attPacket);
                mAttPackets.postValue(AttPackets);
            }else{
                //TODO: add continuing ACL packets to already existing L2CAP packets
            }
        }

    }

    public void changeProtocol(String protocol, HciLogActivity ctx){
        //disable type spinner when hci isn't selected
        if(!protocol.equals("HCI")){
            ctx.mTypeSpinner.setEnabled(false);
            ctx.mTypeSpinner.setClickable(false);
        }else {
            ctx.mTypeSpinner.setEnabled(true);
        }

        switch (protocol){
            case "HCI":
                ctx.mListView.setAdapter(ctx.mAdapter);
                break;
            case "ATT":
                ctx.mListView.setAdapter(new AttPacketListAdapter(ctx, mAttPackets.getValue()));
                break;
            case "L2CAP":
                ctx.mListView.setAdapter(new L2capPacketListAdapter(ctx, mL2capPackets.getValue()));
                break;
        }
    }

    public LiveData<ArrayList<HciPacket>> getSnoopPackets(){
        return mHciPackets;
    }

    public ArrayList<HciPacket> getFilteredHciPackets(String type){
        ArrayList<HciPacket> filteredList = new ArrayList<>();

        //return unfiltered HCI Packets
        if(type.equals("All")){
            return mHciPackets.getValue();
        }

        //filter packets by provided type
        for (HciPacket p: mHciPackets.getValue()) {
            if(p.packet_type.equals(type.toUpperCase())){
                filteredList.add(p);
            }
        }
        return filteredList;
    }
}
