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
	btsnoopfileinfo.h

	Parse bt snoop header

	@author Bertrand Martel
	@version 0.1
*/

#ifndef BTSNOOPFILEINFO_H
#define BTSNOOPFILEINFO_H

#include "vector"
#include "datalink.h"
#include "string"

class BtSnoopFileInfo
{

public:

	/**
	 * @brief
	 *      build file header
	 * @param data
	 *      file header data of size 16 (16 octet => 8 + 4 + 4)
	 */
	BtSnoopFileInfo(char* data);

	BtSnoopFileInfo();

	~BtSnoopFileInfo();

	/**
	 * @brief
	 *      get identification number
	 * @return
	 */
	std::string getIdentificationNumber();

	/**
	 * @brief
	 *      get version number
	 * @return
	 */
	int getVersionNumber();

	/**
	 * @brief
	 *     get datalink enum
	 * @return
	 */
	datalink_type getDatalinkNumber();

	/**
	 * @brief
	 *      get datalink name string
	 * @return
	 */
	std::string getDatalinkStr();

	/**
	 * @brief
	 *      print info in debug mode
	 */
	void printInfo();

private:

	/**
	 * @brief
	 *      used to identify the file as a snoop formatted file
	 */
	std::string identification_number;

	/**
	 * @brief
	 *      version of snoop used
	 */
	int version_number;

	/**
	 * @brief
	 *      type of datalink layer used in this current file
	 */
	datalink_type datalink;

	/**
	 * @brief
	 *      datalink type string
	 */
	std::string datalakink_str;

};

#endif // BTSNOOPFILEINFO_H
