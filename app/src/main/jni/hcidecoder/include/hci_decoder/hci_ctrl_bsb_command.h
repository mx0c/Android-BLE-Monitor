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
	hci_ctrl_bsb_command.h
	list of supported HCI Command Control/Baseband 

	@author Bertrand Martel
	@version 1.0
*/
E(HCI_CMD_OCF_CTRL_BSB_RESET_COMMAND                               ,0x0003 )
E(HCI_CMD_OCF_CTRL_BSB_SET_EVENT_FILTER_COMMAND                    ,0x0005 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_LOCAL_NAME_COMMAND                    ,0x0013 )
E(HCI_CMD_OCF_CTRL_BSB_READ_LOCAL_NAME_COMMAND                     ,0x0014 )
E(HCI_CMD_OCF_CTRL_BSB_READ_CLASS_OF_DEVICE_COMMAND                ,0x0023 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_CLASS_OF_DEVICE_COMMAND               ,0x0024 )
E(HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_MODE_COMMAND                   ,0x0044 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_MODE_COMMAND                  ,0x0045 )
E(HCI_CMD_OCF_CTRL_BSB_READ_LE_HOST_SUPPORT_COMMAND                ,0x006C )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_LE_HOST_SUPPORT_COMMAND               ,0x006D )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_ACTIVITY_COMMAND         ,0x001E )
E(HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_SCAN_ACTIVITY_COMMAND          ,0x001D )
E(HCI_CMD_OCF_CTRL_BSB_READ_VOICE_SETTING_COMMAND                  ,0x0025 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_VOICE_SETTING_COMMAND                 ,0x0026 )
E(HCI_CMD_OCF_CTRL_BSB_READ_CURRENT_IAC_LAP_COMMAND                ,0x0039 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_CURRENT_IAC_LAP_COMMAND               ,0x003A )
E(HCI_CMD_OCF_CTRL_BSB_READ_PAGE_SCAN_TYPE_COMMAND                 ,0x0046 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_SCAN_TYPE_COMMAND                ,0x0047 )
E(HCI_CMD_OCF_CTRL_BSB_READ_SIMPLE_PAIRING_MODE_COMMAND            ,0x0055 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_SIMPLE_PAIRING_MODE_COMMAND           ,0x0056 )
E(HCI_CMD_OCF_CTRL_BSB_READ_EXTENDED_INQUIRY_RESPONSE_COMMAND      ,0x0051 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_EXTENDED_INQUIRY_RESPONSE_COMMAND     ,0x0052 )
E(HCI_CMD_OCF_CTRL_BSB_READ_SCAN_ENABLE_COMMAND                    ,0x0019 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_SCAN_ENABLE_COMMAND                   ,0x001A )
E(HCI_CMD_OCF_CTRL_BSB_READ_INQUIRY_SCAN_TYPE_COMMAND              ,0x0042 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_INQUIRY_SCAN_TYPE_COMMAND             ,0x0043 )
E(HCI_CMD_OCF_CTRL_BSB_SET_EVENT_MASK_COMMAND                      ,0x0001 )
E(HCI_CMD_OCF_CTRL_BSB_READ_PAGE_TIMEOUT_COMMAND                   ,0x0017 )
E(HCI_CMD_OCF_CTRL_BSB_WRITE_PAGE_TIMEOUT_COMMAND                  ,0x0018 )
E(HCI_CMD_OCF_CTRL_BSB_HOST_BUFFER_SIZE_COMMAND                    ,0x0033 )
#undef E