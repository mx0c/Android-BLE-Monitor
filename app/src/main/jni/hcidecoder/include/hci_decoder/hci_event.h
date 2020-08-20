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
	hci_event.h
	list of supported HCI Event

	@author Bertrand Martel
	@version 1.0
*/
E(HCI_EVENT_DISCONNECTION_COMPLETE                      , 0x05 )
E(HCI_EVENT_REMOTE_NAME_REQUEST_COMPLETE                , 0x07 )
E(HCI_EVENT_NUMBER_OF_COMPLETED_PACKET                  , 0x13 )
E(HCI_EVENT_COMMAND_COMPLETE                            , 0x0E )
E(HCI_EVENT_EXTENDED_INQUIRY_RESULT                     , 0x2F )
E(HCI_EVENT_INQUIRY_COMPLETE                            , 0x01 )
E(HCI_EVENT_COMMAND_STATUS                              , 0x0F )
E(HCI_EVENT_REMOTE_HOST_SUPPORTED_FEATURES_NOTIFICATION , 0x3D )
E(HCI_EVENT_LE_META                                     , 0x3E )
#undef E
