
package cc.omusic.Fingerprint_decoder_jni;


import java.io.File;
import android.util.Log;



public class FingerprintWraper {
	
	String TAG = "FingerprintWraper";
	
	
	
	/**
	 * Invoke the fingerprint native library and generate the fingerprint code.<br>
	 * Since echoprint requires the audio data to be an array of floats in the<br>
	 * range [-1, 1] this method will normalize the data array transforming the<br>
	 * 16 bit signed shorts into floats. 
	 * 
	 * @param data PCM encoded data as shorts
	 * @param numSamples number of PCM samples at 44,100 Hz, 2 channel.
	 * @return The generated fingerprint 
	 */
	/**
	 * @param in_object: wav data provider, call object's getWavData() method.
	 * @param sampleRate
	 * @param numChannels
	 * @return
	 */
	
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
        	//System.loadLibrary("fingerprint-jin");
        	System.loadLibrary("fingerprint_decoder_jni");
        	//System.loadLibrary("avcodec");
        	//System.loadLibrary("avformat");
        	
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
