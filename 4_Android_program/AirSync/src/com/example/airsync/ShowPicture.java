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


public class ShowPicture extends Activity 
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
		/*ȫ����ʾ*/
		 getWindow().setFlags
		 (
				 WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN
		  );
		 requestWindowFeature(Window.FEATURE_NO_TITLE );
		 
		/* ����������ĸ����ؼ���ʽ*/
		setContentView(R.layout.activity_showpicture);
		
		/*��һ�е��֣���ʾ��Ϣ��*/
		//tvShowPicture = (TextView) findViewById(R.id.show_picture_text);
		//tvShowPicture.setText("�ðɣ��Ҷ��ˣ�������������");
		
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		
		/*����surface����*/
		mLayout01 = ( LinearLayout )findViewById( R.id.myLinearLayout1 );
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams
			(
				LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT
			);
		mMySurfaceView01 = new MySurfaceView( ShowPicture.this, width, height);
		
		mLayout01.addView( mMySurfaceView01, llp );	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		/*��ָ���ºͷſ�ʱ�Ķ��� */
		if( event.getAction() == MotionEvent.ACTION_DOWN )
		{
			mMySurfaceView01.onDown();
		}
		else if( event.getAction() == MotionEvent.ACTION_UP )
		{
			mMySurfaceView01.onUp();
		}
		
		/*��touch event ����ʱ������myGestureListener */
		if( mGestureDetector01.onTouchEvent(event))
		{
			return mGestureDetector01.onTouchEvent(event);
		}
		else
		{
			return super.onTouchEvent(event);
		}
	}
	
	/*���ƶ����ļ�����*/
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
		/*�뿪����ʱ����ռ�����*/
		mGestureListener01 = null;
		mGestureDetector01 = null;
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		/*�����activity�ٴλظ���ȡ�ý���ʱ�����´��������� */
		mGestureListener01 = new myGestureListener();
		mGestureDetector01 = new GestureDetector( ShowPicture.this, mGestureListener01 );
		
		super.onResume();
	}
		
	 
	 

		
}

	









