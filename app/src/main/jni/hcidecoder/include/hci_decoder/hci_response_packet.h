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
	HCI event stuctures for each supported HCI Response Command

	@author Bertrand Martel
	@version 1.0
*/
#ifndef HCIRESPONSEPACKET_H
#define HCIRESPONSEPACKET_H

#include "json/json.h"
#include "hci_decoder/hci_global.h"

/*response with status only*/
typedef struct status_response_cmd : public IHciResponseFrame {

	status_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		return output;
	}

} status_response_cmd_t;

/* empty response*/
typedef struct empty_response_cmd : public IHciResponseFrame {

	empty_response_cmd(const std::vector<uint8_t> &data){
	}

	Json::Value toJson(){
		Json::Value output;
		return output;
	}

} empty_response_cmd_t;

/**********************************************************************/
/**********************************************************************/
/********************* LINK POLICY COMMAND ****************************/
/**********************************************************************/
/**********************************************************************/

/*HCI Command : OGF=0x01 | OCF=0x001A remote name request cancel response Command*/
typedef struct remote_name_request_cancel_response_cmd : public IHciResponseFrame {

	bt_address  bd_addr;

	remote_name_request_cancel_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		bd_addr.address[0] = data[6];
		bd_addr.address[1] = data[5];
		bd_addr.address[2] = data[4];
		bd_addr.address[3] = data[3];
		bd_addr.address[4] = data[2];
		bd_addr.address[5] = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["bd_addr"] = bd_addr.toString();
		return output;
	}

} remote_name_request_cancel_response_cmd_t;

/**********************************************************************/
/**********************************************************************/
/************ INFORMATIONAL PARAMETERS COMMAND ************************/
/**********************************************************************/
/**********************************************************************/

/*HCI Command : OGF=0x04 | OCF=0x0002 Read local supported Command*/
typedef struct informational_read_local_supported_cmd_response_cmd : public IHciResponseFrame {

	std::vector<uint8_t>  supported_hci_command_mask; /*mask featuring all hci commands supported*/

	informational_read_local_supported_cmd_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		for (unsigned int i = 0 ; i < 64;i++){
			supported_hci_command_mask.push_back(data[1+i]);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		Json::Value supported_hci_command_mask_vals(Json::arrayValue);
		for (unsigned int i = 0;i<64;i++){
			supported_hci_command_mask_vals.append(supported_hci_command_mask[i]);
		}
		output["supported_hci_command_mask"] = supported_hci_command_mask_vals;
		return output;
	}

} informational_read_local_supported_cmd_response_cmd_t;

/*HCI Command : OGF=0x04 | OCF=0x0005 Read Buffer size Command*/
typedef struct informational_read_buffer_size_response_cmd : public IHciResponseFrame {

	uint16_t acl_data_packet_length;
	uint8_t synchronous_data_packet_length;
	uint16_t total_num_acld_data_packet_length;
	uint16_t total_num_synchronous_data_packet_length;

	informational_read_buffer_size_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		acl_data_packet_length = data[1] + (data[2]<<8);
		synchronous_data_packet_length = data[3];
		total_num_acld_data_packet_length = data[4] + (data[5]<<8);
		total_num_synchronous_data_packet_length = data[6] + (data[7]<<8);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["acl_data_packet_length"] = acl_data_packet_length;
		output["synchronous_data_packet_length"] = synchronous_data_packet_length;
		output["total_num_acld_data_packet_length"] = total_num_acld_data_packet_length;
		output["total_num_synchronous_data_packet_length"] = total_num_synchronous_data_packet_length;
		return output;
	}

} informational_read_buffer_size_response_cmd_t;

/*HCI Command : OGF=0x04 | OCF=0x0009 Read BD ADDR Command*/
typedef struct informational_read_bd_addr_response_cmd : public IHciResponseFrame {

	bt_address  bd_addr;

	informational_read_bd_addr_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		bd_addr.address[0] = data[6];
		bd_addr.address[1] = data[5];
		bd_addr.address[2] = data[4];
		bd_addr.address[3] = data[3];
		bd_addr.address[4] = data[2];
		bd_addr.address[5] = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["bd_addr"] = bd_addr.toString();
		return output;
	}

} informational_read_bd_addr_response_cmd_t;

