package com.huc.android_ble_monitor.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.huc.android_ble_monitor.models.SnoopPacket;
import java.util.ArrayList;

public class HciLogViewModel extends ViewModel {
    private MutableLiveData<ArrayList<SnoopPacket>> mSnoopPackets = new MutableLiveData<>();

    public void init(){
        mSnoopPackets.setValue(new ArrayList<SnoopPacket>());
    }

    public void addSnoopPacket(SnoopPacket packet){
        ArrayList list = mSnoopPackets.getValue();
        packet.packet_number = list.size() + 1;
        list.add(packet);
        mSnoopPackets.postValue(list);
    }

    public LiveData<ArrayList<SnoopPacket>> getSnoopPackets(){
        return mSnoopPackets;
    }
}
