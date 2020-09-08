/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Bertrand Martel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/**
	btsnoopmonitor.cpp

	Monitoring implementation of ibtsnooplistener

	@author Bertrand Martel
	@version 0.1
*/

#include "btsnoopmonitor.h"
#include "btsnoop/ibtsnooplistener.h"
#include "btsnoop/btsnoopfileinfo.h"
#include "hci_decoder/IHciFrame.h"
#include "btsnoop/btsnooptask.h"

#include "jni.h"
#include <android/log.h>

using namespace std;

BtSnoopMonitor::BtSnoopMonitor()
{
}

BtSnoopMonitor::~BtSnoopMonitor(){
}

/**
 * @brief
 * 		called when packet counting is completed
 * @param packet_count
 * 		total packet number
 */
void BtSnoopMonitor::onFinishedCountingPackets(int packet_count,JNIEnv * jni_env){

	if (jni_env!=0){
		if (jobj!=0 && mid_counting!=0){

			jni_env->CallVoidMethod(jobj, mid_counting,packet_count);

			if (jni_env->ExceptionCheck()) {
				jni_env->ExceptionDescribe();
			}
		}
		else{
			__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","class or method if not defined\n");
		}
	}
	else{
		__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","jni_env is not defined\n");
	}
}

/**
 * @brief onSnoopPacketReceived
 *      called when a new packet record has been received
 * @param fileInfo
 *      file info object
 * @param packet
 *      snoop packet record object
 * @param jni_env
 *      JNI env object
 */
void BtSnoopMonitor::onSnoopPacketReceived(BtSnoopFileInfo fileInfo,BtSnoopPacket packet,JNIEnv * jni_env){

	IHciFrame * frame = hci_decoder.decode(packet.getPacketData());

	if (frame!=0){

		if (jni_env!=0){
			if (jobj!=0 && mid!=0){

				jstring hci_frame = jni_env->NewStringUTF(frame->toJson(false).data());

				jstring snoop_frame = jni_env->NewStringUTF(packet.toJson(false).data());

				jni_env->CallVoidMethod(jobj, mid, snoop_frame,hci_frame);

				jni_env->DeleteLocalRef(hci_frame);
				jni_env->DeleteLocalRef(snoop_frame);

				if (jni_env->ExceptionCheck()) {
					jni_env->ExceptionDescribe();
				}
			}
			else{
				__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","class or method if not defined\n");
			}
		}
		else{
			__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","jni_env is not defined\n");
		}
	}else{
		__android_log_print(ANDROID_LOG_INFO,"hci-debugger","frame not treated");
	}
}

/**
 * @brief
 * 		called when and error occured
 * @param error_code
 *      error code
 * @param error_message
 *      error message
 * @param jni_env
 *      JNI env object
 */
void BtSnoopMonitor::onError(int error_code,std::string error_message, JNIEnv * jni_env){

	if (jni_env!=0){
		if (jobj!=0 && mid_counting!=0){

			jstring error_jmessage = jni_env->NewStringUTF(error_message.data());

			jni_env->CallVoidMethod(jobj, mid_error,error_code,error_jmessage);

			jni_env->DeleteLocalRef(error_jmessage);

			if (jni_env->ExceptionCheck()) {
				jni_env->ExceptionDescribe();
			}
		}
		else{
			__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","class or method if not defined\n");
		}
	}
	else{
		__android_log_print(ANDROID_LOG_ERROR,"hci-debugger","jni_env is not defined\n");
	}
}