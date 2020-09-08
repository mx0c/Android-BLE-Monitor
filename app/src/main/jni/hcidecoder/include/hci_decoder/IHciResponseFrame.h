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
	IHciResponseFrame.h
	HCI command response frame

	@author Bertrand Martel
	@version 1.0
*/
#ifndef IHCIRESPONSEFRAME_H
#define IHCIRESPONSEFRAME_H

#include "hci_decoder/IHciFrame.h"
#include "hci_decoder/hci_global.h"
#include "json/json.h"

/**
 * @brief IHciResponseFrame class
 *      Interface defining all a generic HCI Response Frame
 *
 */
class IHciResponseFrame{

public:

	virtual Json::Value toJson(){
		return Json::Value(Json::arrayValue);
	}

	virtual ~IHciResponseFrame(){

	}

protected:

	std::vector<uint8_t> response_data;

	uint8_t status;

};

#endif // IHCIRESPONSEFRAME_H
