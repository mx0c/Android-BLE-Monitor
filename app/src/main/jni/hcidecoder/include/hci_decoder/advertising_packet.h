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
	advertising_packet.h
	advertising packet decoding structures

	@author Bertrand Martel
	@version 1.0
*/
#ifndef ADVERTIZINGPACKET_H
#define ADVERTIZINGPACKET_H

#include <iostream>
#include "hci_decoder/IAdStructureFrame.h"
#include "json/json.h"

typedef struct advertising_common : public IAdStructureFrame {

	advertising_common(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		return output;
	}

} advertising_common_t;


typedef struct advertising_report_flags: public IAdStructureFrame {

	bool LE_Limited_Discoverable_Mode = false;
	bool LE_General_Discoverable_Mode = false;
	bool BR_EDR_not_supported = false;
	bool simulataneous_LE_BR_EDR_capable_controller = false;
	bool simulataneous_LE_BR_EDR_capable_host = false;

	advertising_report_flags(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}
		if (data.size()>0){
			if ((data[0] & 0b00000001) == 0b00000001)
				LE_Limited_Discoverable_Mode = true;
			if ((data[0] & 0b00000010) == 0b00000010)
				LE_General_Discoverable_Mode = true;
			if ((data[0] & 0b00000100) == 0b00000100)
				BR_EDR_not_supported = true;
			if ((data[0] & 0b00001000) == 0b00001000)
				simulataneous_LE_BR_EDR_capable_controller = true;
			if ((data[0] & 0b00010000) == 0b00010000)
				simulataneous_LE_BR_EDR_capable_host = true;	
		}
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		output["LE_Limited_Discoverable_Mode"] = LE_Limited_Discoverable_Mode;
		output["LE_General_Discoverable_Mode"] = LE_General_Discoverable_Mode;
		output["BR_EDR_not_supported"] = BR_EDR_not_supported;
		output["simulataneous_LE_BR_EDR_capable_controller"] = simulataneous_LE_BR_EDR_capable_controller;
		output["simulataneous_LE_BR_EDR_capable_host"] = simulataneous_LE_BR_EDR_capable_host;

		return output;
	}

} advertising_report_flags_t;

typedef struct advertising_manufacturer_specific_data : public IAdStructureFrame{

	std::string manufacturer_name;
	std::vector<char> manufacturer_data;
	int manufacturer_id;

	advertising_manufacturer_specific_data(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}
		
		if (data.size()>2){
			manufacturer_id = (data[1] << 8) + data[0];
		}
		if (data.size()>3){
			for (i=0;i< data.size()-2;i++){
				manufacturer_data.push_back(data[i+2]);
			}
		}
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);

		Json::Value manufacturer_data_list(Json::arrayValue);
		unsigned int i = 0;
		for (i = 0; i  < manufacturer_data.size();i++){
			manufacturer_data_list.append(manufacturer_data[i]);
		}
		output["manufacturer_data"] =  manufacturer_data_list;

		Json::Value manufacturer_obj;
		manufacturer_obj["code"] = manufacturer_id;
		

		if (COMMAND_OCF_LINK_CONTROL_STRING_ENUM.count(manufacturer_id)){
			manufacturer_obj["value"] = COMPANY_NUMBERS_STRING_ENUM.at(manufacturer_id);
		}
		else{
			manufacturer_obj["value"] = UNKNOWN_COMPANY;
		}
		output["manufacturer"] = manufacturer_obj;

		return output;
	}

} advertising_manufacturer_specific_data_t;

typedef struct advertising_shortened_local_name : public IAdStructureFrame{

	std::string shortened_local_name;

	advertising_shortened_local_name(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}

		char charac;
		for (i = 0; i < data.size(); i++) {
			charac = data[i];
			shortened_local_name += charac;
		}
	}

	
	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		output["shortened_local_name"] = shortened_local_name;
		return output;
	}

} advertising_shortened_local_name_t;

typedef struct advertising_complete_local_name : public IAdStructureFrame{

	std::string complete_local_name;

	advertising_complete_local_name(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}

		char charac;
		for (i = 0; i < data.size(); i++) {
			charac = data[i];
			complete_local_name += charac;
		}
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		output["complete_local_name"] = complete_local_name;
		return output;
	}

} advertising_complete_local_name_t;

typedef struct advertising_tx_power_level : public IAdStructureFrame{

	uint8_t tx_power;

	advertising_tx_power_level(ADVERTIZING_PACKET_TYPE_ENUM type, const std::vector<char> &data){
		this->length = data.size();
		this->type = type;
		unsigned int i =0;
		for (i=0;i < data.size();i++){
			this->data.push_back(data[i]);
		}
		if (data.size()>0){
			tx_power = data[0];
		}
	}

	Json::Value toJsonObj(){
		Json::Value output;
		init(output);
		output["tx_power"] = tx_power;
		return output;
	}

} advertising_tx_power_level_t;

#endif //ADVERTIZINGPACKET_H