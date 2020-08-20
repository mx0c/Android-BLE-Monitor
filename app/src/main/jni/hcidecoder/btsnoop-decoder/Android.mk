LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -std=gnu++11
LOCAL_CFLAGS += -funwind-tables -Wl,--no-merge-exidx-entries
LOCAL_CPPFLAGS += -fexceptions

LOCAL_MODULE := btsnoopdecoder

LOCAL_C_INCLUDES := $NDK/sources/cxx-stl/gnu-libstdc++/4.8/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/include

LOCAL_SRC_FILES := src/btsnoopfileinfo.cpp \
	src/btsnooppacket.cpp \
	src/btsnoopparser.cpp \
	src/btsnooptask.cpp

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
