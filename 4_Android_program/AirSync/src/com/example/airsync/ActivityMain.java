package com.example.airsync;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Menu;
import android.view.View;

/* TextView 控件需要的库*/
import android.widget.TextView;
import android.widget.Toast;
/*按钮控件需要库*/
import android.widget.Button;
import android.util.DisplayMetrics;
import android.util.Log;
/*按钮监听器*/
import android.view.View.OnClickListener;
/*多个Activity需要新建intent对象 */
import android.content.DialogInterface;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class ActivityMain extends Activity {
	/*用于给用户显示这个程序运行前需要的必要硬件连接 */
	private TextView tvTopinfo;
	private TextView tvMetrics;
	private Button   btPicture;
	private Button   btHttp;
	private Button 	 btImageSwithcer;
	
	//需要下载的图片地址
	//String ImageWeb = "http://image.baidu.com/i?tn=list&word=liulan#%E6%91%84%E5%BD%B1|%E5%85%A8%E9%83%A8|0";
	String ImageWeb = "http://www.huxiu.com/";

	//log 文件的标记
	String Tag = "tusion";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*载入主界面的各个控件格式*/
		setContentView(R.layout.activity_main);
		
		/*判断当前设备的android版本，
		 * APK支持的最低版本在projec.properties target=android-8
		 */
		if( getApplicationInfo().targetSdkVersion <=  Build.VERSION_CODES.FROYO )
		{
			//给出一个提示框
			;
		}
		
		/*用于给用户显示这个程序运行前需要的必要硬件连接信息，要保证他们在一个局域网内 */
		tvTopinfo = (TextView) findViewById(R.id.MainTextView1);
		tvMetrics = (TextView) findViewById(R.id.MainTextView2);
		btPicture = (Button) findViewById(R.id.show_picture_button);
		btHttp =    (Button) findViewById(R.id.http_button);
		btImageSwithcer = (Button) findViewById( R.id.image_switcher_button );
				
		CharSequence info_str = getString( R.string.start_info );
		tvTopinfo.setText( info_str );
	
		/*提示用户设备的分辨率,为现实照片做准备，也许照片可以压缩为这个分辨率再显示*/
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		String metrics_str ="这个设备的分辨率为： "+ dm.widthPixels +
							" * " + dm.heightPixels;
		tvMetrics.setText( metrics_str );
		
		/*为按钮创建监听器*/
		btPicture.setOnClickListener( new PictureButtonListener() );
		btHttp.setOnClickListener(new HttpButtonListener() );
		btImageSwithcer.setOnClickListener( new SwitcherListener() );

	}
	

	/*为显示照片的activity按钮创建监听器*/
    class PictureButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 			Intent intent = new Intent();
 			intent.setClass( ActivityMain.this, ActivitySurface.class);
 			/*调用新的Activity*/
 			ActivityMain.this.startActivity(intent);
 			/*关闭现在这个Activity，按返回按钮时就退不回来了*/
 			//Main.this.finish();
			
		}
 	}
    
    /*为启动网络线程的按钮创建监听器*/
    class HttpButtonListener implements OnClickListener
    {	//为了访问网络，需要在manifest文件中增加网络的权限
    	NetConnection mNet = new NetConnection();
    	List<String> ImageStrings=new ArrayList<String>();
    	String ImageUrl ;
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try{
					
				ImageStrings =  mNet.HttpPraseImage( ImageWeb );
				tvTopinfo.setText(ImageWeb);
				tvMetrics.setText(ImageStrings.toString() );
				
			}
			catch ( Exception e )
			{
				Toast.makeText( ActivityMain.this, "链接延时", Toast.LENGTH_LONG).show(); 
				Log.e(Tag, "Error in network call",e);
			}
			
			try
			{
				for(String ImageUrl: ImageStrings)
				{
					mNet.saveImageFromUrl( ImageUrl );
				}
			}
			catch ( Exception e )
			{
				Toast.makeText( ActivityMain.this, "保存照片出错", Toast.LENGTH_LONG).show(); 
				Log.e(Tag, "Error in save picture to SD card",e);
			}
			
		}		
    }
    
    class SwitcherListener implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 			Intent intent = new Intent();
 			intent.setClass( ActivityMain.this, ActivityImageSwitcher.class);
 			/*调用新的Activity*/
 			ActivityMain.this.startActivity(intent);			
		}
    	
    }
    

    
	/*提示用户WIFI要连到aircard */
	// 启动就提示警告框，不太爽~~
	//ShowAlertDialog( );
     
	/*在启动的时候启动一个警告框。
	 *用于给用户显示这个程序运行前需要的必要硬件连接信息，要保证他们在一个局域网内 
	 *！！！但是设备旋转，activity重新画后，这个警告框会再次出现，这个需要改善。
	 */
   private void ShowAlertDialog(  )
    {
	    new AlertDialog.Builder( ActivityMain.this )
		.setTitle(R.string.alert_title)
		.setMessage(R.string.alert_message)
		//如果确认连接了，下一步。。。
		.setPositiveButton(
							R.string.alert_ok,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}
							)
		//如果没有连接呢？
		.setNegativeButton(
							R.string.alert_exit,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}
							)
		.show();
    }
    
    /*menu菜单下的一些选项*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	


}
