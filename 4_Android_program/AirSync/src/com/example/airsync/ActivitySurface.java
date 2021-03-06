package com.example.airsync;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActivitySurface extends Activity 
{
	private TextView tvShowPicture;
	private String file_path="/mnt/extsd/TestPhoto/photo6.png";
	private String SD_available;
	private List<String> pic_list =new ArrayList<String>();
	
	private LinearLayout mLayout01;
	private MySurfaceView mMySurfaceView01 = null;
	private GestureDetector mGestureDetector01;
	private myGestureListener mGestureListener01;
	private int width = 0;
	private int height = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		/*全屏显示*/
		 getWindow().setFlags
		 (
				 WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN
		  );
		 requestWindowFeature(Window.FEATURE_NO_TITLE );
		 
		/* 载入主界面的各个控件格式*/
		setContentView(R.layout.activity_surface);
		
		/*第一行的字，显示信息用*/
		//tvShowPicture = (TextView) findViewById(R.id.show_picture_text);
		//tvShowPicture.setText("好吧，我饿了，就做到这里了");
		
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		
		/*创建surface对象*/
		mLayout01 = ( LinearLayout )findViewById( R.id.myLinearLayout1 );
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams
			(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT
			);
		mMySurfaceView01 = new MySurfaceView( ActivitySurface.this, width, height);
		
		mLayout01.addView( mMySurfaceView01, llp );	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		/*手指按下和放开时的动作 */
		if( event.getAction() == MotionEvent.ACTION_DOWN )
		{
			mMySurfaceView01.onDown();
		}
		else if( event.getAction() == MotionEvent.ACTION_UP )
		{
			mMySurfaceView01.onUp();
		}
		
		/*有touch event 发生时，触发myGestureListener */
		if( mGestureDetector01.onTouchEvent(event))
		{
			return mGestureDetector01.onTouchEvent(event);
		}
		else
		{
			return super.onTouchEvent(event);
		}
	}
	
	/*手势动作的监听器*/
	 public class myGestureListener implements GestureDetector.OnGestureListener
	 {


		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY)
		{	// TODO Auto-generated method stub
			mMySurfaceView01.handleScroll( e2.getX() - e1.getX() );
			return false;
		}

		 
		@Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
		}	 
	 }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		/*离开程序时，清空监听器*/
		mGestureListener01 = null;
		mGestureDetector01 = null;
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		/*当这个activity再次回复，取得焦点时，重新创建监听器 */
		mGestureListener01 = new myGestureListener();
		mGestureDetector01 = new GestureDetector( ActivitySurface.this, mGestureListener01 );
		
		super.onResume();
	}
		
	 
	 

		
}

	










