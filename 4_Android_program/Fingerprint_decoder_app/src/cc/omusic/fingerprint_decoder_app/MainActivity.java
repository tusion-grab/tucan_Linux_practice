package cc.omusic.fingerprint_decoder_app;

import cc.omusic.FingerprintDecoderLib.FingerprintWraper;
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
		
		infoText.setText( "wating for decorder's version ...");
		
		
		FingerprintWraper fp = new FingerprintWraper();
		
		
		Log.d(TAG, "get result:" + fp.getVersionOfDecoder());
		
		infoText.setText("verison: " +fp.getVersionOfDecoder());
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
