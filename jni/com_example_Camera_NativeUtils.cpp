#include <jni.h>
#include <android/log.h>
#include <math.h>
#include <stdio.h>

#include "Core.h"
#include "Blender.h"

using namespace std;

#define LOG_TAG "Vizor Vision"
#define LOGI(x...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,x)

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_com_example_Camera_NativeUtils_blendWithColor
        (JNIEnv *env, jclass klass, jobject bitmap, jint imageWidth, jint imageHeight, jint blendFunction)
{
    // fetch the class
    jclass bitmapClass = env->GetObjectClass(bitmap);

    // get field id
    jfieldID aId = env->GetFieldID(bitmapClass, "mBuffer", "[B");

    // get object from the field
    // Get the object field, returns JObject (because Array is instance of Object)
    jobject mvdata = env->GetObjectField (bitmap, aId);

    // Cast it to a jbyteArray
    jbyteArray* arr = (jbyteArray*)(&mvdata);


    // Get the elements (you probably have to fetch the length of the array as well
	jboolean isCopy = true;
    void* data = env->GetPrimitiveArrayCritical(*arr, &isCopy);

	if(!isCopy)
	{
		int length = (int)env->GetArrayLength(*arr);
		blender_Process(data, length, blendFunction);
	}
	else
	{
		LOGI("Error: GetPrimitiveArrayCritical returned copy, unhandled situation!");
	}

	env->ReleasePrimitiveArrayCritical(*arr, data, 0);
}

#ifdef __cplusplus
}
#endif