/*HCI Command : OGF=0x04 | OCF=0x0001 Read local version information Command*/
typedef struct informational_read_local_version_information_response_cmd : public IHciResponseFrame {

	uint16_t hci_version; /*Revision of the Current HCI in the BR/EDR Controller*/
	uint8_t  lmp_pal_version;
	uint16_t manufacturer_name; /*Manufacturer Name of the BR/EDR Controller*/
	uint16_t lmp_pal_subversion; /*Subversion of the Current LMP or PAL in the Controller. This value is implementation dependent*/

	informational_read_local_version_information_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		hci_version = data[1] + (data[2]<<8);
		lmp_pal_version = data[3];
		manufacturer_name = data[4] + (data[5]<<8);
		lmp_pal_subversion = data[6] + (data[7]<<8);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["hci_version"] = hci_version;
		output["lmp_pal_version"] = lmp_pal_version;
		output["manufacturer_name"] = manufacturer_name;
		output["lmp_pal_subversion"] = lmp_pal_subversion;
		return output;
	}

} informational_read_local_version_information_response_cmd_t;

/*HCI Command : OGF=0x04 | OCF=0x0004 Read local extended features Command*/
typedef struct informational_read_local_extended_features_response_cmd : public IHciResponseFrame {

	uint8_t page_number;
	uint8_t maximum_page_number; /*The highest features page number which contains non-zero bits for the local device*/
	std::vector<uint8_t> extended_lmp_features; /*Bit map of requested page of LMP features.*/

	informational_read_local_extended_features_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		page_number = data[1];
		maximum_page_number = data[2];
		for (unsigned int i = 0; i < 8;i++){
			extended_lmp_features.push_back(data[3+i]);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["page_number"] = page_number;
		output["maximum_page_number"] = maximum_page_number;
		Json::Value extended_lmp_features_val(Json::arrayValue);

		for (unsigned int i = 0; i < 8;i++){
			extended_lmp_features_val.append(extended_lmp_features[i]);
		}
		output["extended_lmp_features"] = extended_lmp_features_val;
		return output;
	}

} informational_read_local_extended_features_response_cmd_t;

/**********************************************************************/
/**********************************************************************/
/*********************** LE CONTROLLER COMMAND ************************/
/**********************************************************************/
/**********************************************************************/

/*HCI Command : OGF=0x08 | OCF=0x000F LE Read White list Command*/
typedef struct le_read_white_list_response_cmd : public IHciResponseFrame {

	uint8_t white_list_size; /*Total number of white list entries that can be stored in the Controller*/

	le_read_white_list_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		white_list_size = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["white_list_size"] = white_list_size;
		return output;
	}

} le_read_white_list_response_cmd_t;


/*HCI Command : OGF=0x08 | OCF=0x0018 LE Rand Command*/
typedef struct le_rand_response_cmd : public IHciResponseFrame {

	std::vector<uint8_t> random_number; /*Random Number*/

	le_rand_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		for (unsigned int i = 0; i < 8;i++){
			random_number.push_back(data[1+i]);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		Json::Value random_number_val(Json::arrayValue);

		for (unsigned int i = 0; i < 8;i++){
			random_number_val.append(random_number[i]);
		}
		output["random_number"] = random_number_val;
		return output;
	}

} le_rand_response_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0002 LE Read Buffer Size Command*/
typedef struct le_read_buffer_size_response_cmd : public IHciResponseFrame {

	uint16_t le_data_packet_length;
	uint8_t  total_num_le_data_packet_length;

	le_read_buffer_size_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		le_data_packet_length = data[1] + (data[2]<<8);
		total_num_le_data_packet_length = data[3];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["le_data_packet_length"] = le_data_packet_length;
		output["total_num_le_data_packet_length"] = total_num_le_data_packet_length;
		return output;
	}

} le_read_buffer_size_response_cmd_t;

/*HCI Command : OGF=0x08 | OCF=0x0003 LE Read Local Supported Features Command*/
typedef struct le_read_local_supported_features_response_cmd : public IHciResponseFrame {

	std::vector<uint8_t> supported_le_features; /*Bit Mask List of supported LE features */

	le_read_local_supported_features_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		for (unsigned int i = 0; i < 8;i++){
			supported_le_features.push_back(data[1+i]);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		Json::Value supported_le_features_vals(Json::arrayValue);
		for (unsigned int i = 0 ; i< 8;i++){
			supported_le_features_vals.append(supported_le_features[i]);
		}
		output["supported_le_features"] = supported_le_features_vals;
		return output;
	}

} le_read_local_supported_features_response_cmd_t;

