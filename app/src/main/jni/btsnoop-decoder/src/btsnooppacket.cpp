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
	btsnooppacket.cpp

	Parse bt snoop packet record

	@author Bertrand Martel
	@version 0.1
*/
#include "btsnoop/btsnooppacket.h"
#include "iostream"
#include "stdio.h"
#include <inttypes.h>
#include <stdlib.h>
#include <json/json.h>

#ifdef __ANDROID__

#include "android/log.h"

#endif

#define DATE_0AD_TO_YEAR2000 0x00E03AB44A676000

using namespace std;

BtSnoopPacket::BtSnoopPacket(){
}

/**
 * @brief
 *      build snoop packet
 * @param data
 *      data of size 24 (4 + 4 + 4 + 4 + 8)
 */
BtSnoopPacket::BtSnoopPacket(char * data){

	original_length=0;
	included_length=0;
	cumulative_drops=0;
	packet_received=false;
	packet_sent=false;
	packet_type_command_event=false;
	packet_type_data=false;

	int packet_flags = 0;

	for (int i = 0;i<4;i++){
		original_length+=((data[i] & 0xFF) << (3-(i-0))*8);
	}
	for (int i = 4;i<8;i++){
		included_length+=((data[i] & 0xFF) << (3-(i-4))*8);
	}
	for (int i = 8;i<12;i++){
		packet_flags+=   ((data[i] & 0xFF) << (3-(i-8))*8);
	}
	for (int i = 12;i<16;i++){
		cumulative_drops+=((data[i] & 0xFF) << (3-(i-12))*8);
	}

	if ((packet_flags & 0x00000001)==1){
		packet_received=true;
	}
	else{
		packet_sent=true;
	}

	if ((packet_flags & 0x00000002)==1){
		packet_type_command_event=true;
	}
	else{
		packet_type_data=true;
	}

	//this is timestamp in microseconds since 01/01/0 AD
	uint64_t timestamp_ad=0;

	for (int i = 16;i<24;i++){
		timestamp_ad+=(((uint64_t)(data[i] & 0xFF)) << (7-(i-16))*8);
	}

	uint64_t date_between_record_date_year2000=timestamp_ad-DATE_0AD_TO_YEAR2000;

	struct tm t;
	t.tm_year = 100;
	t.tm_mon = 0;
	t.tm_mday = 1;
	t.tm_hour = 0;
	t.tm_min = 0;
	t.tm_sec = 0;
	t.tm_isdst=0;

	//save and set TZ beforme mktime
	char *tz;
	tz = getenv("TZ");
	setenv("TZ", "", 1);
	tzset();

	uint64_t timeSinceEpoch = (uint64_t)mktime(&t);

	//set TZ to previous value
	if (tz)
		setenv("TZ", tz, 1);
	else
		unsetenv("TZ");
	tzset();

	timestamp_microseconds=date_between_record_date_year2000+timeSinceEpoch*1000000;
}

/**
 * @brief
 *      print info in debug mode
 */
void BtSnoopPacket::printInfo(){

	#ifdef __ANDROID__

	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","original length           : %d\n",original_length);
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","included length           : %d\n",included_length);
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","cumulative drops          : %d\n",cumulative_drops);
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","packet_received           : %d\n",packet_received );
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","packet_sent               : %d\n",packet_sent);
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","packet_type_command_event : %d\n",packet_type_command_event );
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","packet_type_data          : %d\n",packet_type_data);
	__android_log_print(ANDROID_LOG_VERBOSE,"snoop packet","--------------------------\n");

	#else

	cout << "original length           : " << original_length << endl;
	cout << "included length           : " << included_length << endl;
	cout << "cumulative drops          : " << cumulative_drops << endl;
	cout << "packet_received           : " << packet_received << endl;
	cout << "packet_sent               : " << packet_sent << endl;
	cout << "packet_type_command_event : " << packet_type_command_event << endl;
	cout << "packet_type_data          : " << packet_type_data << endl;
	cout << "timestamp unix microsec   : " << timestamp_microseconds << endl;
	cout << "data                      : ";

	for (int i = 0; i  < included_length;i++){
		printf("%02X ",(packet_data[i] & 0xFF));
	}
	cout << endl;
	cout << "--------------------------" << endl;

	#endif
}

/**
 * @brief
 *      convert packet class to json
 * @param beautify beautify/uglify json
 */
std::string BtSnoopPacket::toJson(bool beautify){

	Json::Value output;

	output["original_length"] = original_length;
	output["included_length"] = included_length;
	output["cumulative_drops"] = cumulative_drops;
	output["packet_received"] = packet_received;
	output["packet_sent"] = packet_sent;
	output["packet_type_command_event"] = packet_type_command_event;
	output["packet_type_data"] = packet_type_data;
	output["timestamp_microseconds"] = (double)timestamp_microseconds;

	Json::Value packet_data_vals(Json::arrayValue);
	for (int i = 0; i  < included_length;i++){
		packet_data_vals.append((packet_data[i] & 0xFF));
	}
	output["packet_data"]=packet_data_vals;

	if (!beautify){
		Json::StreamWriterBuilder builder;
		builder.settings_["indentation"] = "";
		return Json::writeString(builder, output);
	}
	else{
		return output.toStyledString();
	}
}

BtSnoopPacket::~BtSnoopPacket(){
}

/**
 * @brief
 *      decode packet data field
 * @param data
 */
void BtSnoopPacket::decode_data(char * data){

	for (int i = 0; i  < included_length;i++){
		packet_data.push_back(data[i]);
	}
}

/**
 * @brief
 *      get length of original packet (could be more than this packet's length)
 * @return
 */
int BtSnoopPacket::getOriginalLength(){
	return original_length;
}

/**
 * @brief
 *      get packet data field length
 * @return
 */
int BtSnoopPacket::getincludedLength(){
	return included_length;
}

/**
 * @brief
 *      get number of packet lost between the first record and this record for this file
 * @return
 */
int BtSnoopPacket::getCumulativeDrops(){
	return cumulative_drops;
}

/**
 * @brief
 *       retrieve packet data
 * @return
 */
std::vector<char> BtSnoopPacket::getPacketData(){
	return packet_data;
}

/**
 * @brief
 *      get unix timestamp for this packet record
 * @return
 */
uint64_t BtSnoopPacket::getUnixTimestampMicroseconds(){
	return timestamp_microseconds;
}

/**
 * @brief
 *      define if packet record is sent
 * @return
 */
bool BtSnoopPacket::is_packet_sent(){
	return packet_sent;
}

/**
 * @brief
 *      define if packet record is received
 * @return
 */
bool BtSnoopPacket::is_packet_received(){
	return packet_received;
}

/**
 * @brief
 *      define if packet record is data record
 * @return
 */
bool BtSnoopPacket::is_data(){
	return packet_type_data;
}

/**
 * @brief
 *      define if packet record is command or event
 * @return
 */
bool BtSnoopPacket::is_command_event(){
	return packet_type_command_event;
}