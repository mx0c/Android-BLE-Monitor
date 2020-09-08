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
	IHciFrame.h
	HCI frame template

	@author Bertrand Martel
	@version 1.0
*/
#ifndef IHCIFRAME_H
#define IHCIFRAME_H

#include "hci_decoder/hci_global.h"
#include "json/json.h"

/**
 * @brief The IHciFrame class
 *      Interface defining all a generic HCI Frame
 *
 */
class IHciFrame{

public:

	/**
	 * @brief toStyledJson
	 *      convert frame information to beautiful json format
	 * @return
	 */
	virtual std::string toJson(bool beautify){
		return "{}";
	};

	/**
	 * @brief toStyledJson
	 *      convert frame information to beautiful json format
	 * @return
	 */
	virtual Json::Value toJsonObj(){
		return Json::Value(Json::arrayValue);
	};

	/**
	 * @brief getPacketType
	 *      retrieve HCI Packet type (HCI_COMMAND / HCI_ACL_DATA / HCI_SCO_DATA / HCI_EVENT)
	 * @return
	 */
	virtual HCI_PACKET_TYPE_ENUM getPacketType(){
		return HCI_TYPE_UNKNOWN;
	}

	virtual void print(){
	}

	virtual void clear(){

	}

	virtual ~IHciFrame(){

	}
};

#endif // IHCIFRAME_H
