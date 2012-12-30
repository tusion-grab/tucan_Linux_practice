package com.example.airsync;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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



public class Main extends Activity {
	/*用于给用户显示这个程序运行前需要的必要硬件连接 */
	private TextView tvTopinfo;
	private TextView tvMetrics;
	private Button   btPicture;
	private Button   btHttp;
	//需要下载的图片地址
	String ImagePath = "http://www.baidu.com";
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
		
		/*提示用户WIFI要连到aircard */
		// 启动就提示警告框，不太爽~~
		//ShowAlertDialog( );
		

	}
	

	/*为显示照片的activity按钮创建监听器*/
    class PictureButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 			tvMetrics.setText( "算了，还是别看这个数字了，太乱~");
 			Intent intent = new Intent();
 			intent.setClass( Main.this, ShowPicture.class);
 			/*调用新的Activity*/
 			Main.this.startActivity(intent);
 			/*关闭现在这个Activity，按返回按钮时就退不回来了*/
 			//Main.this.finish();
			
		}
 	}
    
    /*为启动网络线程的按钮创建监听器*/
    class HttpButtonListener implements OnClickListener
    {	//为了访问网络，需要在manifest文件中增加网络的权限
		@Override
		public void onClick(View v) {
			StringBuffer sff = new StringBuffer();
			String myString = null;

			// TODO Auto-generated method stub
			try{
				// 此行必须，否则超链接无法点击，ScrollingMovementMethod 实现滚动条  
				//tvTopinfo.setMovementMethod(LinkMovementMethod.getInstance());  
				//tvTopinfo.setText(getHtml(ImagePath));
				Document doc = Jsoup.connect("http://tusion.sinaapp.com/project2/books.html").get();
				//  Document doc = Jsoup.connect("http://192.168.64.9:8099/AgentJava/tt.jsp").get();
				//Document doc = Jsoup.parse(getHtml(ImagePath));
				//<img src="http://www.baidu.com/img/baidu_sylogo1.gif" width="270" height="129">
				
				//Element content = doc.getElementById("img");
				//Elements links = content.getElementsByTag("a");
				//Elements links = doc.select("img#src");
				//Elements links = doc.select("a[href]");
				Elements media = doc.select("[src]");
				

				Log.i(Tag,"get html's tag select");
				Log.i(Tag,"get media size:"+media.size());
				
				for( Element link: media )
				{
					if (link.tagName().equals("img"))
					{
						Log.i(Tag,link.text());
						sff.append(link.attr("abs:src")).append("\n");
						//src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        //trim(src.attr("alt"), 20));
					}
				}
				myString = sff.toString();
				tvTopinfo.setText(myString);
				Log.i(Tag,myString );
				
			}
			catch ( Exception e )
			{
				Toast.makeText( Main.this, "链接延时", Toast.LENGTH_LONG).show(); 
				Log.e(Tag, "Error in network call",e);
			}
			
			
		}
		

    	
    }
    
    
    /*
     * 将制定网址的网页源文件HTML下载，以便解析
     */
    public String getHtml( String ImagePath ) throws Exception
	{

		URL url = new URL(ImagePath);
		String html="";
		// 打开路径链接---得到HttpURLConnection对象
		HttpURLConnection connection = (HttpURLConnection ) url.openConnection();
		Log.i(Tag,"URL:" + url.toString());
		// 设置连接超时
		connection.setReadTimeout(5*1000);
		// 通过HTTP协议请求网络HTML---设置请求方式：get/post 
		connection.setRequestMethod("GET");
		Log.i(Tag,"response code:"+ connection.getResponseCode());
		//判断网站连接的成功哦功能与否
		if( connection.getResponseCode() == 200 )
		{
			Log.i(Tag,"http connection response success!");
			// 从外界想手机应用内传递数据----通过输入流获取HTML数据 
			InputStream input_stream = connection.getInputStream();
			// 从输入流中获取HTML的二进制数据----readInputStream() 
			byte[] data = readHttpStream(input_stream );
			html = new String(data);
			Log.i(Tag,"data length=" + data.length);	
		}
		Log.i(Tag,"data=" + html);
		return html;
	}
    /*将输入流保存在一个数组中*/
	public byte[] readHttpStream( InputStream in_stream ) throws Exception 
	{
		ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
		  // 定义一个缓冲区 
		byte[] buffer = new byte[1024];
		int length = 1;
	    // 不断的从流里读取数据---in_stream.read(buffer)表示从流里读取数据到缓冲区 
	    // 读取到末尾时，返回值是-1；
		while( (length = in_stream.read( buffer ) )!=-1 )
		{
			out_stream.write( buffer, 0, length );
		}
		out_stream.close();
		in_stream.close();
		return out_stream.toByteArray();
		
	}
    
    
    
    
	/*在启动的时候启动一个警告框。
	 *用于给用户显示这个程序运行前需要的必要硬件连接信息，要保证他们在一个局域网内 
	 *！！！但是设备旋转，activity重新画后，这个警告框会再次出现，这个需要改善。
	 */
   private void ShowAlertDialog(  )
    {
	    new AlertDialog.Builder( Main.this )
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
