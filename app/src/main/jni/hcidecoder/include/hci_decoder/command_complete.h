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
	command_complete.h
	HCI Event command complete frame

	@author Bertrand Martel
	@version 1.0
*/
#ifndef COMMANDCOMPLETE_H
#define COMMANDCOMPLETE_H

#include "hci_decoder/IHciEventFrame.h"
#include "hci_decoder/IHciResponseFrame.h"
#include "json/json.h"

/* HCI Event 0x0E : Command Complete Event*/
class CommandComplete : public IHciEventFrame
{

public:

	CommandComplete(const std::vector<char> &data);

	~CommandComplete();

	void print();

	std::string toJson(bool beautify);

	Json::Value toJsonObj();

private:

	/* 1B | number of HCI command packets which are allowed to be sent to the Controller from the Host */
	uint8_t                    num_hci_command_packets;

	uint8_t                    ogf_ret;
	uint8_t                    ocf_ret;

	/* 2B | Opcode of the command which caused this event */
	uint16_t                   command_opcode;

	/* XB | return parameter(s) for the command specified in the Command_Opcode event parameter */
	std::vector<uint8_t>       return_parameters;

	IHciResponseFrame*          response_frame;

};

#endif // COMMANDCOMPLETE_H
