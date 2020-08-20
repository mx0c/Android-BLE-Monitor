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
	datalink.h

	list of datalink types

	@author Bertrand Martel
	@version 0.1
*/

#ifndef DATALINK_TYPE_H
#define DATALINK_TYPE_H

#include "string"

enum datalink_type{

	UNKNOWN=-1,

	//for regular snoop file
	IEEE_802_3 = 0,
	IEEE_802_4_TOKEN_BUS = 1,
	IEEE_802_5_TOKEN_RING = 2,
	IEEE_802_6_METRO_NET = 3,
	ETHERNET = 4,
	HDLC = 5,
	CHARACTER_SYNCHRONOUS = 6,
	IBM_CHANNEL_TO_CHANNEL = 7,
	FDDI = 8,
	OTHER = 9,

	//for btsnoop file
	HCI_UN_ENCAPSULATED = 1001,
	HCI_UART = 1002,
	HCI_BSCP = 1003,
	HCI_SERIAL = 1004
};

#endif // DATALINK_TYPE_H
