#include <jni.h>
#include <android/log.h>
#include <math.h>
#include <stdio.h>
#include <string.h>

#include "Core.h"
#include "Blender.h"

using namespace std;

#define LOG_TAG "Vizor Vision"
#define LOGI(x...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,x)

#ifdef __cplusplus
extern "C" {
#endif


JNIEXPORT void JNICALL Java_com_example_Camera_NativeUtils_blendWithColor
        (JNIEnv *env, jclass klass, jobject bitmap, jint blendFunction, jint intColor, jfloat opacity)
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
		const u32 length = (u32)env->GetArrayLength(*arr);
		blender_Process(data, length, blendFunction, intColor, opacity);
	}
	else
	{
		LOGI("Error: GetPrimitiveArrayCritical returned copy, unhandled situation!");
	}

	env->ReleasePrimitiveArrayCritical(*arr, data, 0);
}

JNIEXPORT void JNICALL Java_com_example_Camera_NativeUtils_blendWithImage
    (JNIEnv *env, jclass klass, jobject bitmap1, jobject bitmap2)
{
    // fetch the class
    jclass bitmapClass = env->GetObjectClass(bitmap1);

    // get field id
    jfieldID aId = env->GetFieldID(bitmapClass, "mBuffer", "[B");

    // get object from the field
    // Get the object field, returns JObject (because Array is instance of Object)
    jobject mvdata1 = env->GetObjectField (bitmap1, aId);
    jobject mvdata2 = env->GetObjectField (bitmap2, aId);

    // Cast it to a jbyteArray
    jbyteArray* arr1 = (jbyteArray*)(&mvdata1);
    jbyteArray* arr2 = (jbyteArray*)(&mvdata2);


    // Get the elements (you probably have to fetch the length of the array as well
    jboolean isCopy = true;

    void* data1 = env->GetPrimitiveArrayCritical(*arr1, &isCopy);
    void* data2 = env->GetPrimitiveArrayCritical(*arr2, &isCopy);

    if(!isCopy)
    {
        const u32 length1 = (u32)env->GetArrayLength(*arr1);
        const u32 length2 = (u32)env->GetArrayLength(*arr1);

        blender_blendImage(data1, data2, length1);
    }
    else
    {
        LOGI("Error: GetPrimitiveArrayCritical returned copy, unhandled situation!");
    }

    env->ReleasePrimitiveArrayCritical(*arr1, data1, 0);
    env->ReleasePrimitiveArrayCritical(*arr2, data2, 0);
}

JNIEXPORT void JNICALL Java_com_example_Camera_NativeUtils_makeGrayscale
	(JNIEnv *env, jclass klass, jobject bitmap)
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
		const u32 length = (u32)env->GetArrayLength(*arr);
		blender_fastGrayscale(data, length);
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
