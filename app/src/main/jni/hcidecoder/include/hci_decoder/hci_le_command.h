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
	hci_le_command.h
	list of supported HCI LE Command

	@author Bertrand Martel
	@version 1.0
*/
E(HCI_CMD_OCF_LE_SET_SCAN_PARAMETERS_COMMAND                          ,0x000B )
E(HCI_CMD_OCF_LE_SET_ADVERTISING_PARAMETERS_COMMAND                   ,0x0006 )
E(HCI_CMD_OCF_LE_SET_SCAN_ENABLE_COMMAND                              ,0x000C )
E(HCI_CMD_OCF_LE_CREATE_CONNECTION_COMMAND                            ,0x000D )
E(HCI_CMD_OCF_LE_CLEAR_WHITE_LIST_COMMAND                             ,0x0010 )
E(HCI_CMD_OCF_LE_READ_REMOTE_USED_FEATURES_COMMAND                    ,0x0016 )
E(HCI_CMD_OCF_LE_RAND_COMMAND                                         ,0x0018 )
E(HCI_CMD_OCF_LE_READ_WHITE_LIST_SIZE_COMMAND                         ,0x000F )
E(HCI_CMD_OCF_LE_SET_ADVERTISING_DATA_COMMAND                         ,0x0008 )
E(HCI_CMD_OCF_LE_READ_BUFFER_SIZE_COMMAND                             ,0x0002 )

E(HCI_CMD_OCF_LE_SET_EVENT_MASK_COMMAND                               ,0x0001 )
E(HCI_CMD_OCF_LE_SET_RANDOM_ADDRESS_COMMAND                           ,0x0005 )
E(HCI_CMD_OCF_LE_REMOVE_DEVICE_FROM_RESOLVING_LIST_COMMAND            ,0x001C )

E(HCI_CMD_OCF_LE_READ_LOCAL_SUPPORTED_FEATURES_COMMAND ,0x0003 )
#undef E



