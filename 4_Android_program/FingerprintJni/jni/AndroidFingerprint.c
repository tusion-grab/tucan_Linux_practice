
#include <android/log.h>
#include <string.h>
//#include "cc_omusic_fingerprintjni_FingerprintWraper.h"
//#include "./fingerprint_ver_1/fingerprint.h"
#include <stdio.h>
#include <stdlib.h>
#include "./fingerprint_ver_1/fooid.h"
#include "./fingerprint_ver_1/common.h"
#include <jni.h>
#include <math.h>
#define  LOG_TAG    "AndroidFingerprint"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

jobject GetInstance( JNIEnv *env, jclass obj_class);

// in src dir use: javah cc.omusic.fingerprintjni.FingerprintWraper
// this string is gernerate from head file, as last step use javah oo.omusic.***.Fingerprrint
jbyteArray Java_cc_omusic_fingerprintjni_FingerprintWraper_fingerprint
//JNIEXPORT jbyteArray JNICALL Java_cc_omusic_fingerprintjni_FingerprintWraper_fingerprint
(JNIEnv *env, jobject thiz, jobject DecoderObj, jint sampleRate, jint numChannels)

{	// jnit directly use in there

	LOGE("size of float %d \n", sizeof(float));
	LOGE("size of int %d \n", sizeof( int) );
	LOGE("size of short %d \n", sizeof( short) );
	LOGE("size of char %d \n", sizeof( char) );
	LOGE("1.0f/32768.0f = %f \n ",  (1.0f/32768.0f -(1e-15) ) );

	LOGE("fooid round(0.5)=%d, round(-0.5)=%d \n", round(0.5), round(-0.5) );
	LOGE("fooid round(0.6)=%d, round(-0.6)=%d \n", round(0.6), round(-0.6) );



	jclass  PcmDecodeClass = 0;
	jmethodID getPcmSamplesMethod = 0;

	int result= -1;
    int centiseconds = 0;
    int samples_read = 0;

	LOGD("sampleRate= %d, numChannels= %d \n", sampleRate, numChannels);
	/**
	 * native call java method, reference <core java V2> charpter 12
	 */
	PcmDecodeClass  = (*env)->GetObjectClass( env, DecoderObj );
	/** //public  int getWavData( short[] data) */
	//getPcmSamplesMethod = (*env)->GetMethodID( env, PcmDecodeClass, "getWavData", "([S)I");
	/** int readSamples (short[] samples, int offset, int numSamples); */
	getPcmSamplesMethod = (*env)->GetMethodID( env, PcmDecodeClass, "readSamples", "([SII)I");


	//initial t_fooid struct, reasample wav file to 8000hz.
    t_fooid * fooid = fp_init(sampleRate, numChannels);


	int len = (int)(numChannels * sampleRate *1 ); // every chip is 1s

	jshortArray pcmjArray = (*env)->NewShortArray( env, len  );
	short *pcm = NULL;
	LOGD("len= %d \n", len);


	do
    {
		/**
		 * return the number of  items read, every loop read 1s data from wav file
		 * int readSamples (short[] samples, int offset, int numSamples);
		 * samples =  pcmjArray[] to store samples
		 * offset = 0
		 * numSamples = len = (numChannels * sampleRate );
		 */
    	samples_read = (*env)->CallIntMethod( env, DecoderObj, getPcmSamplesMethod, pcmjArray, 0, len);
    	//samples_read, (*env)->GetArrayLength( env, pcmjArray) );
        if (samples_read != 0)
        {
        	pcm =(short *)((*env)->GetShortArrayElements(env, pcmjArray, NULL));
    		//1/100 seconds
        	centiseconds += (100 * samples_read) / (numChannels* sampleRate );
    		//LOGD("centiseconds:%d, samples_read:%d \n", centiseconds, samples_read );

            result = fp_feed_short(fooid, pcm, samples_read);
            if (result < 0)
            {
            	LOGE("fp_feed_short()Error!\n");
                break;
            }
    		//(*env)->ReleaseShortArrayElements(env, pcmjArray, pcm, 0);
        }
        else
        {
        	LOGE("samples_read = %d, error!\n",samples_read);
        	break;
        }

    }while( centiseconds < 10000); //get the head 100s music data

	//fp_size == 424
	unsigned char * fp =  malloc(fp_getsize(fooid));
	result = fp_calculate(fooid, centiseconds, fp);


	LOGE("avg_fit = %d \n", fooid->fp.avg_fit);
	LOGE("avg_dom = %d \n", fooid->fp.avg_dom);

	jbyte *by = (jbyte*)fp;
	jbyteArray jarray =  (*env)->NewByteArray(env,424);
	(*env)->SetByteArrayRegion( env,jarray, 0, 424, (jbyte*)fp );

	(*env)->ReleaseShortArrayElements(env, pcmjArray, pcm, 0);
	free(fp);
	fp_free(fooid);
	//LOGD("complete !!!\n");

    return jarray;
}


