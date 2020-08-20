/************************************************************************************
 * The MIT License (MIT)                                                            *
 *                                                                                  *
 * Copyright (c) 2016 Bertrand Martel                                               *
 *                                                                                  * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy     * 
 * of this software and associated documentation files (the "Software"), to deal    * 
 * in the Software without restriction, including without limitation the rights     * 
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell        * 
 * copies of the Software, and to permit persons to whom the Software is            * 
 * furnished to do so, subject to the following conditions:                         * 
 *                                                                                  * 
 * The above copyright notice and this permission notice shall be included in       * 
 * all copies or substantial portions of the Software.                              * 
 *                                                                                  * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR       * 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,         * 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE      * 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER           * 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,    * 
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN        * 
 * THE SOFTWARE.                                                                    * 
 ************************************************************************************/
/**
	hci_event_packet.h
	HCI event stuctures for each supported HCI Event

	@author Bertrand Martel
	@version 1.0
*/
#ifndef HCIEVENTPACKET_H
#define HCIEVENTPACKET_H

#include <iostream>
#include "hci_decoder/IHciEventFrame.h"
#include "hci_decoder/IAdStructureFrame.h"
#include "advertising_packet.h"

/**********************************************************************/
/**********************************************************************/
/********************* HCI EVENTS** ***********************************/
/**********************************************************************/
/**********************************************************************/

/* HCI Event 0x3E : LE META Event*/
typedef struct le_meta_event{

	uint8_t command;                 /* 1B | */
	uint8_t parameter_length;        /* 1B | parameter length */
	uint8_t subevent_code;           /* 1B | Subevent code for LE Connection Complete event */

	le_meta_event(const std::vector<char> &data){
		this->command = HCI_EVENT_LE_META;
		this->parameter_length = data[EVENT_FRAME_OFFSET];
		this->subevent_code = data[EVENT_FRAME_OFFSET+1];
	}

} le_meta_event_t;

/**********************************************************************/
/**********************************************************************/
/********************* LE META SUBEVENT *******************************/
/**********************************************************************/
/**********************************************************************/

/*advertising report struct */
typedef struct {

	uint8_t event_type;              /* 1B | 0x00 Connectable undirected advertising (ADV_IND) | 0x01 Connectable directed advertising (ADV_DIRECT_IND) | 0x02 Scannable undirected advertising (ADV_SCAN_IND) | 0x03 Non connectable undirected advertising (ADV_NONCONN_IND) | 0x04 Scan Response (SCAN_RSP)*/
	uint8_t address_type;            /* 1B | 0x00 Public Device Address | 0x01 Random Device Address | 0x02 Public Identity Address (Corresponds to Resolved Private Address) | 0x03 Random (static) Identity Address (Corresponds to Resolved Private Address)*/
	uint8_t address[6];              /* 6B | Public Device Address, Random Device Address, Public Identity Address or Random (static) Identity Address of the advertising device*/
	uint8_t length_data;             /* 1B | Length of the Data[i] field for each device which responded */
	uint8_t rssi;                    /* 1B | Size: 1 Octet (signed integer) Range: -127 ≤ N ≤ +20 Units: dBm */
	uint8_t data[];                  /* XB | Length_Data[i] octets of advertising or scan response data */
}le_advertising_report;


//HCI Event 0x3E | subevent_code : 0x02 : LE Advertising Report Event
typedef struct {

	uint8_t command;                  /* 1B | */
	uint8_t subevent_code;            /* 1B | Subevent code for LE Advertising Report event */
	uint8_t num_reports;              /* 1B | Number of responses in event */
	le_advertising_report advertising_report[];

} le_advertising_report_event_t;

struct advertising_report {

	uint8_t event_type;                /* 1B | 0x00 : Connectable undirected advertising (ADV_IND)
													 0x01 : Connectable directed advertising (ADV_DIRECT_IND)
													 0x02 : Scannable undirected advertising (ADV_SCAN_IND)
													 0x03 : Non connectable undirected advertising (ADV_NONCONN_IND)
													 0x04 : Scan Response (SCAN_RSP) */

