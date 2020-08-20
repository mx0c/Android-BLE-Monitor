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
	hci_cmd_packet.h
	HCI Command packet

	@author Bertrand Martel
	@version 1.0
*/
#ifndef HCICMDPACKET_H
#define HCICMDPACKET_H

#include "hci_decoder/IHciEventFrame.h"
#include "hci_decoder/IHciCommandFrame.h"
#include "hci_decoder/hci_global.h"
#include <json/json.h>
#include <iostream>

/*HCI command which does not decode any additionnal data*/
typedef struct void_cmd : public IHciCommandFrame {

	void_cmd(const std::vector<char> &data,COMMAND_OGF_ENUM ogf,uint8_t ocf){
		this->ogf = ogf;
		this->ocf = ocf;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		return output;
	};

} void_cmd_t;

/*HCI vendor specific frame*/
typedef struct vendor_specific_cmd : public IHciCommandFrame {

	std::vector<uint8_t> parameters;

	vendor_specific_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_VENDOR_SPECIFIC;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		for (unsigned int i = 0; i  < parameter_total_length;i++){
			this->parameters.push_back(data[COMMAND_FRAME_OFFSET+1+i]);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parametes_val;
		Json::Value data_val(Json::arrayValue);
		for (unsigned int i = 0; i  < parameters.size();i++){
			data_val.append(parameters[i]);
		}
		parametes_val["parameters_val"] = data_val;
		output["parameters"] = parametes_val;
		return output;
	};

} vendor_specific_cmd_t;

/*************************************************************************/
/*************************************************************************/
/*************** HCI LINK CONTROL COMMANDS *******************************/
/*************************************************************************/
/*************************************************************************/

/*HCI Command : OGF=0x01 | OCF=0x0006 Disconnect Command*/
typedef struct link_control_disconnect_cmd : public IHciCommandFrame {

	uint16_t connection_handle; /* 2B | Connection_Handle for the connection being disconnected*/
	uint8_t  reason; /* 1B | Authentication Failure error code (0x05), Other End Terminated Connec-
							tion error codes (0x13-0x15), Unsupported Remote Feature error code
							(0x1A), Pairing with Unit Key Not Supported error code (0x29) and Unac-
							ceptable Connection Parameters error code (0x3B)*/

	link_control_disconnect_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LINK_CONTROL_COMMANDS;
		this->ocf = HCI_CMD_OCF_LINK_CONTROL_DISCONNECT_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		connection_handle = data[COMMAND_FRAME_OFFSET + 1 ] + (data[COMMAND_FRAME_OFFSET+2]<<8);
		this->reason = data[COMMAND_FRAME_OFFSET +3];
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["connection_handle"] = connection_handle;
		parameters["reason"] = reason;
		output["parameters"] = parameters;

		return output;
	};

} link_control_disconnect_cmd_t;

/*HCI Command : OGF=0x01 | OCF=0x0019 Remote name request Command*/
typedef struct link_control_remote_name_request_cmd : public IHciCommandFrame {

	bt_address bd_addr;         /* 6B | BD_ADDR for the device whose name is requested. */
	uint8_t page_scan_repetition_mode; /* 1B : 0x00 R0 | 0x01 R1 | 0x02 R2 */
	uint8_t reserved;                  /* 1B : 0x00*/
	uint16_t clock_offset;             /* 2B : bt14-0 => Bits 16-2 of CLKNslave-CLK | bit 15 : Clock_Offset_Valid_Flag,Invalid Clock Offset = 0,Valid Clock Offset = 1*/

	link_control_remote_name_request_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LINK_CONTROL_COMMANDS;
		this->ocf = HCI_CMD_OCF_LINK_CONTROL_REMOTE_NAME_REQUEST_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		this->bd_addr.address[0]=data[COMMAND_FRAME_OFFSET+6];
		this->bd_addr.address[1]=data[COMMAND_FRAME_OFFSET+5];
		this->bd_addr.address[2]=data[COMMAND_FRAME_OFFSET+4];
		this->bd_addr.address[3]=data[COMMAND_FRAME_OFFSET+3];
		this->bd_addr.address[4]=data[COMMAND_FRAME_OFFSET+2];
		this->bd_addr.address[5]=data[COMMAND_FRAME_OFFSET+1];

		page_scan_repetition_mode=data[COMMAND_FRAME_OFFSET+7];
		reserved=data[COMMAND_FRAME_OFFSET+8];
		this->clock_offset = data[COMMAND_FRAME_OFFSET+9] + (data[COMMAND_FRAME_OFFSET+10]<<8);
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["bd_addr"] = bd_addr.toString();
		parameters["page_scan_repetition_mode"] = page_scan_repetition_mode;
		parameters["reserved"] = reserved;
		parameters["clock_offset"] = clock_offset;
		output["parameters"] = parameters;

		return output;
	};

} link_control_remote_name_request_cmd_t;

