LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

# имя модуля, который будет вызываться в Java при помощи System.loadLibrary()
LOCAL_MODULE    := rosetta

# Add all source file names to be included in lib separated by a whitespace
LOCAL_SRC_FILES := com_example_Camera_NativeUtils.cpp

#FILE_LIST := $(wildcard $(LOCAL_PATH)/src/*.cpp)
#LOCAL_SRC_FILES := $(FILE_LIST:$(LOCAL_PATH)/%=%)

# статические библиотеки, уже скомпиленные за нас
#LOCAL_STATIC_LIBRARIES := cpufeatures

# добавим библиотеку для логирования
LOCAL_LDLIBS := -llog

LOCAL_CFLAGS := -DNDEBUG

include $(BUILD_SHARED_LIBRARY)

# 
$(call import-module,cpufeatures)
