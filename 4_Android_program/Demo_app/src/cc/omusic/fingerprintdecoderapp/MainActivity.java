package cc.omusic.fingerprintdecoderapp;


import com.changba.utils.JNIUtils;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TextView infoText;
	
	private String TAG = "main";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		infoText = (TextView) findViewById( R.id.info_text);
		
		infoText.setText( "null");
		
		infoText.setText( hello() );
		
		infoText.setText("�ܱʳ�: " + JNIUtils.getSecretKey("�ܱʳ�") +"\n"
						+ "�찲��: " + JNIUtils.getSecretKey("�찲��") +"\n"
							);
		
		
	}
	
	
	
	
	public native String hello();
	
	static 
	{	
        try{
			//shared lib called libfingerprint-jin.so
        	Log.i("lib","try to load library : libhello.so ");
        	
        	System.loadLibrary("hello");
        	
		}catch( UnsatisfiedLinkError use ){
			Log.e("lib", "could not load native library libhello.so");
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
