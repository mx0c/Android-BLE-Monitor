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
	hci_ogf.h
	list of HCI LE SubEvent

	@author Bertrand Martel
	@version 1.0
*/
E(HCI_EVENT_LE_CONNECTION_COMPLETE                 , 0x01)
E(HCI_EVENT_LE_ADVERTISING_REPORT                  , 0x02)
E(HCI_EVENT_LE_CONNECTION_UPDATE_COMPLETE          , 0x03)
E(HCI_EVENT_LE_READ_REMOTE_USED_FEATURES_COMPLETE  , 0x04)
E(HCI_EVENT_LE_LONG_TERM_KEY_REQUEST               , 0x05)
E(HCI_EVENT_LE_REMOTE_CONNECTION_PARAMETER_REQUEST , 0x06)
E(HCI_EVENT_LE_DATA_LENGTH_CHANGE                  , 0x07)
E(HCI_EVENT_LE_READ_LOCAL_P256_PUBLIC_KEY_COMPLETE , 0x08)
E(HCI_EVENT_LE_GENERATE_DHKEY_COMPLETE             , 0x09)
E(HCI_EVENT_LE_ENHANCED_CONNECTION_COMPLETE        , 0x0A)
E(HCI_EVENT_LE_DIRECT_ADVERTISING_REPORT           , 0x0B)
#undef E
