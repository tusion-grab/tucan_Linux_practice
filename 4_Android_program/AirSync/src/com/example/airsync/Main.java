package com.example.airsync;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
/* TextView 控件需要的库*/
import android.widget.TextView;
/*按钮控件需要库*/
import android.widget.Button;
/*探测设备的分辨率时需要*/
import android.util.DisplayMetrics;
/*按钮监听器*/
import android.view.View.OnClickListener;
/*多个Activity需要新建intent对象 */
import android.content.Intent;



public class Main extends Activity {
	/*用于给用户显示这个程序运行前需要的必要硬件连接 */
	private TextView tvTopinfo;
	private TextView tvMetrics;
	private Button   btPicture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*载入主界面的各个控件格式*/
		setContentView(R.layout.activity_main);
		
		/*用于给用户显示这个程序运行前需要的必要硬件连接信息，要保证他们在一个局域网内 */
		tvTopinfo = (TextView) findViewById(R.id.MainTextView1);
		tvMetrics = (TextView) findViewById(R.id.MainTextView2);
		btPicture = (Button) findViewById(R.id.show_picture_button);
				
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
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
    class PictureButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 			tvMetrics.setText( "算了，还是别看这个数字了，太乱~");
 			Intent intent = new Intent();
 			intent.setClass( Main.this, ShowPicture.class);
 			/*调用新的Activity*/
 			Main.this.startActivity(intent);
			
		}
 	}

	


}
