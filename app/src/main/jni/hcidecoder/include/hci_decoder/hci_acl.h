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
#ifndef HCI_ACL_H
#define HCI_ACL_H

#include "IHciFrame.h"
#include "hci_global.h"
#include "json/json.h"

typedef struct hci_acl : IHciFrame{

	uint16_t connection_handle;
	uint8_t  packet_boundary_flag;
	uint8_t  broadcast_flag;
	uint16_t data_total_length;
	std::vector<uint8_t> acl_data;

	hci_acl(const std::vector<char> &data){
		connection_handle = (((data[0] & 0x0F) << 8) + (data[1] & 0xF0)) >> 4;
		packet_boundary_flag = data[1] & 0x0C;
		broadcast_flag = data[1] & 0x03;
		data_total_length = (data[2]<<8) + data[3];
		for (unsigned int i = 4; i  < data.size(); i++){
			acl_data.push_back(data[i]);
		}
	}

	/**
	 * @brief getPacketType
	 *      retrieve HCI Packet type (HCI_COMMAND / HCI_TYPE_ACL_DATA / HCI_SCO_DATA / HCI_EVENT)
	 * @return
	 */
	HCI_PACKET_TYPE_ENUM getPacketType(){
		return HCI_TYPE_ACL_DATA;
	}

	void print(){
		std::cout << toJson(true).data() << std::endl;
	}

	std::string toJson(bool beautify){
		return convert_json_to_string(beautify,toJsonObj());
	}

	Json::Value toJsonObj(){
		Json::Value output;

		Json::Value packet_type;
		packet_type["code"] = HCI_TYPE_ACL_DATA;
		packet_type["value"] = HCI_PACKET_TYPE_STRING_ENUM.at(HCI_TYPE_ACL_DATA);

		output["packet_type"] = packet_type;
		output["connection_handle"] = connection_handle;
		output["packet_boundary_flag"] = packet_boundary_flag;
		output["broadcast_flag"] = broadcast_flag;
		output["data_total_length"] = data_total_length;

		Json::Value data_val(Json::arrayValue);

		for (unsigned int i = 0; i  < data_total_length;i++){
			data_val.append(acl_data[i]);
		}

		output["data"] = data_val;
		
		return output;
	};

} hci_acl_t;

#endif //HCI_ACL_H