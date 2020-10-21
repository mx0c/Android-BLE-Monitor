package com.huc.android_ble_monitor.models.AttProtocol;

import com.huc.android_ble_monitor.models.AttMethod;
import com.huc.android_ble_monitor.models.L2capPacket;
import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;

public class BaseAttPacket implements Comparable<BaseAttPacket>{
    public BaseAttPacket(L2capPacket p){
        Byte opcode = p.packet_data[0];
        this.packet_method = AttOpCodeMethod.getAttOpCodeMethod(opcode & 0x3f);
        this.packet_command_flag = ((opcode & 0x40) >> 6) == 1;
        this.packet_authentication_signature_flag = ((opcode & 0x80) >> 7) == 1;
        this.packet_length = p.packet_data.length;
        this.packet_type = resolvePacketType();
        this.packet_data = p.packet_data;
        this.l2capPacket = p;
    }

    public static int DEFAULT_MTU_SIZE = 23;
    public static int MTU_SIZE = DEFAULT_MTU_SIZE;
    public static String BLE_BASE_UUID_16_BIT_MNEMONIC = "0000xxxx-0000-1000-8000-00805F9B34FB";

    public L2capPacket l2capPacket;
    public boolean packet_authentication_signature_flag;
    public boolean packet_command_flag;
    public AttOpCodeMethod packet_method;
    public String packet_type;
    public int packet_length;
    public Byte[] packet_data;
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

    protected Byte[] decodeValue(Byte[] data, int from, int to){
        Byte[] temp = Arrays.copyOfRange(data, from, to);
        ArrayUtils.reverse(temp);
        return temp;
    }

    protected short decode16BitValue(byte LSB, byte MSB){
        return (short)((MSB << 8) | LSB);
    }

    public boolean compareAttMethodString(AttMethod method){
        if(this.packet_method.name().contains(method.getAttMethod())){
            if(Math.abs(this.packet_method.name().length() - method.getAttMethod().length()) <= 4 ){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString(){
        String res = "";
        res += "Packet Type: " + this.packet_method.name() + "\n";
        res += "Auth Signature flag: " + (this.packet_authentication_signature_flag ? "1" : "0") + "\n";
        res += "Command Flag: " + (this.packet_command_flag ? "1" : "0") + "\n";
        res += "Raw Data (0x): ";
        for(Byte b : this.packet_data){
            res += String.format("%02X ", b);
        }
        res += "\n";
        return res;
    }

    @Override
    public int compareTo(BaseAttPacket o) {
        return o.l2capPacket.timestamp.compareTo(this.l2capPacket.timestamp);
    }
}