	uint8_t address_type;              /* 1B | 0x00 : Public Device Address 
													 0x01 : Random Device Address 
													 0x02 : Public Identity Address (Corresponds to Resolved Private Address)
													 0x03 : Random (static) Identity Address */

	bt_address address;                /* 6B | Public Device Address, Random Device Address, Public Identity
													 Address or Random (static) Identity Address of the advertising device*/

	uint8_t data_length;               /* 1B | 0x00-0x1F : Length of the Data[i] field for each device which responded */

	std::vector<IAdStructureFrame*> data;              /* XB | Length_Data[i] octets of advertising or scan response data formatted */

	uint8_t rssi;                      /* 1B | Range: -127 ≤ N ≤ +20; Units: dBm */

	void clear(){
		for (std::vector<IAdStructureFrame*>::iterator it = data.begin(); it != data.end();it++){
			delete (*it);
		}
		data.clear();
	}

	Json::Value toJson(){

		Json::Value output;
		output["event_type"] = event_type;
		output["address_type"] = address_type;
		output["address"] = address.toString();
		output["data_length"] = data_length;
		output["rssi"] = rssi;

		Json::Value data_array(Json::arrayValue);
		for (unsigned int i = 0; i  < data.size();i++){
			data_array.append(data[i]->toJsonObj());
		}
		output["data"] = data_array;

		return output;
	}

	std::string toJson(bool beautify){
		return convert_json_to_string(beautify,toJson());
	}
};

