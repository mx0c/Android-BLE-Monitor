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
	command_complete.cpp
	
	Command complete Event management
	
	This specific event is put apart from the others since it features decoding of all possible response to existing commmand frame

	@author Bertrand Martel
	@version 1.0
*/
#include "hci_decoder/command_complete.h"
#include "hci_decoder/hci_global.h"
#include "hci_decoder/hci_response_packet.h"
#include <json/json.h>

using namespace std;

CommandComplete::CommandComplete(const std::vector<char> &data){

	this->event_code = HCI_EVENT_COMMAND_COMPLETE;
	this->parameter_total_length = data[EVENT_FRAME_OFFSET];
	this->num_hci_command_packets = data[EVENT_FRAME_OFFSET+1];
	ogf_ret = data[EVENT_FRAME_OFFSET+3]>>2;
	ocf_ret = (data[EVENT_FRAME_OFFSET+3]&0x03) + data[EVENT_FRAME_OFFSET+2];
	uint8_t size = parameter_total_length-3;
	for (unsigned int i = 0; i  < size;i++){
		return_parameters.push_back(data[EVENT_FRAME_OFFSET+4+i]);
	}

	response_frame = 0;

	if (ogf_ret!=HCI_CMD_OGF_VENDOR_SPECIFIC)
	{
		switch (ogf_ret)
		{
			case HCI_CMD_OGF_LINK_CONTROL_COMMANDS:
			{
				switch (ocf_ret)
				{
					case HCI_CMD_OCF_LINK_CONTROL_INQUIRY_COMMAND:
					{
						response_frame = new empty_response_cmd(return_parameters);
						break;	
					}
					case HCI_CMD_OCF_LINK_CONTROL_INQUIRY_CANCEL_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LINK_CONTROL_REMOTE_NAME_REQUEST_CANCEL_COMMAND:
					{
						response_frame = new remote_name_request_cancel_response_cmd(return_parameters);
						break;
					}
					default:
					{
						cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
					}
					break;
				}
				break;
			}
			case HCI_CMD_OGF_LINK_POLICY_COMMANDS:
			{
				switch(ocf_ret)
				{
					case HCI_CMD_OCF_LINK_POLICY_WRITE_DEFAULT_LINK_POLICY_SETTINGS_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					default:
					{
						cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
					}
				}
				break;
			}
			case HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS:
			{
				switch(ocf_ret)
				{
					case HCI_CMD_OCF_CTRL_BSB_RESET_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_SET_EVENT_FILTER_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_LOCAL_NAME_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_LOCAL_NAME_COMMAND:
					{
						response_frame = new ctrl_bsb_read_local_name_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_CLASS_OF_DEVICE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_class_of_device_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_CLASS_OF_DEVICE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_MODE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_inquiry_mode_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_MODE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_LE_HOST_SUPPORT_COMMAND:
					{
						response_frame = new ctrl_bsb_read_le_host_support_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_LE_HOST_SUPPORT_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_ACTIVITY_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_SCAN_ACTIVITY_COMMAND:
					{
						response_frame = new ctrl_bsb_read_inquiry_scan_activity_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_VOICE_SETTING_COMMAND:
					{
						response_frame = new ctrl_bsb_read_voice_settings_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_VOICE_SETTING_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_CURRENT_IAC_LAP_COMMAND:
					{
						response_frame = new ctrl_bsb_read_iac_lap_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_CURRENT_IAC_LAP_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_PAGE_SCAN_TYPE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_page_scan_type_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_SCAN_TYPE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_SIMPLE_PAIRING_MODE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_simple_pairing_mode_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_SIMPLE_PAIRING_MODE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_EXTENDED_INQUIRY_RESPONSE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_extended_inquiry_response_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_EXTENDED_INQUIRY_RESPONSE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_SCAN_ENABLE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_scan_enable_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_SCAN_ENABLE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_SCAN_TYPE_COMMAND:
					{
						response_frame = new ctrl_bsb_read_inquiry_scan_type_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_TYPE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_SET_EVENT_MASK_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_READ_PAGE_TIMEOUT_COMMAND:
					{
						response_frame = new ctrl_bsb_read_page_timeout_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_TIMEOUT_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_CTRL_BSB_HOST_BUFFER_SIZE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					default:
					{
						cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
					}
				}
				break;
			}
			case HCI_CMD_OGF_INFORMATIONAL_PARAMETERS:
			{
				switch (ocf_ret){
					case HCI_CMD_OCF_INFORMATIONAL_READ_LOCAL_SUPPORTED_COMMAND:
					{
						response_frame = new informational_read_local_supported_cmd_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_INFORMATIONAL_READ_BUFFER_SIZE_COMMAND:
					{
						response_frame = new informational_read_buffer_size_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_INFORMATIONAL_READ_BD_ADDR:
					{
						response_frame = new informational_read_bd_addr_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_INFORMATIONAL_READ_LOCAL_VERSION_INFORMATION_COMMAND:
					{
						response_frame = new informational_read_local_version_information_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_INFORMATIONAL_READ_LOCAL_EXTENDED_FEATURES_COMMAND:
					{
						response_frame = new informational_read_local_extended_features_response_cmd_t(return_parameters);
						break;
					}
					default:
					{
						cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
					}
				}
				break;
			}
			case HCI_CMD_OGF_STATUS_PARAMETERS:
			{
				cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
				break;
			}
			case HCI_CMD_OGF_TESTING_COMMANDS:
			{
				cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
				break;
			}
			case HCI_CMD_OGF_LE_CONTROLLER_COMMANDS:
			{
				switch (ocf_ret){

					case HCI_CMD_OCF_LE_SET_SCAN_PARAMETERS_COMMAND	:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_SET_EVENT_MASK_COMMAND :
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_SET_RANDOM_ADDRESS_COMMAND :
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_REMOVE_DEVICE_FROM_RESOLVING_LIST_COMMAND :
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_SET_SCAN_ENABLE_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_SET_ADVERTISING_PARAMETERS_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_CLEAR_WHITE_LIST_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_READ_WHITE_LIST_SIZE_COMMAND:
					{
						response_frame = new le_read_white_list_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_RAND_COMMAND:
					{
						response_frame = new le_rand_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_SET_ADVERTISING_DATA_COMMAND:
					{
						response_frame = new status_response_cmd(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_READ_BUFFER_SIZE_COMMAND:
					{
						response_frame = new le_read_buffer_size_response_cmd_t(return_parameters);
						break;
					}
					case HCI_CMD_OCF_LE_READ_LOCAL_SUPPORTED_FEATURES_COMMAND:
					{
						response_frame = new le_read_local_supported_features_response_cmd_t(return_parameters);
						break;
					}
					default:
					{
						cout << "[RESPONSE NOT DECODED] ogf : " << unsigned(ogf_ret) << " | ocf : " << unsigned(ocf_ret) << endl;
					}
				}
				break;
			}
		}
	}
}

CommandComplete::~CommandComplete(){
	delete response_frame;
}

void CommandComplete::print(){

	std::cout << "> COMMAND_COMPLETE : \n" << toJson(true).data() << std::endl;

}

Json::Value CommandComplete::toJsonObj(){

	Json::Value output;
	init(output);
	Json::Value parameters;
	parameters["num_hci_command_packets"] = num_hci_command_packets;

	Json::Value command_opcode_ret;
	Json::Value ogf_code;
	ogf_code["code"] = ogf_ret;

	if (ogf_ret!=HCI_CMD_OGF_VENDOR_SPECIFIC)
	{
		Json::Value ocf_code;
		ocf_code["code"] = ocf_ret;
		switch (ogf_ret)
		{
			case HCI_CMD_OGF_LINK_CONTROL_COMMANDS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_LINK_CONTROL_COMMANDS);
				if (COMMAND_OCF_LINK_CONTROL_STRING_ENUM.count(ocf_ret))
						ocf_code["value"] = COMMAND_OCF_LINK_CONTROL_STRING_ENUM.at(ocf_ret);
				break;
			}
			case HCI_CMD_OGF_LINK_POLICY_COMMANDS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_LINK_POLICY_COMMANDS);

				if (COMMAND_OCF_LINK_POLICY_STRING_ENUM.count(ocf_ret))
					ocf_code["value"] = COMMAND_OCF_LINK_POLICY_STRING_ENUM.at(ocf_ret);
				break;
			}
			case HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS);

				if (COMMAND_OCF_CTRL_BSB_STRING_ENUM.count(ocf_ret))
					ocf_code["value"] = COMMAND_OCF_CTRL_BSB_STRING_ENUM.at(ocf_ret);

				break;
			}
			case HCI_CMD_OGF_INFORMATIONAL_PARAMETERS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_INFORMATIONAL_PARAMETERS);

				if (COMMAND_OCF_INFORMATIONAL_STRING_ENUM.count(ocf_ret))
					ocf_code["value"] = COMMAND_OCF_INFORMATIONAL_STRING_ENUM.at(ocf_ret);

				break;
			}
			case HCI_CMD_OGF_STATUS_PARAMETERS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_STATUS_PARAMETERS);
				break;
			}
			case HCI_CMD_OGF_TESTING_COMMANDS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_TESTING_COMMANDS);
				break;
			}
			case HCI_CMD_OGF_LE_CONTROLLER_COMMANDS:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_LE_CONTROLLER_COMMANDS);

				if (COMMAND_OCF_LE_STRING_ENUM.count(ocf_ret))
					ocf_code["value"] = COMMAND_OCF_LE_STRING_ENUM.at(ocf_ret);
				
				break;
			}
			case HCI_CMD_OGF_VENDOR_SPECIFIC:
			{
				ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_VENDOR_SPECIFIC);
				break;
			}
		}
		command_opcode_ret["ocf"] = ocf_code;
	}
	command_opcode_ret["ogf"] = ogf_code;
	parameters["command_opcode"] = command_opcode_ret;

	Json::Value return_parameters_data;

	if (response_frame!=0){
		return_parameters_data["values"] = response_frame->toJson();
	}
	else{
		return_parameters_data["values"] = Json::Value(Json::arrayValue);
	}

	parameters["return_parameters"] =  return_parameters_data;

	output["parameters"] = parameters;
	return output;
}

std::string CommandComplete::toJson(bool beautify){
	return convert_json_to_string(beautify,toJsonObj());
}