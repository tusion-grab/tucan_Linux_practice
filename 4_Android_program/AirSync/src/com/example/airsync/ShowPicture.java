package com.example.airsync;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class ShowPicture extends Activity {
	private TextView tvShowPicture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*载入主界面的各个控件格式*/
		setContentView(R.layout.activity_showpicture);
		
		tvShowPicture = (TextView) findViewById(R.id.show_picture_text);
		tvShowPicture.setText("好吧，我饿了，就做到这里了");
		
	}
	

}
