#include "org_acme_JniWrapper.h"
#include <stdio.h>
 
#define DEFAULT_RET "HELLO WORLD !!!"
 
JNIEXPORT jstring JNICALL Java_org_acme_JniWrapper_getString
  (JNIEnv *env, jobject thiz) {
    char* ret = DEFAULT_RET;
    return (*env)->NewStringUTF(env, ret);
}