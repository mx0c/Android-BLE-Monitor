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
	IHciEventFrame.h
	HCI event frame

	@author Bertrand Martel
	@version 1.0
*/
#ifndef IHCIEVENTFRAME_H
#define IHCIEVENTFRAME_H

#include "hci_decoder/IHciFrame.h"
#include "hci_decoder/hci_global.h"
#include "json/json.h"

/**
 * @brief IHciEventFrame class
 *      Interface defining all a generic HCI Event Frame
 *
 */
class IHciEventFrame : public virtual IHciFrame{

public:

	/**
	 * @brief getEventCode
	 *      retrieve event code
	 * @return
	 */
	EVENT_ENUM getEventCode(){
		return event_code;
	}

	LE_SUBEVENT_ENUM getSubEventCode(){
		return subevent_code;
	}

	void print(){
		std::cout << "> " << HCI_PACKET_TYPE_STRING_ENUM.at(HCI_TYPE_EVENT) << " : \n" << toJson(true).data() << std::endl;
	}

	std::string toJson(bool beautify){
		return convert_json_to_string(beautify,toJsonObj());
	}
	/**
	 * @brief getPacketType
	 *      retrieve HCI Packet type (HCI_COMMAND / HCI_ACL_DATA / HCI_SCO_DATA / HCI_EVENT)
	 * @return
	 */
	HCI_PACKET_TYPE_ENUM getPacketType(){
		return HCI_TYPE_EVENT;
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

		Json::Value packet_type;
		packet_type["code"] = HCI_TYPE_EVENT;
		packet_type["value"] = HCI_PACKET_TYPE_STRING_ENUM.at(HCI_TYPE_EVENT);
		output["packet_type"] = packet_type;

		Json::Value code;
		code["code"] = event_code;
		code["value"] = EVENT_STRING_ENUM.at(event_code);
		output["event_code"] = code;

		output["parameter_total_length"] = parameter_total_length;
	}

	/*event code*/
	EVENT_ENUM event_code;

	/*subevent code*/
	LE_SUBEVENT_ENUM subevent_code;

	/*parameters length*/
	uint8_t parameter_total_length;
};

#endif // IHCIEVENTFRAME_H
