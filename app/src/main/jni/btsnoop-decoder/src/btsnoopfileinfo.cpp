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
	btsnoopfileinfo.cpp

	Parse bt snoop header

	@author Bertrand Martel
	@version 0.1
*/

#include "btsnoop/btsnoopfileinfo.h"
#include "iostream"

using namespace std;

BtSnoopFileInfo::BtSnoopFileInfo(){
	version_number = 0;
	datalink = UNKNOWN;
	datalakink_str = "";
	identification_number = "";
}

/**
 * @brief
 *      build file header
 * @param data
 *      file header data of size 16 (16 octet => 8 + 4 + 4)
 */
BtSnoopFileInfo::BtSnoopFileInfo(char* data){

	identification_number=std::string(data,data+8);

	version_number=0;

	for (int i = 8;i<12;i++){
		version_number+=((data[i] & 0xFF) << (3-(i-8))*8);
	}

	unsigned int datalink_num = 0;

	datalink=UNKNOWN;
	datalakink_str="";

	for (int i = 12;i<16;i++){
		datalink_num+=((data[i] & 0xFF) << ((3-(i-12)))*8);
	}

	switch (datalink_num){

		case IEEE_802_3:
			datalink=IEEE_802_3;
			datalakink_str="IEEE_802_3";
			break;
		case IEEE_802_4_TOKEN_BUS:
			datalink=IEEE_802_4_TOKEN_BUS;
			datalakink_str="IEEE_802_4_TOKEN_BUS";
			break;
		case IEEE_802_5_TOKEN_RING:
			datalink=IEEE_802_5_TOKEN_RING;
			datalakink_str="IEEE_802_5_TOKEN_RING";
			break;
		case IEEE_802_6_METRO_NET:
			datalink=IEEE_802_6_METRO_NET;
			datalakink_str="IEEE_802_6_METRO_NET";
			break;
		case ETHERNET:
			datalink=ETHERNET;
			datalakink_str="ETHERNET";
			break;
		case HDLC:
			datalink=HDLC;
			datalakink_str="HDLC";
			break;
		case CHARACTER_SYNCHRONOUS:
			datalink=CHARACTER_SYNCHRONOUS;
			datalakink_str="CHARACTER_SYNCHRONOUS";
			break;
		case IBM_CHANNEL_TO_CHANNEL:
			datalink=IBM_CHANNEL_TO_CHANNEL;
			datalakink_str="IBM_CHANNEL_TO_CHANNEL";
			break;
		case FDDI:
			datalink=FDDI;
			datalakink_str="FDDI";
			break;
		case OTHER:
			datalink=OTHER;
			datalakink_str="OTHER";
			break;
		case HCI_UN_ENCAPSULATED:
			datalink=HCI_UN_ENCAPSULATED;
			datalakink_str="HCI_UN_ENCAPSULATED";
			break;
		case HCI_UART:
			datalink=HCI_UART;
			datalakink_str="HCI_UART";
			break;
		case HCI_BSCP:
			datalink=HCI_BSCP;
			datalakink_str="HCI_BSCP";
			break;
		case HCI_SERIAL:
			datalink=HCI_SERIAL;
			datalakink_str="HCI_SERIAL";
			break;
	}

}

/**
 * @brief
 *      print info in debug mode
 */
void BtSnoopFileInfo::printInfo(){

	cout << "version                   : " << version_number << endl;
	cout << "datalink                  : " << datalakink_str << endl;
	cout << "identification num        : " << identification_number<< endl;

}

BtSnoopFileInfo::~BtSnoopFileInfo(){
}

/**
 * @brief
 *      get identification number
 * @return
 */
std::string BtSnoopFileInfo::getIdentificationNumber(){
	return identification_number;
}

/**
 * @brief
 *      get version number
 * @return
 */
int BtSnoopFileInfo::getVersionNumber(){
	return version_number;
}

/**
 * @brief
 *     get datalink enum
 * @return
 */
datalink_type BtSnoopFileInfo::getDatalinkNumber(){
	return datalink;
}

/**
 * @brief
 *      get datalink name string
 * @return
 */
std::string BtSnoopFileInfo::getDatalinkStr(){
	return datalakink_str;
}
