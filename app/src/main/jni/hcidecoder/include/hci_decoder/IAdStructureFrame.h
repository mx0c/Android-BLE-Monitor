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
	IAdStructureFrame.h
	Advertizing structure frame

	@author Bertrand Martel
	@version 1.0
*/
#ifndef IADSTRUCTUREFRAME_H
#define IADSTRUCTUREFRAME_H

#include "hci_decoder/hci_global.h"
#include "json/json.h"

/**
 * @brief IAdStructureFrame class
 *      Interface defining all generic Ad Structure packets
 *
 */
class IAdStructureFrame {

public:

	/**
	 * @brief
	 *      retrieve advertizing structure length
	 * @return
	 */
	uint8_t getLength(){
		return length;
	}

	/**
	 * @brief
	 *      retrieve advertizing packet type
	 * @return
	 */
	ADVERTIZING_PACKET_TYPE_ENUM getType(){
		return type;
	}

	void print(){
		std::cout << toJson(true).data() << std::endl;
	}

	/**
	 * @brief
	 *      convert frame information to beautiful json format
	 * @return
	 */
	virtual std::string toJson(bool beautify){
		return "{}";
	};

	/**
	 * @brief
	 *      convert frame information to beautiful json format
	 * @return
	 */
	virtual Json::Value toJsonObj(){
		return Json::Value(Json::arrayValue);
	};
	
	/**
	 * @brief
	 *      advertizing packet data
	 * @return
	 */
	std::vector<uint8_t> getData(){
		return data;
	}

	virtual ~IAdStructureFrame(){

	}

protected:

	void init(Json::Value& output){
		output["length"] = length;

		Json::Value packet_type;
		packet_type["code"] = type;
		packet_type["value"] = ADVERTIZING_PACKET_STRING_ENUM.at(type);
		output["type"] = packet_type;
		
		Json::Value data_json(Json::arrayValue);
		for (unsigned int i = 0; i < length;i++){
			data_json.append(data[i]);
		}
		output["data"] = data_json;
	}

	/**
	 * advertising packet length (include type + data)
	 */
	uint8_t length;

	/**
	 * advertising packet type
	 */
	ADVERTIZING_PACKET_TYPE_ENUM  type;

	/**
	 * advertising packet data
	 */
	std::vector<uint8_t> data;
};

#endif // IADSTRUCTUREFRAME_H
