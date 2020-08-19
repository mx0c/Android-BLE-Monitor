//
// Created by marius on 19.08.2020.
//

#include "btsnoop/btsnoopparser.h"
#include "btsnoop/btsnooptask.h"
#include "btsnoopmonitor.h"
#include "android/log.h"
#include <jni.h>

using namespace std;

static BtSnoopParser parser;
static BtSnoopMonitor monitor;

extern "C" {
    JNIEXPORT void JNICALL Java_com_huc_android_1ble_1monitor_util_HciSnoopLog_startHciLogStream(JNIEnv* env, jobject obj,jstring filePath,jint lastPacketCount){
        jclass g_clazz = env->GetObjectClass(monitor.jobj);
        if (g_clazz == NULL) {
        __android_log_print(ANDROID_LOG_ERROR,"startHciLogStream","Failed to find class\n");
        }

        monitor.mid = env->GetMethodID(g_clazz, "onHciFrameReceived", "(Ljava/lang/String;Ljava/lang/String;)V");
        if (monitor.mid == NULL) {
        __android_log_print(ANDROID_LOG_ERROR,"startHciLogStream","Unable to get method ref\n");
        }

        monitor.mid_counting= env->GetMethodID(g_clazz, "onFinishedPacketCount", "(I)V");
        if (monitor.mid_counting == NULL) {
        __android_log_print(ANDROID_LOG_ERROR,"startHciLogStream","Unable to get method ref\n");
        }

        monitor.mid_error= env->GetMethodID(g_clazz, "onError", "(ILjava/lang/String;)V");
        if (monitor.mid_error == NULL) {
        __android_log_print(ANDROID_LOG_ERROR,"startHciLogStream","Unable to get method ref\n");
        }

        parser.addSnoopListener(&monitor);

        const char* filePathConvert = env->GetStringUTFChars(filePath, 0);

        string filePathStr = filePathConvert;

        __android_log_print(ANDROID_LOG_VERBOSE,"startHciLogStream","opening HCI log file in %s\n",filePathStr.data());

        bool success = parser.decode_streaming_file(filePathStr,lastPacketCount);

        if (success){
        //success
        __android_log_print(ANDROID_LOG_VERBOSE,"startHciLogStream","file opened successfully");
        }
        else{
        //failure (bad reading / file not found)
        __android_log_print(ANDROID_LOG_VERBOSE,"startHciLogStream","fail to open file");
        }

        env->ReleaseStringUTFChars(filePath, filePathConvert);
    }
}

extern "C" {
    JNIEXPORT void JNICALL Java_com_huc_android_1ble_1monitor_util_HciSnoopLog_stopHciLogStream(JNIEnv* env, jobject obj)
    {
        __android_log_print(ANDROID_LOG_VERBOSE,"stopHciLogStream","stopping thread\n");
        parser.stop();
        parser.clearListeners();
    }
}

extern "C" {
    JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* aReserved)
    {
        BtSnoopTask::jvm = vm;
        return JNI_VERSION_1_6;
    }
}