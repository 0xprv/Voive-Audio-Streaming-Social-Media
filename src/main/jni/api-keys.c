#include <jni.h>

//For first API key
JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getAppId(JNIEnv *env, jobject instance) {
return (*env)-> NewStringUTF(env, "XX");
}
JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getserver(JNIEnv *env, jobject instance) {
    return (*env)-> NewStringUTF(env, "https://parseapi.back4app.com/");
}
JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getclient(JNIEnv *env, jobject instance) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_special_1speaker_1live_1table_getaudiokey(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_special_1speaker_1live_1table_getAudiocertificate(JNIEnv *env,
                                                                             jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_sound_1preview_getaudiokey(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_sound_1preview_getAudiocertificate(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}


JNIEXPORT jstring JNICALL
Java_com_voive_android_user_1QR_1code_getMapboxId(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "sk.eyJ1IjoicHJhbmF2dmlzaG5vaSIsImEiOiJjbDJpa282ZWswb2lxM3FwaHB0eW90azFnIn0.cxIlq0RVXLb46NrWY5Zieg");
}


JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getLIVE(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_nearby_1tables_getaudiokey(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_nearby_1tables_getAudiocertificate(JNIEnv *env, jobject thiz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getaudiokey(JNIEnv *env, jclass clazz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getAudiocertificate(JNIEnv *env, jclass clazz) {
    return (*env)-> NewStringUTF(env, "XX");
}

JNIEXPORT jstring JNICALL
Java_com_voive_android_App_getGCM(JNIEnv *env, jclass clazz) {
    return (*env)-> NewStringUTF(env, "XX");
}