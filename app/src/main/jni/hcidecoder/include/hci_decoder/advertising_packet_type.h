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
	advertising_packet_type.h
	list advertising packet type

	@author Bertrand Martel
	@version 1.0
*/
E(ADVERTIZING_TYPE_UNKNOWN                                   , 0x00)
E(ADVERTIZING_TYPE_FLAGS                                     , 0x01)
E(ADVERTIZING_TYPE_INCOMPLETE_LIST_16BIT_SERVICE_CLASS_UUID  , 0x02)
E(ADVERTIZING_TYPE_COMPLETE_LIST_16BIT_SERVICE_CLASS_UUID    , 0x03)
E(ADVERTIZING_TYPE_INCOMPLETE_LIST_32BIT_SERVICE_CLASS_UUID  , 0x04)
E(ADVERTIZING_TYPE_COMPLETE_LIST_32BIT_SERVICE_CLASS_UUID    , 0x05)
E(ADVERTIZING_TYPE_INCOMPLETE_LIST_128BIT_SERVICE_CLASS_UUID , 0x06)
E(ADVERTIZING_TYPE_COMPLETE_LIST_128BIT_SERVICE_CLASS_UUID   , 0x07)
E(ADVERTIZING_TYPE_SHORTENED_LOCAL_NAME                      , 0x08)
E(ADVERTIZING_TYPE_COMPLETE_LOCAL_NAME                       , 0x09)
E(ADVERTIZING_TYPE_TX_POWER_LEVEL                            , 0x0A)
E(ADVERTIZING_TYPE_CLASS_OF_DEVICE                           , 0x0D)
E(ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH_C                     , 0x0E)
E(ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER_R               , 0x0F)
E(ADVERTIZING_TYPE_DEVICE_ID                                 , 0x10)
E(ADVERTIZING_TYPE_SECURITY_MANAGER_OUT_OF_BAND_FLAGS        , 0x11)
E(ADVERTIZING_TYPE_SLAVE_CONNECTION_INTERVAL_RANGE           , 0x12)
E(ADVERTIZING_TYPE_LIST_16BIT_SOLICITATION_UUIDS             , 0x14)
E(ADVERTIZING_TYPE_LIST_32BIT_SOLICITATION_UUIDS             , 0x1F)
E(ADVERTIZING_TYPE_LIST_128BIT_SOLICITATION_UUIDS            , 0x15)
E(ADVERTIZING_TYPE_SERVICE_DATA_16BIT                        , 0x16)
E(ADVERTIZING_TYPE_SERVICE_DATA_32BIT                        , 0x20)
E(ADVERTIZING_TYPE_SERVICE_DATA_128BIT                       , 0x21)
E(ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_CONFIRMATION_VALUE  , 0x22)
E(ADVERTIZING_TYPE_LE_SECURE_CONNECTIONS_RANDOM_VALUE        , 0x23)
E(ADVERTIZING_TYPE_PUBLIC_TARGET_ADDRESS                     , 0x17)
E(ADVERTIZING_TYPE_RANDOM_TARGET_ADDRESS                     , 0x18)
E(ADVERTIZING_TYPE_APPEARANCE                                , 0x19)
E(ADVERTIZING_TYPE_ADVERTIZING_INTERVAL                      , 0x1A)
E(ADVERTIZING_TYPE_LE_BLUETOOTH_DEVICE_ADDRESS               , 0x1B)
E(ADVERTIZING_TYPE_LE_ROLE                                   , 0x1C)
E(ADVERTIZING_TYPE_SIMPLE_PAIRING_HASH                       , 0x1D)
E(ADVERTIZING_TYPE_SIMPLE_PAIRING_RANDOMIZER                 , 0x1E)
E(ADVERTIZING_TYPE_INFORMATION_DATA_3D                       , 0x3D)
E(ADVERTIZING_TYPE_MANUFACTURER_SPECIFIC_DATA                , 0xFF)
#undef E