/* HCI LE Meta sub event 0x02 : LE_ADVERTISING_REPORT*/
typedef struct le_meta_advertising_report_event : public IHciEventFrame{

	uint8_t  num_reports;                              /* 1B | 0x01-0x19 : Number of responses in event */
	
	std::vector<advertising_report*> ad_report_items;  /* a list of advertising report*/

	void clear(){
		for (std::vector<advertising_report*>::iterator it = ad_report_items.begin(); it != ad_report_items.end();it++){
			(*it)->clear();
			delete (*it);
		}
		ad_report_items.clear();
	}

	le_meta_advertising_report_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_LE_META;
		this->subevent_code = HCI_EVENT_LE_ADVERTISING_REPORT;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->num_reports = data[EVENT_FRAME_OFFSET+2];
		unsigned int i = 0;
		int offset = EVENT_FRAME_OFFSET+3;

		for (i = 0 ;i  < this->num_reports;i++){

			advertising_report *report = new advertising_report();

			report->event_type = data[offset];
			report->address_type = data[offset + 1];

			bt_address addr;
			addr.address[0]=data[offset + 7];
			addr.address[1]=data[offset + 6];
			addr.address[2]=data[offset + 5];
			addr.address[3]=data[offset + 4];
			addr.address[4]=data[offset + 3];
			addr.address[5]=data[offset + 2];

			report->address = addr;

			report->data_length = data[offset + 8];

			std::vector<IAdStructureFrame*> data_val;

			int j = 0;
			char state=0;

			uint8_t packet_type = 0;
			std::vector<char> packet_data;
			int length=0;
			for (j=0;j < report->data_length;j++){
				if (state==0){
					length = data[offset + 9 + j];
					packet_data.clear();
					state=1;
				}
				else if (state==1){
					packet_type = data[offset + 9 + j];

					state=2;
					length--;
				}
				else{
					packet_data.push_back(data[offset + 9 + j]);
					length--;
					if (length==0){

						state = 0;
						IAdStructureFrame * frame = 0;

						switch (packet_type){
							case ADVERTIZING_TYPE_FLAGS:
							{
								frame = new advertising_report_flags_t(ADVERTIZING_TYPE_FLAGS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_INCOMPLETE_LIST_16BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_INCOMPLETE_LIST_16BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_COMPLETE_LIST_16BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_COMPLETE_LIST_16BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_INCOMPLETE_LIST_32BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_INCOMPLETE_LIST_32BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_COMPLETE_LIST_32BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_COMPLETE_LIST_32BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_INCOMPLETE_LIST_128BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_INCOMPLETE_LIST_128BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_COMPLETE_LIST_128BIT_SERVICE_CLASS_UUID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_COMPLETE_LIST_128BIT_SERVICE_CLASS_UUID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SHORTENED_LOCAL_NAME:
							{
								frame = new advertising_shortened_local_name_t(ADVERTIZING_TYPE_SHORTENED_LOCAL_NAME, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_COMPLETE_LOCAL_NAME:
							{
								frame = new advertising_complete_local_name_t(ADVERTIZING_TYPE_COMPLETE_LOCAL_NAME, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_TX_POWER_LEVEL:
							{
								frame = new advertising_tx_power_level_t(ADVERTIZING_TYPE_TX_POWER_LEVEL, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_CLASS_OF_DEVICE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_CLASS_OF_DEVICE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH_C:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH_C, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER_R:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER_R, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_DEVICE_ID:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_DEVICE_ID, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SECURITY_MANAGER_OUT_OF_BAND_FLAGS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SECURITY_MANAGER_OUT_OF_BAND_FLAGS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LIST_16BIT_SOLICITATION_UUIDS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LIST_16BIT_SOLICITATION_UUIDS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LIST_32BIT_SOLICITATION_UUIDS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LIST_32BIT_SOLICITATION_UUIDS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LIST_128BIT_SOLICITATION_UUIDS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LIST_128BIT_SOLICITATION_UUIDS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SERVICE_DATA_16BIT:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SERVICE_DATA_16BIT, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SERVICE_DATA_32BIT:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SERVICE_DATA_32BIT, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SERVICE_DATA_128BIT:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SERVICE_DATA_128BIT, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_CONFIRMATION_VALUE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_CONFIRMATION_VALUE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_RANDOM_VALUE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_RANDOM_VALUE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_PUBLIC_TARGET_ADDRESS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_PUBLIC_TARGET_ADDRESS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_RANDOM_TARGET_ADDRESS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_RANDOM_TARGET_ADDRESS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_APPEARANCE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_APPEARANCE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_ADVERTIZING_INTERVAL:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_ADVERTIZING_INTERVAL, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_LE_ROLE:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_LE_ROLE, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_INFORMATION_DATA_3D:
							{
								frame = new advertising_common_t(ADVERTIZING_TYPE_INFORMATION_DATA_3D, packet_data);
								break;
							}
							case ADVERTIZING_TYPE_MANUFACTURER_SPECIFIC_DATA:
							{
								frame = new advertising_manufacturer_specific_data_t(ADVERTIZING_TYPE_MANUFACTURER_SPECIFIC_DATA, packet_data);
								break;
							}
							default:
							{
								std::cout << "[ADVERTISING PACKET NOT DECODED] : " << unsigned(packet_type) << std::endl;
							}
						}

						if (frame!=0){
							data_val.push_back(frame);
						}
					}
				}
			}
			
			report->data = data_val;
			report->rssi = data[report->data_length + offset + 9];
			this->ad_report_items.push_back(report);
			offset+=report->data_length + 10;
		}
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		Json::Value event_code;
		event_code["code"] = HCI_EVENT_LE_ADVERTISING_REPORT;
		event_code["value"] = LE_SUBEVENT_STRING_ENUM.at(HCI_EVENT_LE_ADVERTISING_REPORT);
		output["subevent_code"] = event_code;

		init(output);
		parameters["num_reports"] = num_reports;

		Json::Value report_list(Json::arrayValue);
		unsigned int i = 0;
		for (i = 0; i  < ad_report_items.size();i++){
			report_list.append(ad_report_items[i]->toJson());
		}
		parameters["reports"] =  report_list;
		output["parameters"] = parameters;
		return output;
	}

} le_meta_advertising_report_event_t;


/* HCI Event 0x0F : Command status Event*/
typedef struct command_status  : public IHciEventFrame{

	uint8_t status;         /* 1B | 0x00 : Connection successfully completed, 0x01-0xFF : connection failure */
	uint8_t num_hci_packet; /*The Number of HCI command packets which are allowed to be sent to the Controller from the Host*/

	/*Opcode of the command which caused this event and is pending completion*/
	uint8_t ogf;
	uint8_t ocf;

	command_status(const std::vector<char> &data){
		this->event_code = HCI_EVENT_COMMAND_STATUS;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->status = data[EVENT_FRAME_OFFSET+1];
		this->num_hci_packet = data[EVENT_FRAME_OFFSET + 2];
		ogf = get_ogf(data[EVENT_FRAME_OFFSET + 4]);
		ocf = get_ocf(data[EVENT_FRAME_OFFSET + 4],data[EVENT_FRAME_OFFSET + 3]);
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["status"] =  status;
		parameters["num_hci_packet"] =  num_hci_packet;

		Json::Value command_opcode;

		Json::Value ogf_val;
		ogf_val["code"] = ogf;

		if (COMMAND_OGF_STRING_ENUM.count(ogf))
			ogf_val["value"] = COMMAND_OGF_STRING_ENUM.at(ogf);

		Json::Value ocf_val;
		ocf_val["code"] = ocf;

		switch (ogf)
		{
			case HCI_CMD_OGF_LINK_CONTROL_COMMANDS:
			{
				if (COMMAND_OCF_LINK_CONTROL_STRING_ENUM.count(ocf))
					ocf_val["value"] = COMMAND_OCF_LINK_CONTROL_STRING_ENUM.at(ocf);
				break;
			}
			case HCI_CMD_OGF_LINK_POLICY_COMMANDS:
			{
				if (COMMAND_OCF_LINK_POLICY_STRING_ENUM.count(ocf))
					ocf_val["value"] = COMMAND_OCF_LINK_POLICY_STRING_ENUM.at(ocf);
				break;
			}
			case HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS:
			{
				if (COMMAND_OCF_CTRL_BSB_STRING_ENUM.count(ocf))
					ocf_val["value"] = COMMAND_OCF_CTRL_BSB_STRING_ENUM.at(ocf);

				break;
			}
			case HCI_CMD_OGF_INFORMATIONAL_PARAMETERS:
			{
				if (COMMAND_OCF_INFORMATIONAL_STRING_ENUM.count(ocf))
					ocf_val["value"] = COMMAND_OCF_INFORMATIONAL_STRING_ENUM.at(ocf);

				break;
			}
			case HCI_CMD_OGF_STATUS_PARAMETERS:
			{
				break;
			}
			case HCI_CMD_OGF_TESTING_COMMANDS:
			{
				break;
			}
			case HCI_CMD_OGF_LE_CONTROLLER_COMMANDS:
			{
				if (COMMAND_OCF_LE_STRING_ENUM.count(ocf))
					ocf_val["value"] = COMMAND_OCF_LE_STRING_ENUM.at(ocf);
				
				break;
			}
			case HCI_CMD_OGF_VENDOR_SPECIFIC:
			{
				break;
			}
		}

		command_opcode["ogf"] = ogf_val;
		command_opcode["ocf"] = ocf_val;

		parameters["command_opcode"] =  command_opcode;
		output["parameters"] = parameters;
		return output;
	}

} command_status_t;

typedef struct completed_packet{

	uint16_t connection_handle;
	uint16_t num_of_completed_packet;

	completed_packet(uint16_t connection_handle,uint16_t num_of_completed_packet){
		this->connection_handle = connection_handle;
		this->num_of_completed_packet = num_of_completed_packet;
	}

	Json::Value toJson(){
		Json::Value output;
		output["connection_handle"] = connection_handle;
		output["num_of_completed_packet"] = num_of_completed_packet;
		return output;
	}

} completed_packet_t;

/* HCI Event 0x13 : Number of completed packet Event*/
typedef struct number_of_completed_packet_event  : public IHciEventFrame{

	uint8_t                         number_of_handles;             /* 1B | The number of Connection_Handles and Num_HCI_Data_Packets parameters pairs contained in this event */
	std::vector<completed_packet_t*> completed_packet_list;

	void clear(){
		for (std::vector<completed_packet_t*>::iterator it = completed_packet_list.begin(); it != completed_packet_list.end();it++){
			delete (*it);
		}
		completed_packet_list.clear();
	}

	number_of_completed_packet_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_NUMBER_OF_COMPLETED_PACKET;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->number_of_handles = data[EVENT_FRAME_OFFSET+1];

		int offset = 0;

		for (unsigned int i = 0; i  < number_of_handles;i++){
			completed_packet_list.push_back(new completed_packet_t(
				data[EVENT_FRAME_OFFSET + 2 + offset]+(data[EVENT_FRAME_OFFSET + 3 + offset]<<8),
				data[EVENT_FRAME_OFFSET + 4 + offset]+(data[EVENT_FRAME_OFFSET + 5 + offset]<<8)
				));
			offset+=4;
		}
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["number_of_handles"] =  number_of_handles;

		Json::Value completed_packet_arr(Json::arrayValue);

		for (unsigned int i = 0; i  < number_of_handles;i++){
			completed_packet_arr.append(completed_packet_list[i]->toJson());
		}
		parameters["completed_packet_list"] = completed_packet_arr;

		output["parameters"] = parameters;
		return output;
	}

} number_of_completed_packet_event_t;

/* HCI Event 0x07 : Remote name request Complete Event*/
typedef struct remote_name_request_complete_event  : public IHciEventFrame{

	uint8_t     status ;     /* 1B | 0x00 : Connection successfully completed, 0x01-0xFF : connection failure */
	bt_address  bd_addr;     /* BD_ADDR for the device whose name was requested.*/
	std::string remote_name; /* A UTF-8 encoded user-friendly descriptive name for the remote device.*/

	remote_name_request_complete_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_REMOTE_NAME_REQUEST_COMPLETE;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->status = data[EVENT_FRAME_OFFSET+1];

		bd_addr.address[0]=data[EVENT_FRAME_OFFSET + 7];
		bd_addr.address[1]=data[EVENT_FRAME_OFFSET + 6];
		bd_addr.address[2]=data[EVENT_FRAME_OFFSET + 5];
		bd_addr.address[3]=data[EVENT_FRAME_OFFSET + 4];
		bd_addr.address[4]=data[EVENT_FRAME_OFFSET + 3];
		bd_addr.address[5]=data[EVENT_FRAME_OFFSET + 2];

		bool found = false;
		for (unsigned int i = 0; i <248 && !found;i++){
			if (data[COMMAND_FRAME_OFFSET+8+i]!='\0'){
				remote_name+=data[COMMAND_FRAME_OFFSET+8+i];
			}
			else{
				found=true;
			}
		}
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["status"] =  status;
		parameters["bd_addr"] =  bd_addr.toString();
		parameters["remote_name"] = remote_name;
		output["parameters"] = parameters;
		return output;
	}

} remote_name_request_complete_event_t;

/* HCI Event 0x3D : Remote Host supported features Event*/
typedef struct remote_host_supported_features_notification_event  : public IHciEventFrame{

	bt_address  bd_addr;                          /* BD_ADDR for the device whose name was requested.*/
	std::vector<uint8_t> host_supported_features; /* Bit map of Host Supported Features page of LMP extended features.*/

	remote_host_supported_features_notification_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_REMOTE_HOST_SUPPORTED_FEATURES_NOTIFICATION;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];

		bd_addr.address[0]=data[EVENT_FRAME_OFFSET + 6];
		bd_addr.address[1]=data[EVENT_FRAME_OFFSET + 5];
		bd_addr.address[2]=data[EVENT_FRAME_OFFSET + 4];
		bd_addr.address[3]=data[EVENT_FRAME_OFFSET + 3];
		bd_addr.address[4]=data[EVENT_FRAME_OFFSET + 2];
		bd_addr.address[5]=data[EVENT_FRAME_OFFSET + 1];

		for (unsigned int i = 0;i < 8;i++){
			host_supported_features.push_back(data[COMMAND_FRAME_OFFSET+7+i]);
		}
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["bd_addr"] =  bd_addr.toString();

		Json::Value host_supported_features_val(Json::arrayValue);
		for (unsigned int i = 0;i < 8;i++){
			host_supported_features_val.append(host_supported_features[i]);
		}
		parameters["host_supported_features"] = host_supported_features_val;

		output["parameters"] = parameters;
		return output;
	}

} remote_host_supported_features_notification_event_t;

/* HCI Event 0x05 : Disconnection Complete Event*/
typedef struct disconnection_complete_event  : public IHciEventFrame{

	uint8_t  status;             /* 1B | 0x00 : Connection successfully completed, 0x01-0xFF : connection failure */
	uint16_t connection_handle;  /* 2B | Connection_Handle which was disconnected*/
	uint8_t  reason;             /* 1B | Reason for disconnection*/

	disconnection_complete_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_DISCONNECTION_COMPLETE;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->status = data[EVENT_FRAME_OFFSET+1];
		this->connection_handle = data[EVENT_FRAME_OFFSET+2] + (data[EVENT_FRAME_OFFSET+3]<<8);
		this->reason = data[EVENT_FRAME_OFFSET+4];
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["status"] =  status;
		parameters["connection_handle"] =  connection_handle;
		parameters["reason"] =  reason;
		output["parameters"] = parameters;
		return output;
	}

} disconnection_complete_event_t;

/* HCI Event 0x01 : Inquiry Complete Event*/
typedef struct inquiry_complete_event  : public IHciEventFrame{

	uint8_t status;         /* 1B | 0x00 : Connection successfully completed, 0x01-0xFF : connection failure */

	inquiry_complete_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_INQUIRY_COMPLETE;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->status = data[EVENT_FRAME_OFFSET+1];
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["status"] =  status;
		output["parameters"] = parameters;
		return output;
	}

} inquiry_complete_event_t;


/* HCI Event 0x2F : Extended Inquiry Event*/
typedef struct extended_inquiry_result_event : public IHciEventFrame{

	uint8_t              num_responses; /*Number of responses from the inquiry*/
	bt_address           bd_addr; /*BD_ADDR for the device that responded*/
	uint8_t              page_repetition_mode; /*0:R0 1:R1 2:R2*/
	uint32_t             class_of_device; /*Class of Device for the device that responded*/
	uint16_t             clock_offset;
	uint8_t              rssi;
	std::vector<uint8_t> extended_inquiry_response;

	extended_inquiry_result_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_EXTENDED_INQUIRY_RESULT;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->num_responses = data[EVENT_FRAME_OFFSET+1];
		bd_addr.address[0]=data[EVENT_FRAME_OFFSET + 7];
		bd_addr.address[1]=data[EVENT_FRAME_OFFSET + 6];
		bd_addr.address[2]=data[EVENT_FRAME_OFFSET + 5];
		bd_addr.address[3]=data[EVENT_FRAME_OFFSET + 4];
		bd_addr.address[4]=data[EVENT_FRAME_OFFSET + 3];
		bd_addr.address[5]=data[EVENT_FRAME_OFFSET + 2];
		page_repetition_mode = data[EVENT_FRAME_OFFSET + 8];
		//1 octet reserved here
		class_of_device = data[EVENT_FRAME_OFFSET + 10] + (data[EVENT_FRAME_OFFSET + 11 ] << 8) + (data[EVENT_FRAME_OFFSET + 12 ] << 16) ;
		clock_offset = data[EVENT_FRAME_OFFSET + 13] + (data[EVENT_FRAME_OFFSET + 14] << 8);
		rssi = data[EVENT_FRAME_OFFSET + 15];
		for (unsigned int i = 0 ; i< 240;i++){
			extended_inquiry_response.push_back(data[EVENT_FRAME_OFFSET + 16]);
		}
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value parameters;
		init(output);
		parameters["num_responses"] =  num_responses;
		parameters["bd_addr"] =  bd_addr.toString();
		parameters["page_repetition_mode"] =  page_repetition_mode;
		parameters["class_of_device"] =  class_of_device;
		parameters["clock_offset"] =  clock_offset;
		parameters["rssi"] =  rssi;

		Json::Value inquiry_response_array(Json::arrayValue);
		for (unsigned int i = 0 ; i< 240;i++){
			inquiry_response_array.append(extended_inquiry_response[i]);
		}
		parameters["extended_inquiry_response"] =  inquiry_response_array;

		output["parameters"] = parameters;
		return output;
	}

} extended_inquiry_result_event_t;

