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
	hci_global.h
	generation of enum/map according to supported HCI Event/Command

	@author Bertrand Martel
	@version 1.0
*/
#ifndef HCIGLOBAL_H
#define HCIGLOBAL_H

#include <iostream>
#include <json/json.h>

//excluding command code decode from parameter length field
#define COMMAND_FRAME_OFFSET 3

//excluding event code decode from parameter length field
#define EVENT_FRAME_OFFSET   2

#define ACL_FRAME_OFFSET     1

#define E(x,y) x = y,
enum COMMAND_OCF_LE_ENUM {
#include "hci_decoder/hci_le_command.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OCF_LE_STRING_ENUM = {
#include "hci_decoder/hci_le_command.h"
};

#define E(x,y) x = y,
enum COMMAND_OCF_CTRL_BSB_ENUM {
#include "hci_decoder/hci_ctrl_bsb_command.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OCF_CTRL_BSB_STRING_ENUM = {
#include "hci_decoder/hci_ctrl_bsb_command.h"
};

#define E(x,y) x = y,
enum COMMAND_OCF_INFORMATIONAL_ENUM {
#include "hci_decoder/hci_informational_command.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OCF_INFORMATIONAL_STRING_ENUM = {
#include "hci_decoder/hci_informational_command.h"
};

#define E(x,y) x = y,
enum COMMAND_OCF_LINK_POLICY_ENUM {
#include "hci_decoder/hci_link_policy_command.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OCF_LINK_POLICY_STRING_ENUM = {
#include "hci_decoder/hci_link_policy_command.h"
};

#define E(x,y) x = y,
enum COMMAND_OCF_LINK_CONTROL_ENUM {
#include "hci_decoder/hci_link_control_command.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OCF_LINK_CONTROL_STRING_ENUM = {
#include "hci_decoder/hci_link_control_command.h"
};

#define E(x,y) x = y,
enum EVENT_ENUM {
#include "hci_decoder/hci_event.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> EVENT_STRING_ENUM = {
#include "hci_decoder/hci_event.h"
};

#define E(x,y) x = y,
enum LE_SUBEVENT_ENUM {
#include "hci_decoder/hci_subevent.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> LE_SUBEVENT_STRING_ENUM = {
#include "hci_decoder/hci_subevent.h"
};

#define E(x,y) x = y,
enum COMMAND_OGF_ENUM {
#include "hci_decoder/hci_ogf.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMMAND_OGF_STRING_ENUM = {
#include "hci_decoder/hci_ogf.h"
};

#define E(x,y) x = y,
enum HCI_PACKET_TYPE_ENUM {
#include "hci_decoder/hci_packet_type.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> HCI_PACKET_TYPE_STRING_ENUM = {
#include "hci_decoder/hci_packet_type.h"
};

#define E(x,y) x = y,
enum ADVERTIZING_PACKET_TYPE_ENUM {
#include "hci_decoder/advertising_packet_type.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> ADVERTIZING_PACKET_STRING_ENUM = {
#include "hci_decoder/advertising_packet_type.h"
};

#define E(x,y) x = y,
enum COMPANY_NUMBERS_ENUM {
#include "hci_decoder/company_number.h"
};

#define E(x,y) { x,#x },
const std::map< int, std::string> COMPANY_NUMBERS_STRING_ENUM = {
#include "hci_decoder/company_number.h"
};

struct bt_address{
	
	uint8_t address[6];

	void print(){
		printf("address : %02X:%02X:%02X:%02X:%02X:%02X\n",address[0],address[1],address[2],address[3],address[4],address[5]);
	}

	std::string toString(){
		char output[24];
		sprintf(output,"%02X:%02X:%02X:%02X:%02X:%02X",address[0],address[1],address[2],address[3],address[4],address[5]);
		std::string out = output;
		return out;
	}

};

inline uint8_t get_ogf(uint8_t data){
	return (data>>2);
}

inline uint8_t get_ocf(uint8_t msb,uint8_t lsb){
	return ((msb & 0x03) + lsb);
}

/**
 * convert Json value to string without linefeed / indent
 */
inline std::string convert_json_to_string(bool beautify,Json::Value output){

	if (!beautify){
		Json::StreamWriterBuilder builder;
		builder.settings_["indentation"] = "";
		return Json::writeString(builder, output);
	}
	else{
		return output.toStyledString();
	}
}

#endif // HCIGLOBAL_H
