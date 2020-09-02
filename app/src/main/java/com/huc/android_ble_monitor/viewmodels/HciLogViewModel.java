package com.huc.android_ble_monitor.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.huc.android_ble_monitor.activities.HciLogActivity;
import com.huc.android_ble_monitor.adapters.AttPacketListAdapter;
import com.huc.android_ble_monitor.adapters.L2capPacketListAdapter;
import com.huc.android_ble_monitor.models.AttPacket;
import com.huc.android_ble_monitor.models.HciPacket;
import com.huc.android_ble_monitor.models.L2capPacket;
import com.huc.android_ble_monitor.util.HciSnoopLog;
import java.util.ArrayList;

public class HciLogViewModel extends ViewModel {
    private MutableLiveData<ArrayList<HciPacket>> mHciPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<L2capPacket>> mL2capPackets = new MutableLiveData<>();
    private MutableLiveData<ArrayList<AttPacket>> mAttPackets = new MutableLiveData<>();

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
                //re-decode L2CAP + ATT packets
                mAttPackets.postValue(HciSnoopLog.convertL2capToAtt(mL2capPackets.getValue()));
                ctx.mListView.setAdapter(new AttPacketListAdapter(ctx, mAttPackets.getValue()));
                break;
            case "L2CAP":
                //re-decode l2CAP packets
                mL2capPackets.postValue(HciSnoopLog.convertHciToL2cap(mHciPackets.getValue()));
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
