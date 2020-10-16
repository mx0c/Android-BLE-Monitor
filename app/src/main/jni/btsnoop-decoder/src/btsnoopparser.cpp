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
	btsnoopparsert.cpp

	Parse and manage task for streaming bt snoop file

	@author Bertrand Martel
	@version 0.1
*/

#include "btsnoop/btsnoopparser.h"
#include "iostream"
#include "btsnoop/ibtsnooplistener.h"
#include <pthread.h>
#include "btsnoop/btsnooptask.h"

using namespace std;

/**
 * @brief
 *      initialize bt snoop file parser
 */
BtSnoopParser::BtSnoopParser() {

	thread_started=false;

}

/**
 * @brief
 * 		stop and join thread
 **/
BtSnoopParser::~BtSnoopParser(){

	snoop_task.stop();

	if (thread_started)
		(void)pthread_join(decode_task,NULL);
}

/**
 * @brief
 *      add a listener to monitor streamed packet record
 * @param listener
 */
void BtSnoopParser::addSnoopListener(IBtSnoopListener* listener){

	snoopListenerList.push_back(listener);

}

/**
 * @brief
 *      remove all listeners in snoop listener list
 */
void BtSnoopParser::clearListeners(){
	snoopListenerList.clear();
}

/**
 * @brief
 *      wait for thread to finish (blocking method)
 */
void BtSnoopParser::join(){

	if (thread_started)
		(void)pthread_join(decode_task,NULL);
}

/**
 * @brief
 *      wait for thread to finish (blocking method)
 */
void BtSnoopParser::stop(){
	thread_started = false;
	snoop_task.stop();
	join();
}

/**
 * @brief
 *      decode streaming file
 * @param file_path
 *      btsnoop file path
 * @return
 *      success status
 */
bool BtSnoopParser::decode_streaming_file(std::string file_path){

	snoop_task.stop();

	if (thread_started)
		(void)pthread_join(decode_task,NULL);

	snoop_task= BtSnoopTask(file_path,&snoopListenerList);

	int rc = pthread_create(&decode_task, NULL,&BtSnoopTask::decoding_helper,(void*)&snoop_task);

	if (rc){
		cerr << "Error:unable to create thread," << rc << endl;
		return false;
	}
	else{
		thread_started=true;
	}

	return true;
}

/**
 * @brief
 *      decode streaming file with a fixed number of decoded packet (from the end of the files to the beginning)
 * @param file_path
 *      btsnoop file path
 * @param packetNumber
 *      number of packet to decoded (from the end to the beginning)
 * @return
 *      success status
 */
bool BtSnoopParser::decode_streaming_file(std::string file_path, int packetNumber){

	snoop_task.stop();

	if (thread_started)
		(void)pthread_join(decode_task,NULL);

	snoop_task= BtSnoopTask(file_path,&snoopListenerList,packetNumber);

	int rc = pthread_create(&decode_task, NULL,&BtSnoopTask::decoding_helper,(void*)&snoop_task);

	if (rc){
		cerr << "Error:unable to create thread," << rc << endl;
		return false;
	}
	else{
		thread_started=true;
	}

	return true;
}