/**********************************************************************/
/**********************************************************************/
/***************** CONTROLLER BASEBAND COMMAND ************************/
/**********************************************************************/
/**********************************************************************/

/*HCI Command : OGF=0x03 | OCF=0x0014 Read local name Command*/
typedef struct ctrl_bsb_read_local_name_response_cmd : public IHciResponseFrame {

	std::string local_name;

	ctrl_bsb_read_local_name_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		local_name="";
		bool found = false;
		for (unsigned int i = 0; i <248 && !found;i++){
			if (data[1+i]!='\0'){
				local_name+=data[1+i];
			}
			else{
				found=true;
			}
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["local_name"] = local_name;
		return output;
	}

} ctrl_bsb_read_local_name_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0023 Read Class of Device Command*/
typedef struct ctrl_bsb_read_class_of_device_response_cmd : public IHciResponseFrame {

	uint32_t device_class; /*3B : Class of Device for the device*/

	ctrl_bsb_read_class_of_device_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		device_class = data[1] + (data[2]<<8) + (data[3]<<16);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["device_class"] = device_class;
		return output;
	}

} ctrl_bsb_read_class_of_device_response_cmd_t;


/*HCI Command : OGF=0x03 | OCF=0x0044 Read inquiry mode Command*/
typedef struct ctrl_bsb_read_inquiry_mode_response_cmd : public IHciResponseFrame {

	uint8_t inquiry_mode; /*1B : 0x00:Standard Inquiry Result event format | 0x01: Inquiry Result format with RSSI | 0x02: Inquiry Result with RSSI format or Extended Inquiry Result format*/

	ctrl_bsb_read_inquiry_mode_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		inquiry_mode = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["inquiry_mode"] = inquiry_mode;
		return output;
	}

} ctrl_bsb_read_inquiry_mode_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x006C Read LE host support Command*/
typedef struct ctrl_bsb_read_le_host_support_response_cmd : public IHciResponseFrame {

	uint8_t le_supported_host; /*1B*/
	uint8_t simultaneous_le_host; /*1B*/

	ctrl_bsb_read_le_host_support_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		le_supported_host = data[1];
		simultaneous_le_host = data[2];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["le_supported_host"] = le_supported_host;
		output["simultaneous_le_host"] = simultaneous_le_host;
		return output;
	}

} ctrl_bsb_read_le_host_support_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x001D Read Inquiry Scan activity Command*/
typedef struct ctrl_bsb_read_inquiry_scan_activity_response_cmd : public IHciResponseFrame {

	uint16_t inquiry_scan_interval;
	uint16_t inquiry_scan_window;

	ctrl_bsb_read_inquiry_scan_activity_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		inquiry_scan_interval = data[1] + (data[2]<<8);
		inquiry_scan_window =data[3] + (data[4]<<8);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["inquiry_scan_interval"] = inquiry_scan_interval;
		output["inquiry_scan_window"] = inquiry_scan_window;
		return output;
	}

} ctrl_bsb_read_inquiry_scan_activity_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0025 Read voice settings Command*/
typedef struct ctrl_bsb_read_voice_settings_response_cmd : public IHciResponseFrame {

	uint16_t voice_settings; /*2B : 10 Bits meaningful*/

	ctrl_bsb_read_voice_settings_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		voice_settings = data[1] + (data[2]<<8);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["voice_settings"] = voice_settings;
		return output;
	}

} ctrl_bsb_read_voice_settings_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0039 Read Inquiry Access Code (IAC) LAP Command*/
typedef struct ctrl_bsb_read_iac_lap_response_cmd : public IHciResponseFrame {

	uint8_t num_current_iac; /*Specifies the number of IACs (Inquiry Access Codes) which are currently in use by the local BR/
								EDR Controller to simultaneously listen for during an Inquiry Scan*/

	std::vector<uint32_t> lap_iac; /*LAP(s) used to create IAC which is currently in use by the local BR/EDR
									Controller to simultaneously listen for during an Inquiry Scan*/

	ctrl_bsb_read_iac_lap_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		num_current_iac = data[1];
		for (unsigned int i = 0 ; i< num_current_iac;i++){
			uint32_t lap = data[2+i] + (data[3+i]<<8) + (data[4+i]<<16);
			lap_iac.push_back(lap);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["num_current_iac"] = num_current_iac;
		Json::Value laps(Json::arrayValue);
		for (unsigned int i = 0; i  < num_current_iac;i++){
			laps.append(lap_iac[i]);
		}
		output["lap_iac"] = laps;
		return output;
	}

} ctrl_bsb_read_iac_lap_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0046 Read page scan type Command*/
typedef struct ctrl_bsb_read_page_scan_type_response_cmd : public IHciResponseFrame {

	uint8_t page_scan_type; /*1B : 0x00:Mandatory: Standard Scan (default) | 0x01: Optional: Interlaced Scan*/

	ctrl_bsb_read_page_scan_type_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		page_scan_type = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["page_scan_type"] = page_scan_type;
		return output;
	}

} ctrl_bsb_read_page_scan_type_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0055 Read simple pairing mode Command*/
typedef struct ctrl_bsb_read_simple_pairing_mode_response_cmd : public IHciResponseFrame {

	uint8_t simple_pairing_mode; /*1B : 0x00:Simple Pairing disabled | 0x01:Simple pairing enabled*/

	ctrl_bsb_read_simple_pairing_mode_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		simple_pairing_mode = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["simple_pairing_mode"] = simple_pairing_mode;
		return output;
	}

} ctrl_bsb_read_simple_pairing_mode_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0051 Read extended inquiry response Command*/
typedef struct ctrl_bsb_read_extended_inquiry_response_response_cmd : public IHciResponseFrame {

	uint8_t              fec_required; /*1B : 0x00:FEC (Forward Error correction) is not required | 0x01:FEC is required*/
	std::vector<uint8_t> extended_inquiry_response;

	ctrl_bsb_read_extended_inquiry_response_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		fec_required = data[1];
		for (unsigned int i = 0;i < 240;i++){
			extended_inquiry_response.push_back(data[2+i]);
		}
	}

	Json::Value toJson(){
		Json::Value output;
		output["fec_required"] = fec_required;
		Json::Value extended_inquiry_response_val(Json::arrayValue);
		for (unsigned int i = 0;i < 240;i++){
			extended_inquiry_response_val.append(extended_inquiry_response[i]);
		}
		output["extended_inquiry_response"] = extended_inquiry_response_val;
		return output;
	}

} ctrl_bsb_read_extended_inquiry_response_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0019 Read scan enable Command*/
typedef struct ctrl_bsb_read_scan_enable_response_cmd : public IHciResponseFrame {

	uint8_t scan_enable; /*1B : 0x00:No Scans enabled | 0x01: Inquiry Scan enabled/Page Scan disabled | 0x02: Inquiry Scan disabled/Page Scan enabled | 0x03:Inquiry Scan enabled/Page Scan enabled*/

	ctrl_bsb_read_scan_enable_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		scan_enable = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["scan_enable"] = scan_enable;
		return output;
	}

} ctrl_bsb_read_scan_enable_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0042 Read inquiry scan type Command*/
typedef struct ctrl_bsb_read_inquiry_scan_type_response_cmd : public IHciResponseFrame {

	uint8_t scan_type; /*1B : 0x00:Standard Scan | 0x01: Interlaced Scan*/

	ctrl_bsb_read_inquiry_scan_type_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		scan_type = data[1];
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["scan_type"] = scan_type;
		return output;
	}

} ctrl_bsb_read_inquiry_scan_type_response_cmd_t;

/*HCI Command : OGF=0x03 | OCF=0x0017 Read page timeout Command*/
typedef struct ctrl_bsb_read_page_timeout_response_cmd : public IHciResponseFrame {

	uint16_t page_timeout; /* 0:Illegal Page Timeout. Must be larger than 0 otherwise Page Timeout measured in Number of Baseband slots*/

	ctrl_bsb_read_page_timeout_response_cmd(const std::vector<uint8_t> &data){
		status = data[0];
		page_timeout = data[1] + (data[2]<<8);
	}

	Json::Value toJson(){
		Json::Value output;
		output["status"] = status;
		output["page_timeout"] = page_timeout;
		return output;
	}

} ctrl_bsb_read_page_timeout_response_cmd_t;

#endif //HCIRESPONSEPACKET_H