package com.example.jsonparsing;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class JSONParsingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jsonparsing);
		
		startButton = (Button)findViewByID( R.id.startButton)
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_jsonparsing, menu);
		return true;
	}

}