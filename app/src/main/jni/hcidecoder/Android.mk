LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_CFLAGS := -std=gnu++11
LOCAL_CPPFLAGS += -fexceptions

LOCAL_MODULE := hcidecoder

HCI_DECODER_PATH := src

LOCAL_C_INCLUDES := $(LOCAL_PATH)/${HCI_DECODER_PATH}/include
LOCAL_C_INCLUDES += $NDK/sources/cxx-stl/gnu-libstdc++/4.8/include
LOCAL_C_INCLUDES += $(LOCAL_PATH)/${HCI_DECODER_PATH}/btsnoop-decoder/include

LOCAL_SRC_FILES := $(HCI_DECODER_PATH)/jsoncpp.cpp \
	$(HCI_DECODER_PATH)/hcidecoder.cpp \
	$(HCI_DECODER_PATH)/command_complete.cpp

include $(BUILD_SHARED_LIBRARY)