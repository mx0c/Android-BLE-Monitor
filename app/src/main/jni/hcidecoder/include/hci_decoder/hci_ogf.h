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
	list of HCI Opcode Group Field for HCI Commands

	@author Bertrand Martel
	@version 1.0
*/
E(HCI_CMD_OGF_LINK_CONTROL_COMMANDS        , 0x01)
E(HCI_CMD_OGF_LINK_POLICY_COMMANDS         , 0x02)
E(HCI_CMD_OGF_CONTROLLER_BASEBAND_COMMANDS , 0x03)
E(HCI_CMD_OGF_INFORMATIONAL_PARAMETERS     , 0x04)
E(HCI_CMD_OGF_STATUS_PARAMETERS            , 0x05)
E(HCI_CMD_OGF_TESTING_COMMANDS             , 0x06)
E(HCI_CMD_OGF_LE_CONTROLLER_COMMANDS       , 0x08)
E(HCI_CMD_OGF_VENDOR_SPECIFIC              , 0x3F)
#undef E
