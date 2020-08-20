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
	main.cpp

	Testing snoop straming decoding library

	@author Bertrand Martel
	@version 0.1
*/

#include <string>
#include "time.h"
#include "btsnoop/btsnoopparser.h"
#include <iostream>
#include "fstream"
#include <sys/param.h>
#include "btsnoopmonitor.h"
#include <unistd.h>
#include <signal.h>
#include "cstdlib"
#include "btsnoop/btsnooptask.h"

using namespace std;

void catch_signint(int sig){
	cout << "SIGINT received" << endl;
	exit(1);
}

int main(int argc, char *argv[])
{
	if (argc <= 1){
		cerr << "you must provide btsnoop file path " << endl;
		return -1;
	}

	struct sigaction sigIntHandler;

	sigIntHandler.sa_handler = catch_signint;
	sigemptyset(&sigIntHandler.sa_mask);
	sigIntHandler.sa_flags = 0;

	sigaction(SIGINT, &sigIntHandler, NULL);

	std::string recordFile(argv[1]);

	/*
	BtSnoopTask decoder(recordFile);

	bool decode_status = decoder.decode_file();

	decoder.getFileInfo().printInfo();

	cout << decoder.getPacketDataRecords().size() << endl;
	*/

	/*
	for (int i = 0; i  < decoder.getPacketDataRecords().size();i++){
		decoder.getPacketDataRecords()[i].printInfo();
	}
	*/

	BtSnoopParser parser;

	BtSnoopMonitor monitor;

	parser.addSnoopListener(&monitor);

	//parse streaming file
	bool success = parser.decode_streaming_file(recordFile,1500);

	if (!success)
		cerr << "file reading error occured" << endl;

	/*
	//waiting 2s
	usleep(2000000);

	//parse the same streaming file : this will stop previous decoding task and start a new one
	parser.decode_streaming_file(recordFile);

	//waiting 2s
	usleep(2000000);
	*/

	//wait indefinitly
	parser.join();
	
	return 0;
}
