/**
 * @mainpage
 * 
 * <h3>Lightweight Bluetooth HCI decoder library parsing individually HCI frames into JSON format</h3>
 * 
 * <h4>Setup & Build</h4>
 * 
 * <p>
 * <ul>
 * <li>git submodule init</li>
 * <li>git submodule update</li>
 * <li>cmake .</li>
 * <li>make</li>
 * </ul>
 * </p>
 * <hr/>
 * library release is located under lib directory.
 * 
 */

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
	hcidecoder.h
	HCI decoder

	@author Bertrand Martel
	@version 1.0
*/
#ifndef HCIDECODER_H
#define HCIDECODER_H

#include "vector"
#include "hci_decoder/IHciFrame.h"

class HciDecoder
{

public:

	HciDecoder();

	~HciDecoder();

	/**
	 * @brief
	 *      decode one HCI frame
	 * @param data
	 *      data to be parsed (length can be > packet size)
	 * @return
	 *      Hci frame object decoded or null pointer if not parsed correctly
	 */
	IHciFrame* decode(std::vector<char> data);

	/**
	 * @brief
	 *      get all decoded packet
	 * @return
	 *      all decoded packet
	 */
	std::vector<IHciFrame*> getFrameList();

	/**
	 * @brief
	 *      convert all decoded frame to json
	 * @param beautify
	 *      add indentation + linefeed
	 * @return
	 *      all decoded packet in json format
	 */
	std::string toJson(bool beautify);

private:
	
	/*list of frame decoded with this decoder*/
	std::vector<IHciFrame*> frame_list;

};

#endif // HCIDECODER_H
