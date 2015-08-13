#include <jni.h>
#include <android/log.h>
#include <math.h>
#include <stdio.h>
#include <cpu-features.h>

#include <string>

#include "Blender.h"

using namespace std;

#define LOG_TAG "SimpleJni"
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
    jbyteArray* arr = reinterpret_cast<jbyteArray*>(&mvdata);


    // Get the elements (you probably have to fetch the length of the array as well
	jboolean isCopy = true;
    void* data = env->GetPrimitiveArrayCritical(*arr, &isCopy);

    int length = (int)env->GetArrayLength(*arr);

    // do something

    Blender b;

    b.process(data, length, blendFunction);


    env->ReleasePrimitiveArrayCritical(*arr, data, 0);


    LOGI("WELCOME TO NDK! Copy: %s, Length: %d", isCopy ? "true" : "false", length);
    //return imageData;
}


//// Cast it to a jbyteArray
//jbyteArray* arr = reinterpret_cast<jbyteArray*>(&mvdata);
//
//jboolean isCopy = false;
//
//// Get the elements (you probably have to fetch the length of the array as well
//// UNSAFE!!!
//jbyte* data = (*env)->GetPrimitiveArrayCritical(env, array, &isCopy);
//
//jint length = GetArrayLength(env, arr);
//
//// do something
//
//
//env->ReleaseByteArrayElements(*arr, data, 0);
//
//
//LOGI("WELCOME TO NDK! Copy: %s, Length: %d", isCopy ? "true" : "false", length);
////return imageData;

/*
JNIEXPORT jboolean JNICALL Java_com_example_Camera_NativeUtils_nativeIsPrime(
		JNIEnv *env, jobject obj, jint candidate) {
	LOGI("JNI: nativeIsPrime called");
	if (candidate < 2)
		return JNI_FALSE;

	double srt = sqrt((float) candidate);
	int lowerSrt = (int) floor(srt);

	//char *log = new char[64];

	//sprintf(log, "JNI: sqrt=%.4f floor=%d", srt, lowerSrt);
	//LOGI(log);
	//delete[] log;

	for (int i = 2; i <= lowerSrt; i++) {
		if (candidate % i == 0)
			return JNI_FALSE;
	}

	return JNI_TRUE;
}

JNIEXPORT jstring JNICALL Java_com_example_Camera_NativeUtils_getCpuInfo(JNIEnv* env,
		jobject obj) {

	string a;
	a.append("[JNI]: ");
	uint64_t cpu_features;

	if (android_getCpuFamily() != ANDROID_CPU_FAMILY_ARM) {
		return env->NewStringUTF("Not ARM");
	}

	cpu_features = android_getCpuFeatures();
	if (cpu_features & ANDROID_CPU_ARM_FEATURE_ARMv7) {
		a.append(" ARMc7 \n");
		LOGI("Arm7");
	}
	if (cpu_features & ANDROID_CPU_ARM_FEATURE_VFPv3) {
		a.append(" ARM w VFPv3 support \n");
		LOGI("VFP3");
	}
	if ((cpu_features & ANDROID_CPU_ARM_FEATURE_NEON)) {
		a.append(" ARM w NEON support \n");
		LOGI("NEON");
	}

	if (cpu_features & ANDROID_CPU_ARM_FEATURE_LDREX_STREX) {
		a.append(" LDREX_STREX ");
		LOGI("LDREX_STREX");
	}

	if (a == "") {
		return env->NewStringUTF("Unknown");
	}

	return env->NewStringUTF(a.c_str());
}

JNIEXPORT void JNICALL Java_com_example_Camera_NativeUtils_increment(JNIEnv* env,
		jobject obj)
{
    increment_int_field_by_name(env, obj, "var", 3);
}

*/

#ifdef __cplusplus
}
#endif
