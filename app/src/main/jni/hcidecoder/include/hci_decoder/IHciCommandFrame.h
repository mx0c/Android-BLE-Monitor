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
	IHciCommandFrame.h
	HCI event frame

	@author Bertrand Martel
	@version 1.0
*/
#ifndef IHCICOMMANDFRAME_H
#define IHCICOMMANDFRAME_H

#include "hci_decoder/IHciFrame.h"
#include "hci_decoder/hci_global.h"
#include <json/json.h>

/**
 * @brief IHciCommandFrame class
 *      Interface defining all a generic HCI Command Frame
 *
 */
class IHciCommandFrame : public virtual IHciFrame{

public:

	/**
	 * @brief getOGF
	 *      retrieve Opcode Group Frame
	 * @return
	 */
	COMMAND_OGF_ENUM getOGF(){
		return ogf;
	}

	/**
	 * @brief getOCF
	 *      retrieve Opcode Command Frame
	 * @return
	 */
	int getOCF(){
		return ocf;
	}

	/**
	 * @brief getPacketType
	 *      retrieve HCI Packet type (HCI_COMMAND / HCI_ACL_DATA / HCI_SCO_DATA / HCI_EVENT)
	 * @return
	 */
	HCI_PACKET_TYPE_ENUM getPacketType(){
		return HCI_TYPE_COMMAND;
	}

	void print(){
		std::cout << toJson(true).data() << std::endl;
	}

	std::string toJson(bool beautify){
		return convert_json_to_string(beautify,toJsonObj());
	}

	/**
	 * @brief getParamterTotalLength
	 *      number in bytes of total parameter length
	 * @return
	 */
	uint8_t getParamterTotalLength(){
		return parameter_total_length;
	}

protected:

	void init(Json::Value& output){

		output["parameter_total_length"] = parameter_total_length;

		Json::Value packet_type;
		packet_type["code"] = HCI_TYPE_COMMAND;
		packet_type["value"] = HCI_PACKET_TYPE_STRING_ENUM.at(HCI_TYPE_COMMAND);
		output["packet_type"] = packet_type;

		Json::Value ogf_code;
		ogf_code["code"] = ogf;
		ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(ogf);
		output["ogf"] = ogf_code;

		if (ogf!=HCI_CMD_OGF_VENDOR_SPECIFIC)
		{
			Json::Value ocf_code;
			ocf_code["code"] = ocf;
			switch (ogf)
			{
				case HCI_CMD_OGF_LINK_CONTROL_COMMANDS:
				{
					ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_LINK_CONTROL_COMMANDS);
					if (COMMAND_OCF_LINK_CONTROL_STRING_ENUM.count(ocf))
						ocf_code["value"] = COMMAND_OCF_LINK_CONTROL_STRING_ENUM.at(ocf);
					break;
				}
				case HCI_CMD_OGF_LINK_POLICY_COMMANDS:
				{
					ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_LINK_POLICY_COMMANDS);

					if (COMMAND_OCF_LINK_POLICY_STRING_ENUM.count(ocf))
						ocf_code["value"] = COMMAND_OCF_LINK_POLICY_STRING_ENUM.at(ocf);
					break;
				}
				case HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS:
				{
					ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS);

					if (COMMAND_OCF_CTRL_BSB_STRING_ENUM.count(ocf))
						ocf_code["value"] = COMMAND_OCF_CTRL_BSB_STRING_ENUM.at(ocf);

					break;
				}
				case HCI_CMD_OGF_INFORMATIONAL_PARAMETERS:
				{
					ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_INFORMATIONAL_PARAMETERS);

					if (COMMAND_OCF_INFORMATIONAL_STRING_ENUM.count(ocf))
						ocf_code["value"] = COMMAND_OCF_INFORMATIONAL_STRING_ENUM.at(ocf);

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

					if (COMMAND_OCF_LE_STRING_ENUM.count(ocf))
						ocf_code["value"] = COMMAND_OCF_LE_STRING_ENUM.at(ocf);
					
					break;
				}
				case HCI_CMD_OGF_VENDOR_SPECIFIC:
				{
					ogf_code["value"] = COMMAND_OGF_STRING_ENUM.at(HCI_CMD_OGF_VENDOR_SPECIFIC);
					break;
				}
			}
			output["ocf"] = ocf_code;
		}
	}

	/* Opcode group field 1B | (6 bits)*/
	COMMAND_OGF_ENUM  ogf;

	/* Opcode command field 2B | 0x000C */
	uint8_t ocf;

	/*parameters length*/
	uint8_t parameter_total_length;
};

#endif // IHCICOMMANDFRAME_H
