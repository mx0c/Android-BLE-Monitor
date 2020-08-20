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
	btsnoopparsert.h

	Parse and manage task for streaming bt snoop file

	@author Bertrand Martel
	@version 0.1
*/

#ifndef BTSNOOPPARSER_H
#define BTSNOOPPARSER_H

#include "btsnoopstate.h"
#include "fstream"
#include "vector"
#include "ibtsnooplistener.h"
#include "btsnoopfileinfo.h"
#include "pthread.h"
#include "btsnoop/btsnooptask.h"

class BtSnoopParser
{

public:

	/**
	 * @brief
	 *      initialize bt snoop file parser
	 */
	BtSnoopParser();

	/**
	 * stop and join thread
	 **/
	~BtSnoopParser();

	/**
	 * @brief
	 *      add a listener to monitor streamed packet record
	 * @param listener
	 */
	void addSnoopListener(IBtSnoopListener* listener);

	/**
	 * @brief
	 *      remove all listeners in snoop listener list
	 */
	 void clearListeners();

	/**
	 * @brief
	 *      decode streaming file
	 * @param file_path
	 * @return
	 *      success status
	 */
	bool decode_streaming_file(std::string file_path);

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
	bool decode_streaming_file(std::string file_path, int packetNumber);

	/**
	 * @brief
	 *      wait for thread to finish (blocking method)
	 */
	void join();

	/**
	 * @brief
	 *      stop and join current thread
	 */
	void stop();

private:

	/**
	 * @brief
	 *      decode thread task
	 */
	pthread_t decode_task;

	/**
	 * @brief
	 *      parser object manager
	 */
	BtSnoopTask snoop_task;

	/**
	 * @brief
	 *      list of listener registered
	 */
	std::vector<IBtSnoopListener*> snoopListenerList;

	/**
	 * @brief
	 *      define if a thread has already been created before
	 */
	bool thread_started;
};

#endif // BTSNOOPPARSER_H
