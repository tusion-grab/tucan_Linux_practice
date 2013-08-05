package cc.omusic.FingerprintDecoderLib;

import java.io.File;
import android.util.Log;

public class FingerprintWraper {
	
	String TAG = "FingerprintWraper";

	
	public native String getVersionOfDecoder();

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
	
	
	
	static 
	{	
        try{
			//shared lib called libfingerprint-jin.so
        	Log.i("lib","try to load library : fingerprint_decoder_jni ");
   
        	
        	System.loadLibrary("avcodec");
        	System.loadLibrary("avformat");
        	System.loadLibrary("avutil");
        	System.loadLibrary("fingerprint_decoder_jni");
        	
		}catch( UnsatisfiedLinkError use ){
			Log.e("lib", "could not load native library fingerprint_decoder_jni");
		}
	}
	
	
	public String getDecoderVersion()
	{
		Log.d("lib", "Use a new method to call native method");
		return getVersionOfDecoder();
	}
	
	
}
