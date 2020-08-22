package com.huc.android_ble_monitor.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class SnoopPacket {
    public SnoopPacket(String snoopJson, String hciJson){
        parseSnoopJson(snoopJson);
        parseHciJson(hciJson);
    }

    private void parseSnoopJson(String json){
        try {
            JSONObject jsonSnoopFrame = new JSONObject(json);
            this.cumulative_drops = jsonSnoopFrame.getInt("cumulative_drops");
            this.included_length = jsonSnoopFrame.getInt("included_length");
            this.original_length = jsonSnoopFrame.getInt("original_length");

            JSONArray arr = jsonSnoopFrame.getJSONArray("packet_data");
            ArrayList<Integer> cList = new ArrayList(arr.length());
            for(int i = 0; i < arr.length();i++){
                cList.add((int)arr.get(i));
            }
            this.packet_data = cList;

            this.packet_destination = jsonSnoopFrame.getBoolean("packet_received") ? "PACKET_RECEIVED" : "PACKET_SENT";
            this.packet_sent = jsonSnoopFrame.getBoolean("packet_sent");
            this.packet_type_command_event = jsonSnoopFrame.getBoolean("packet_type_command_event");
            this.packet_type_data = jsonSnoopFrame.getBoolean("packet_type_data");
            this.timestamp = new Date(jsonSnoopFrame.getLong("timestamp_microseconds") / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseHciJson(String json){
        try {
            JSONObject jsonHciFrame = new JSONObject(json);
            this.packet_type = jsonHciFrame.getJSONObject("packet_type").getString("value").replace("HCI_TYPE_", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * packet sequential number
     */
    public int packet_number;

    /**
     * @brief
     *      length of original packet (could be more than this packet's length)
     */
    public int original_length;

    /**
     * @brief
     *      packet data field length
     */
    public int included_length;

    /**
     * @brief
     *      number of packet lost between the first record and this record for this file
     */
    public int cumulative_drops;

    /**
     * @brief
     *      unix timestamp for this packet record
     */
    public Date timestamp;

    /**
     * @brief
     *      packet data
     */
    public ArrayList<Integer> packet_data;

    /**
     * @brief
     *      define if packet record is sent
     * @return
     */
    public boolean packet_sent;

    /**
     * @brief
     *      define if packet record is Sent or Received
     * @return
     */
    public String packet_destination;

    /**
     * @brief
     *      define if packet record is data record
     * @return
     */
    public boolean packet_type_data;

    /**
     * @brief
     *      define if packet record is command or event
     * @return
     */
    public boolean packet_type_command_event;

    /**
     * @brief
     *      defines the type of the packet
     * @return
     */
    public String packet_type;
}