/*HCI Command : OGF=0x01 | OCF=0x001A Remote name request Cancel Command*/
typedef struct link_control_remote_name_request_cancel_cmd : public IHciCommandFrame {

	bt_address bd_addr;         /* 6B | BD_ADDR for the device whose name is requested. */
	
	link_control_remote_name_request_cancel_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LINK_CONTROL_COMMANDS;
		this->ocf = HCI_CMD_OCF_LINK_CONTROL_REMOTE_NAME_REQUEST_CANCEL_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		this->bd_addr.address[0]=data[COMMAND_FRAME_OFFSET+6];
		this->bd_addr.address[1]=data[COMMAND_FRAME_OFFSET+5];
		this->bd_addr.address[2]=data[COMMAND_FRAME_OFFSET+4];
		this->bd_addr.address[3]=data[COMMAND_FRAME_OFFSET+3];
		this->bd_addr.address[4]=data[COMMAND_FRAME_OFFSET+2];
		this->bd_addr.address[5]=data[COMMAND_FRAME_OFFSET+1];
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["bd_addr"] = bd_addr.toString();
		output["parameters"] = parameters;

		return output;
	};

} link_control_remote_name_request_cancel_cmd_t;

/*HCI Command : OGF=0x01 | OCF=0x0001 Inquiry Command*/
typedef struct link_control_inquiry_cmd : public IHciCommandFrame {

	uint32_t lap;
	uint8_t inquiry_length;
	uint8_t num_responses;

	link_control_inquiry_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LINK_CONTROL_COMMANDS;
		this->ocf = HCI_CMD_OCF_LINK_CONTROL_INQUIRY_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		lap = data[COMMAND_FRAME_OFFSET + 1 ];
		inquiry_length = data[COMMAND_FRAME_OFFSET + 2 ];
		num_responses = data[COMMAND_FRAME_OFFSET + 3 ];
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["lap"] = lap;
		parameters["inquiry_length"] = inquiry_length;
		parameters["num_responses"] = num_responses;
		output["parameters"] = parameters;

		return output;
	};

} link_control_inquiry_cmd_t;

/*************************************************************************/
/*************************************************************************/
/********************* HCI LE COMMANDS ***********************************/
/*************************************************************************/
/*************************************************************************/