/* HCI LE Meta sub event 0x01 : LE_CONNECTION_COMPLETE*/
typedef struct le_meta_connection_complete_event : public IHciEventFrame {

	uint8_t    status;                  /* 1B | 0x00 : Connection successfully completed, 0x01-0xFF : connection failure */
	uint16_t   connection_handle;       /* 2B | Connection_Handle to be used to identify a connection between two
											 Bluetooth devices. The Connection_Handle is used as an identifier
											 for transmitting and receiving data*/
	uint8_t    role;                    /* 1B | 0x00 : Connection is master, 0x01 : Connection is slave */
	uint8_t    peer_address_type;       /* 1B | 0x00 : Peer is using a Public Device Address, 0x01 : Peer is using a Random Device Address */
	bt_address peer_address;            /* 6B | address */
	uint16_t   conn_interval;           /* 2B | Connection interval used on this connection */
	uint16_t   conn_latency;            /* 2B | Slave latency for the connection in number of connection events */
	uint16_t   supervision_timeout;     /* 2B | Connection supervision timeout */
	uint8_t    master_clock_accuracy;   /* 1B | */
	
	le_meta_connection_complete_event(const std::vector<char> &data){
		this->event_code = HCI_EVENT_LE_META;
		this->subevent_code = HCI_EVENT_LE_CONNECTION_COMPLETE;
		this->parameter_total_length = data[EVENT_FRAME_OFFSET];
		this->status = data[EVENT_FRAME_OFFSET+2];
		this->connection_handle = (data[EVENT_FRAME_OFFSET+3]<<8) + data[EVENT_FRAME_OFFSET+4];
		this->role = data[EVENT_FRAME_OFFSET+5];
		this->peer_address_type = data[EVENT_FRAME_OFFSET+6];

		this->peer_address.address[0] = data[EVENT_FRAME_OFFSET+12];
		this->peer_address.address[1] = data[EVENT_FRAME_OFFSET+11];
		this->peer_address.address[2] = data[EVENT_FRAME_OFFSET+10];
		this->peer_address.address[3] = data[EVENT_FRAME_OFFSET+9];
		this->peer_address.address[4] = data[EVENT_FRAME_OFFSET+8];
		this->peer_address.address[5] = data[EVENT_FRAME_OFFSET+7];

		this->conn_interval = (data[EVENT_FRAME_OFFSET+13]<<8) + data[EVENT_FRAME_OFFSET+14];
		this->conn_latency = (data[EVENT_FRAME_OFFSET+15]<<8) + data[EVENT_FRAME_OFFSET+16];
		this->supervision_timeout = (data[EVENT_FRAME_OFFSET+17]<<8) + data[EVENT_FRAME_OFFSET+18];
		this->master_clock_accuracy = data[EVENT_FRAME_OFFSET+19];
	}

	Json::Value toJsonObj(){

		Json::Value output;
		Json::Value event_code;
		Json::Value parameters;
		event_code["code"] = HCI_EVENT_LE_CONNECTION_COMPLETE;
		event_code["value"] = LE_SUBEVENT_STRING_ENUM.at(HCI_EVENT_LE_CONNECTION_COMPLETE);
		output["subevent_code"] = event_code;
		init(output);
		parameters["status"] =  status;
		parameters["connection_handle"] =  connection_handle;
		parameters["peer_address_type"] =  peer_address_type;
		parameters["peer_address"] =  peer_address.toString();
		parameters["conn_interval"] =  conn_interval;
		parameters["conn_latency"] =  conn_latency;
		parameters["supervision_timeout"] =  supervision_timeout;
		parameters["master_clock_accuracy"] =  master_clock_accuracy;
		output["parameters"] = parameters;
		return output;
	}

} le_meta_connection_complete_event_t;

#endif //HCIEVENTPACKET_H