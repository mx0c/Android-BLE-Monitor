package com.huc.android_ble_monitor.models.AttProtocol;
import java.util.ArrayList;

public class BaseAttPacket {
    public BaseAttPacket(ArrayList<Byte> data, int number){
        this.packet_number = number;
        //decode opcode
        Byte opcode = data.get(0);
        this.packet_method = AttOpCodeMethod.getAttOpCodeMethod(opcode & 0x3f);
        this.packet_command_flag = ((opcode & 0x40) >> 6) == 1;
        this.packet_authentication_signature_flag = ((opcode & 0x80) >> 7) == 1;
        this.packet_length = data.size();
        this.packet_type = resolvePacketType();
        this.packet_data = data;
    }

    public static int DEFAULT_MTU_SIZE = 23;
    public static int MTU_SIZE = DEFAULT_MTU_SIZE;

    public boolean packet_authentication_signature_flag;
    public boolean packet_command_flag;
    public AttOpCodeMethod packet_method;
    public String packet_type;
    public int packet_length;
    public ArrayList<Byte> packet_data;
    public int packet_number;

    private String resolvePacketType(){
        String[] parts = this.packet_method.name().split("_");
        switch(parts[parts.length-1]){
            case "CMD":
                return "Command";
            case "REQ":
                return "Request";
            case "RSP":
                return "Response";
            case "NTF":
                return "Notification";
            case "IND":
                return "Indication";
            case "CFM":
                return "Confirmation";
            default:
                return "";
        }
    }
}