/*HCI Command : OGF=0x08 | OCF=0x0016 LE Read remote used features Command*/
typedef struct le_read_remote_used_features_cmd : public IHciCommandFrame {

	uint8_t connection_handle;              /* 2B | Connection_Handle to be used to identify a connection */

	le_read_remote_used_features_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_READ_REMOTE_USED_FEATURES_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		this->connection_handle = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["connection_handle"] = connection_handle;
		output["parameters"] = parameters;

		return output;
	}

} le_read_remote_used_features_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x000D LE Create connection Command*/
typedef struct le_create_connection_cmd : public IHciCommandFrame {

	uint16_t   scan_interval;
	uint16_t   scan_window;
	uint8_t    initiator_filter_policy;
	uint8_t    peer_address_type;
	bt_address peer_address;
	uint8_t    own_address_type;
	uint16_t   conn_interval_min;
	uint16_t   conn_interval_max;
	uint16_t   conn_latency;
	uint16_t   supervision_timeout;
	uint16_t   minimum_ce_length;
	uint16_t   maximum_ce_length;

	le_create_connection_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_CREATE_CONNECTION_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		this->scan_interval = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
		this->scan_window = data[COMMAND_FRAME_OFFSET+3] + (data[COMMAND_FRAME_OFFSET+4]<<8);
		this->initiator_filter_policy = data[COMMAND_FRAME_OFFSET+5];
		this->peer_address_type = data[COMMAND_FRAME_OFFSET+6];
		this->peer_address.address[0]=data[COMMAND_FRAME_OFFSET+12];
		this->peer_address.address[1]=data[COMMAND_FRAME_OFFSET+11];
		this->peer_address.address[2]=data[COMMAND_FRAME_OFFSET+10];
		this->peer_address.address[3]=data[COMMAND_FRAME_OFFSET+9];
		this->peer_address.address[4]=data[COMMAND_FRAME_OFFSET+8];
		this->peer_address.address[5]=data[COMMAND_FRAME_OFFSET+7];
		this->own_address_type = data[COMMAND_FRAME_OFFSET+13];
		this->conn_interval_min = data[COMMAND_FRAME_OFFSET+14] + (data[COMMAND_FRAME_OFFSET+15]<<8);
		this->conn_interval_max = data[COMMAND_FRAME_OFFSET+15] + (data[COMMAND_FRAME_OFFSET+16]<<8);
		this->conn_latency = data[COMMAND_FRAME_OFFSET+17] + (data[COMMAND_FRAME_OFFSET+18]<<8);
		this->supervision_timeout = data[COMMAND_FRAME_OFFSET+19] + (data[COMMAND_FRAME_OFFSET+20]<<8);
		this->minimum_ce_length = data[COMMAND_FRAME_OFFSET+21] + (data[COMMAND_FRAME_OFFSET+22]<<8);
		this->maximum_ce_length = data[COMMAND_FRAME_OFFSET+23] + (data[COMMAND_FRAME_OFFSET+24]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["scan_interval"] = scan_interval;
		parameters["scan_window"] = scan_window;
		parameters["initiator_filter_policy"] = initiator_filter_policy;
		parameters["peer_address_type"] = peer_address_type;
		parameters["peer_address"] = peer_address.toString();
		parameters["own_address_type"] = own_address_type;
		parameters["conn_interval_min"] = conn_interval_min;
		parameters["conn_interval_max"] = conn_interval_max;
		parameters["conn_latency"] = conn_latency;
		parameters["supervision_timeout"] = supervision_timeout;
		parameters["minimum_ce_length"] = minimum_ce_length;
		parameters["maximum_ce_length"] = maximum_ce_length;
		output["parameters"] = parameters;

		return output;
	}

} le_create_connection_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x000B LE Set Scan Parameters Command*/
typedef struct le_set_scan_parameters_cmd : public IHciCommandFrame {

	uint8_t le_scan_type;              /* 1B | 0x00 Passive Scanning. No SCAN_REQ packets shall be sent | 0x01 Active scanning. SCAN_REQ packets may be sent */
	uint16_t le_scan_interval;         /* 2B | This is defined as the time interval from when the Controller started its last LE scan until it begins the subsequent LE scan*/
	uint16_t le_scan_window;           /* 2B | The duration of the LE scan. LE_Scan_Window shall be less than or equal to LE_Scan_Interval*/
	uint8_t own_address_type;          /* 1B | */
	uint8_t scanning_filter_policy;    /* 1B | */

	le_set_scan_parameters_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_SCAN_PARAMETERS_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		this->le_scan_type = data[COMMAND_FRAME_OFFSET+1];
		this->le_scan_interval = (data[COMMAND_FRAME_OFFSET+2]<<8) + data[COMMAND_FRAME_OFFSET+3];
		this->le_scan_window = (data[COMMAND_FRAME_OFFSET+4]<<8) + data[COMMAND_FRAME_OFFSET+5];
		this->own_address_type = data[COMMAND_FRAME_OFFSET+6];
		this->scanning_filter_policy = data[COMMAND_FRAME_OFFSET+7];
	}


	Json::Value toJsonObj(){

		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["le_scan_type"] = le_scan_type;
		parameters["le_scan_interval"] = le_scan_interval;
		parameters["le_scan_window"] = le_scan_window;
		parameters["own_address_type"] = own_address_type;
		parameters["scanning_filter_policy"] = scanning_filter_policy;
		output["parameters"] = parameters;

		return output;
	}

} le_set_scan_parameters_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0001 LE Set Event Mask Command*/
typedef struct le_set_event_mask_cmd : public IHciCommandFrame {

	std::vector<uint8_t> event_mask; /*The Set_Event_Mask command is used to control which events are generated by the HCI for the Host*/

	le_set_event_mask_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_EVENT_MASK_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		for (unsigned int i = 0; i  < 8;i++){
			event_mask.push_back(data[COMMAND_FRAME_OFFSET+1+i]);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		Json::Value mask_vals(Json::arrayValue);
		for (unsigned int i = 0; i  < 8;i++){
			mask_vals.append(event_mask[i]);
		}
		parameters["event_mask"] = mask_vals;
		output["parameters"] = parameters;
		return output;
	}

} le_set_event_mask_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0008 LE Set Random Address Command*/
typedef struct le_set_random_address_cmd : public IHciCommandFrame {

	bt_address random_address; /* 6B | Random Device Address */

	le_set_random_address_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_RANDOM_ADDRESS_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		random_address.address[0]=data[COMMAND_FRAME_OFFSET+6];
		random_address.address[1]=data[COMMAND_FRAME_OFFSET+5];
		random_address.address[2]=data[COMMAND_FRAME_OFFSET+4];
		random_address.address[3]=data[COMMAND_FRAME_OFFSET+3];
		random_address.address[4]=data[COMMAND_FRAME_OFFSET+2];
		random_address.address[5]=data[COMMAND_FRAME_OFFSET+1];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["random_address"] = random_address.toString();
		output["parameters"] = parameters;
		return output;
	}

} le_set_random_address_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x001C LE Remove device from resolving list Command*/
typedef struct le_remove_device_from_resolving_list_cmd : public IHciCommandFrame {

	bt_address peer_address;                 /* 6B | Public or Random (static) Identity Address of the peer device */
	uint8_t    peer_identity_address_type;   /* 1B | 0x00: Public Identity Address | 0x01 : Random (static) Identity Address*/

	le_remove_device_from_resolving_list_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_REMOVE_DEVICE_FROM_RESOLVING_LIST_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		peer_identity_address_type = data[COMMAND_FRAME_OFFSET+1];

		peer_address.address[0]=data[COMMAND_FRAME_OFFSET+7];
		peer_address.address[1]=data[COMMAND_FRAME_OFFSET+6];
		peer_address.address[2]=data[COMMAND_FRAME_OFFSET+5];
		peer_address.address[3]=data[COMMAND_FRAME_OFFSET+4];
		peer_address.address[4]=data[COMMAND_FRAME_OFFSET+3];
		peer_address.address[5]=data[COMMAND_FRAME_OFFSET+2];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["peer_identity_address_type"] = peer_identity_address_type;
		parameters["peer_address"] = peer_address.toString();
		output["parameters"] = parameters;
		return output;
	}

} le_remove_device_from_resolving_list_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x000C LE Set Scan Enable Command*/
typedef struct le_set_scan_enable_cmd : public IHciCommandFrame {

	uint8_t  le_scan_enable;            /* 1B | 0x00 : Scanning disabled | 0x01 : Scanning enabled */
	uint8_t  filter_duplicates;         /* 1B | 0x00 : Duplicate filtering disabled | 0x01 : Duplicate filtering enabled */

	le_set_scan_enable_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_SCAN_ENABLE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		this->le_scan_enable = data[COMMAND_FRAME_OFFSET+1];
		this->filter_duplicates = data[COMMAND_FRAME_OFFSET+2];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["le_scan_enable"] = le_scan_enable;
		parameters["filter_duplicates"] = filter_duplicates;
		output["parameters"] = parameters;
		return output;
	}

} le_set_scan_enable_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0008 LE Set advertising data Command*/
typedef struct le_set_advertising_data_cmd : public IHciCommandFrame {

	uint8_t                     advertising_data_length; /* 1B : The number of significant octets in the Advertising_Data*/
	std::vector<unsigned char>  advertising_data;        /* 31 bits*/

	le_set_advertising_data_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_ADVERTISING_DATA_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		advertising_data_length = data[COMMAND_FRAME_OFFSET+1];
		for (unsigned int i = 0; i  < advertising_data_length;i++){
			advertising_data.push_back(data[COMMAND_FRAME_OFFSET + 2 + i]);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["advertising_data_length"] = advertising_data_length;

		Json::Value data(Json::arrayValue);
		for (unsigned int i = 0; i < advertising_data_length;i++){
			data.append(advertising_data[i]);
		}
		parameters["advertising_data"] = data;

		output["parameters"] = parameters;
		return output;
	}

} le_set_advertising_data_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0006 LE Set Advertising Parameters Command*/
typedef struct le_set_advertising_parameters_cmd : public IHciCommandFrame {

	uint16_t advertising_interval_min;  /* 2B | Minimum advertising interval for undirected and low duty cycle directed advertising */
	uint16_t advertising_interval_max;  /* 2B | Maximum advertising interval for undirected and low duty cycle directed advertising */
	uint8_t  advertising_type;          /* 1B | */
	uint8_t  own_address_type;          /* 1B | */
	uint8_t  peer_address_type;         /* 1B | */
	bt_address peer_address;            /* 6B | Public Device Address, Random Device Address, Public Identity Address, or Random (static) Identity Address of the device to be connected*/
	uint8_t  advertising_channel_map;   /* 1B | */
	uint8_t  advertising_filter_policy; /* 1B | */

	le_set_advertising_parameters_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LE_CONTROLLER_COMMANDS;
		this->ocf = HCI_CMD_OCF_LE_SET_ADVERTISING_PARAMETERS_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		this->advertising_interval_min = (data[COMMAND_FRAME_OFFSET+1]<<8) + data[COMMAND_FRAME_OFFSET+2];
		this->advertising_interval_max = (data[COMMAND_FRAME_OFFSET+3]<<8) + data[COMMAND_FRAME_OFFSET+4];
		this->advertising_type = data[COMMAND_FRAME_OFFSET+5];
		this->own_address_type = data[COMMAND_FRAME_OFFSET+6];
		this->peer_address_type = data[COMMAND_FRAME_OFFSET+7];
		peer_address.address[0]=data[COMMAND_FRAME_OFFSET+13];
		peer_address.address[1]=data[COMMAND_FRAME_OFFSET+12];
		peer_address.address[2]=data[COMMAND_FRAME_OFFSET+11];
		peer_address.address[3]=data[COMMAND_FRAME_OFFSET+10];
		peer_address.address[4]=data[COMMAND_FRAME_OFFSET+9];
		peer_address.address[5]=data[COMMAND_FRAME_OFFSET+8];
		this->advertising_channel_map = data[COMMAND_FRAME_OFFSET+14];
		this->advertising_filter_policy = data[COMMAND_FRAME_OFFSET+15];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value parameters;
		parameters["advertising_interval_min"] = advertising_interval_min;
		parameters["advertising_interval_max"] = advertising_interval_max;
		parameters["advertising_type"] = advertising_type;
		parameters["own_address_type"] = own_address_type;
		parameters["peer_address_type"] = peer_address_type;
		parameters["peer_address"] = peer_address.toString();
		parameters["advertising_channel_map"] = advertising_channel_map;
		parameters["advertising_filter_policy"] = advertising_filter_policy;
		output["parameters"] = parameters;

		return output;
	}

} le_set_advertising_parameters_cmd_t;

