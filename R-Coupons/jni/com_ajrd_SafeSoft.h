/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_ajrd_SafeSoft */

#ifndef _Included_com_ajrd_SafeSoft
#define _Included_com_ajrd_SafeSoft
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_ajrd_SafeSoft
 * Method:    GenerateMac
 * Signature: ([BI[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_GenerateMac
  (JNIEnv *, jclass, jbyteArray, jint, jbyteArray);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    EncryptPin
 * Signature: ([B[B[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptPin
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jbyteArray);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    Byte2Hex
 * Signature: ([BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Byte2Hex
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    Hex2Byte
 * Signature: ([BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Hex2Byte
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    Des
 * Signature: ([B[BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_Des
  (JNIEnv *, jclass, jbyteArray, jbyteArray, jint);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    EncryptMsg
 * Signature: ([BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptMsg
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    DecryptMsg
 * Signature: ([BI)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_DecryptMsg
  (JNIEnv *, jclass, jbyteArray, jint);

/*
 * Class:     com_ajrd_SafeSoft
 * Method:    EncryptTlr
 * Signature: ([B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_ajrd_SafeSoft_EncryptTlr
  (JNIEnv *, jclass, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
