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
	btsnoopmonitor.h

	Monitoring implementation of ibtsnooplistener

	@author Bertrand Martel
	@version 0.1
*/

#ifndef BTSNOOPMONITOR_H
#define BTSNOOPMONITOR_H

#include "btsnoop/ibtsnooplistener.h"
#include "btsnoop/btsnooppacket.h"
#include "btsnoop/btsnoopfileinfo.h"

class BtSnoopMonitor : public IBtSnoopListener
{

public:

	BtSnoopMonitor();

	~BtSnoopMonitor();


	/**
	 * @brief onSnoopPacketReceived
	 *      called when a new packet record has been received
	 * @param fileInfo
	 *      file info object
	 * @param packet
	 *      snoop packet record object
	 */
	void onSnoopPacketReceived(BtSnoopFileInfo fileInfo,BtSnoopPacket packet);

	/**
	 * @brief
	 * 		called when packet counting is completed
	 * @param packet_count
	 *      total packet count
	 */
	void onFinishedCountingPackets(int packet_count);

	/**
	 * @brief
	 * 		called when and error occured
	 * @param error_code
	 *      error code
	 * @param error_message
	 *      error message
	 */
	void onError(int error_code,std::string error_message);

	int count;
};

#endif // BTSNOOPMONITOR_H