/*************************************************************************/
/*************************************************************************/
/********************* HCI LINK POLICY COMMANDS **************************/
/*************************************************************************/
/*************************************************************************/

/*HCI Command : OGF=0x02 | OCF=0x000F Write default link policy settings Command*/
typedef struct write_default_link_policy_settings_cmd : public IHciCommandFrame {

	uint16_t default_link_policy_settings; /*2B : 0x0000 : Disable All LM Modes Default | 0x0001 : Enable Role Switch | 0x0002 : Enable Hold Mode | 0x0004 : Enable Sniff Mode | 0x0008: Enable Park State*/

	write_default_link_policy_settings_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_LINK_POLICY_COMMANDS;
		this->ocf = HCI_CMD_OCF_LINK_POLICY_WRITE_DEFAULT_LINK_POLICY_SETTINGS_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		default_link_policy_settings = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["default_link_policy_settings"] = default_link_policy_settings;
		output["parameters"] = parameters;
		return output;
	}

} write_default_link_policy_settings_cmd_t;

/*************************************************************************/
/*************************************************************************/
/********************* HCI INFORMATIONAL COMMANDS ************************/
/*************************************************************************/
/*************************************************************************/

/*HCI Command : OGF=0x04 | OCF=0x0004 Read local extended features Command*/
typedef struct informational_read_local_extended_features_cmd : public IHciCommandFrame {

	uint8_t page_number; /*0x00: Requests the normal LMP features as returned by Read_Local_Supported_Features | 0x01-0xFF:Return the corresponding page of features*/

	informational_read_local_extended_features_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_INFORMATIONAL_PARAMETERS;
		this->ocf = HCI_CMD_OCF_INFORMATIONAL_READ_LOCAL_EXTENDED_FEATURES_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		page_number = data[COMMAND_FRAME_OFFSET];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["page_number"] = page_number;
		output["parameters"] = parameters;
		return output;
	}

} informational_read_local_extended_features_cmd_t;

/*************************************************************************/
/*************************************************************************/
/********************* HCI CONTROL BASEBAND COMMANDS *********************/
/*************************************************************************/
/*************************************************************************/

/*HCI Command : OGF=0x03 | OCF=0x0013 Write local name Command*/
typedef struct ctrl_bsb_write_local_name_cmd : public IHciCommandFrame {

	std::string local_name; /*max 248 bit null terminated */

	ctrl_bsb_write_local_name_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_LOCAL_NAME_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		local_name="";
		bool found = false;
		for (unsigned int i = 0; i <248 && !found;i++){
			if (data[COMMAND_FRAME_OFFSET+1+i]!='\0'){
				local_name+=data[COMMAND_FRAME_OFFSET+1+i];
			}
			else{
				found=true;
			}
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["local_name"] = local_name;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_local_name_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0005 Set event filter Command*/
typedef struct ctrl_bsb_set_event_filter_cmd : public IHciCommandFrame {

	uint8_t filter_type;
	uint8_t inquiry_result_filter_condition_type;
	//uint8_t connection_setup_filter_condition_type;

	ctrl_bsb_set_event_filter_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_LOCAL_NAME_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];

		filter_type = data[COMMAND_FRAME_OFFSET + 1];
		inquiry_result_filter_condition_type = data[COMMAND_FRAME_OFFSET + 2];
		//connection_setup_filter_condition_type = data[COMMAND_FRAME_OFFSET + 3];
		
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["filter_type"] = filter_type;
		parameters["inquiry_result_filter_condition_type"] = inquiry_result_filter_condition_type;
		//parameters["connection_setup_filter_condition_type"] = connection_setup_filter_condition_type;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_set_event_filter_cmd_t;


/*HCI Command : OGF=0x03 | OCF=0x0023 Read Class of Device Command*/
typedef struct ctrl_bsb_read_class_of_device_cmd : public IHciCommandFrame {

	ctrl_bsb_read_class_of_device_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_READ_CLASS_OF_DEVICE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		return output;
	}

} ctrl_bsb_read_class_of_device_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0024 Write Class of Device Command*/
typedef struct ctrl_bsb_write_class_of_device_cmd : public IHciCommandFrame {

	uint32_t device_class; /*3B : Class of Device for the device*/

	ctrl_bsb_write_class_of_device_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_CLASS_OF_DEVICE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		device_class = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8) + (data[COMMAND_FRAME_OFFSET+3]<<16);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["device_class"] = device_class;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_class_of_device_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0045 Write inquiry mode Command*/
typedef struct ctrl_bsb_write_inquiry_mode_cmd : public IHciCommandFrame {

	uint8_t inquiry_mode; /*1B : 0x00:Standard Inquiry Result event format | 0x01: Inquiry Result format with RSSI | 0x02: Inquiry Result with RSSI format or Extended Inquiry Result format*/

	ctrl_bsb_write_inquiry_mode_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_MODE_COMMAND;
		inquiry_mode = data[COMMAND_FRAME_OFFSET];
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["inquiry_mode"] = inquiry_mode;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_inquiry_mode_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x006D Write LE host support Command*/
typedef struct ctrl_bsb_write_le_host_support_cmd : public IHciCommandFrame {

	uint8_t le_supported_host; /*1B*/
	uint8_t simultaneous_le_host; /*1B*/

	ctrl_bsb_write_le_host_support_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_LE_HOST_SUPPORT_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		le_supported_host = data[COMMAND_FRAME_OFFSET+1];
		simultaneous_le_host = data[COMMAND_FRAME_OFFSET+2];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["le_supported_host"] = le_supported_host;
		parameters["simultaneous_le_host"] = simultaneous_le_host;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_le_host_support_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x001E Write Inquiry Scan activity Command*/
typedef struct ctrl_bsb_write_inquiry_scan_activity_cmd : public IHciCommandFrame {

	uint16_t inquiry_scan_interval;
	uint16_t inquiry_scan_window;

	ctrl_bsb_write_inquiry_scan_activity_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_ACTIVITY_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		inquiry_scan_interval = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
		inquiry_scan_window =data[COMMAND_FRAME_OFFSET+3] + (data[COMMAND_FRAME_OFFSET+4]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["inquiry_scan_interval"] = inquiry_scan_interval;
		parameters["inquiry_scan_window"] = inquiry_scan_window;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_inquiry_scan_activity_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0026 Write voice settings Command*/
typedef struct ctrl_bsb_write_voice_settings_cmd : public IHciCommandFrame {

	uint16_t voice_settings; /*2B : 10 Bits meaningful*/

	ctrl_bsb_write_voice_settings_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_VOICE_SETTING_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		voice_settings = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["voice_settings"] = voice_settings;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_voice_settings_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x003A Write Inquiry Access Code (IAC) LAP (lower address part) Command*/
typedef struct ctrl_bsb_write_iac_lap_cmd : public IHciCommandFrame {

	uint8_t num_current_iac; /*Specifies the number of IACs (Inquiry Access Codes) which are currently in use by the local BR/
								EDR Controller to simultaneously listen for during an Inquiry Scan*/

	std::vector<uint32_t> lap_iac; /*LAP(s) used to create IAC which is currently in use by the local BR/EDR
									Controller to simultaneously listen for during an Inquiry Scan*/

	ctrl_bsb_write_iac_lap_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_CURRENT_IAC_LAP_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		num_current_iac = data[COMMAND_FRAME_OFFSET+1];
		for (unsigned int i = 0 ; i< num_current_iac;i++){
			uint32_t lap = data[COMMAND_FRAME_OFFSET+2+i] + (data[COMMAND_FRAME_OFFSET+3+i]<<8) + (data[COMMAND_FRAME_OFFSET+4+i]<<16);
			lap_iac.push_back(lap);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["num_current_iac"] = num_current_iac;
		Json::Value laps(Json::arrayValue);
		for (unsigned int i = 0; i  < num_current_iac;i++){
			laps.append(lap_iac[i]);
		}
		parameters["lap_iac"] = laps;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_iac_lap_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0047 Write page scan type Command*/
typedef struct ctrl_bsb_write_page_scan_type_cmd : public IHciCommandFrame {

	uint8_t page_scan_type; /*1B : 0x00:Mandatory: Standard Scan (default) | 0x01: Optional: Interlaced Scan*/

	ctrl_bsb_write_page_scan_type_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_SCAN_TYPE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		page_scan_type = data[COMMAND_FRAME_OFFSET+1];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["page_scan_type"] = page_scan_type;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_page_scan_type_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0056 Write simple pairing mode Command*/
typedef struct ctrl_bsb_write_simple_pairing_mode_cmd : public IHciCommandFrame {

	uint8_t simple_pairing_mode; /*1B : 0x00:Simple Pairing disabled | 0x01:Simple pairing enabled*/

	ctrl_bsb_write_simple_pairing_mode_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_SIMPLE_PAIRING_MODE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		simple_pairing_mode = data[COMMAND_FRAME_OFFSET+1];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["simple_pairing_mode"] = simple_pairing_mode;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_simple_pairing_mode_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0052 Write extended inquiry response Command*/
typedef struct ctrl_bsb_write_extended_inquiry_response_cmd : public IHciCommandFrame {

	uint8_t              fec_required; /*1B : 0x00:FEC (Forward Error correction) is not required | 0x01:FEC is required*/
	std::vector<uint8_t> extended_inquiry_response;

	ctrl_bsb_write_extended_inquiry_response_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_EXTENDED_INQUIRY_RESPONSE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		fec_required = data[COMMAND_FRAME_OFFSET+1];
		for (unsigned int i = 0;i < 240;i++){
			extended_inquiry_response.push_back(data[COMMAND_FRAME_OFFSET+2+i]);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["fec_required"] = fec_required;
		Json::Value extended_inquiry_response_val(Json::arrayValue);
		for (unsigned int i = 0;i < 240;i++){
			extended_inquiry_response_val.append(extended_inquiry_response[i]);
		}
		parameters["extended_inquiry_response"] = extended_inquiry_response_val;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_extended_inquiry_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x001A Write scan enable Command*/
typedef struct ctrl_bsb_write_scan_enable_cmd : public IHciCommandFrame {

	uint8_t scan_enable; /*1B : 0x00:No Scans enabled | 0x01: Inquiry Scan enabled/Page Scan disabled | 0x02: Inquiry Scan disabled/Page Scan enabled | 0x03:Inquiry Scan enabled/Page Scan enabled*/

	ctrl_bsb_write_scan_enable_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_SCAN_ENABLE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		scan_enable = data[COMMAND_FRAME_OFFSET+1];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["scan_enable"] = scan_enable;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_scan_enable_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0043 Write inquiry scan type Command*/
typedef struct ctrl_bsb_write_inquiry_scan_type_cmd : public IHciCommandFrame {

	uint8_t scan_type; /*1B : 0x00:Standard Scan | 0x01: Interlaced Scan*/

	ctrl_bsb_write_inquiry_scan_type_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_TYPE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		scan_type = data[COMMAND_FRAME_OFFSET+1];
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["scan_type"] = scan_type;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_inquiry_scan_type_cmd_t;


/*HCI Command : OGF=0x03 | OCF=0x0001 set event mask Command*/
typedef struct ctrl_bsb_set_event_mask_cmd : public IHciCommandFrame {

	std::vector<uint8_t> event_mask; /*The Set_Event_Mask command is used to control which events are generated by the HCI for the Host*/

	ctrl_bsb_set_event_mask_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_SET_EVENT_MASK_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		for (unsigned int i = 0; i  < 8;i++){
			event_mask.push_back(data[COMMAND_FRAME_OFFSET+1+i]);
		}
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		Json::Value mask_vals(Json::arrayValue);
		for (unsigned int i = 0; i  < 8;i++){
			mask_vals.append(event_mask[i]);
		}
		parameters["event_mask"] = mask_vals;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_set_event_mask_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0018 Write page timeout Command*/
typedef struct ctrl_bsb_write_page_timeout_cmd : public IHciCommandFrame {

	uint16_t page_timeout; /* 0:Illegal Page Timeout. Must be larger than 0 otherwise Page Timeout measured in Number of Baseband slots*/

	ctrl_bsb_write_page_timeout_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_TIMEOUT_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		page_timeout = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["page_timeout"] = page_timeout;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_write_page_timeout_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0033 Host Buffer size Command*/
typedef struct ctrl_bsb_host_buffer_size_cmd : public IHciCommandFrame {

	uint16_t host_acl_data_packet_length; /*Maximum length (in octets) of the data portion of each HCI ACL Data packet that the Host is able to accept*/
	uint8_t  host_synchronous_data_packet_length; /*Maximum length (in octets) of the data portion of each HCI synchronous Data Packet that the Host is able to accept.*/
	uint16_t host_total_num_acl_data_packet;/*Total number of HCI ACL Data Packets that can be stored in the data buffers of the Host*/
	uint16_t host_total_num_synchronous_data_packet;/*Total number of HCI synchronous Data Packets that can be stored in the data buffers of the Host*/

	ctrl_bsb_host_buffer_size_cmd(const std::vector<char> &data){
		this->ogf = HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS;
		this->ocf = HCI_CMD_OCF_CTRL_BSB_HOST_BUFFER_SIZE_COMMAND;
		parameter_total_length = data[COMMAND_FRAME_OFFSET];
		host_acl_data_packet_length = data[COMMAND_FRAME_OFFSET+1] + (data[COMMAND_FRAME_OFFSET+2]<<8);
		host_synchronous_data_packet_length = data[COMMAND_FRAME_OFFSET+3];
		host_total_num_acl_data_packet = data[COMMAND_FRAME_OFFSET+4] + (data[COMMAND_FRAME_OFFSET+5]<<8);
		host_total_num_synchronous_data_packet = data[COMMAND_FRAME_OFFSET+6] + (data[COMMAND_FRAME_OFFSET+7]<<8);
	}


	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		Json::Value parameters;
		parameters["host_acl_data_packet_length"] = host_acl_data_packet_length;
		parameters["host_synchronous_data_packet_length"] = host_synchronous_data_packet_length;
		parameters["host_total_num_acl_data_packet"] = host_total_num_acl_data_packet;
		parameters["host_total_num_synchronous_data_packet"] = host_total_num_synchronous_data_packet;
		output["parameters"] = parameters;
		return output;
	}

} ctrl_bsb_host_buffer_size_cmd_t;

#endif // HCICMDPACKET_H
