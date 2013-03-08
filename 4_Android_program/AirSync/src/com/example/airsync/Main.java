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

/* TextView �ؼ���Ҫ�Ŀ�*/
import android.widget.TextView;
import android.widget.Toast;
/*��ť�ؼ���Ҫ��*/
import android.widget.Button;
import android.util.DisplayMetrics;
import android.util.Log;
/*��ť������*/
import android.view.View.OnClickListener;
/*���Activity��Ҫ�½�intent���� */
import android.content.DialogInterface;
import android.content.Intent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class Main extends Activity {
	/*���ڸ��û���ʾ�����������ǰ��Ҫ�ı�ҪӲ������ */
	private TextView tvTopinfo;
	private TextView tvMetrics;
	private Button   btPicture;
	private Button   btHttp;
	//��Ҫ���ص�ͼƬ��ַ
	String ImagePath = "http://www.baidu.com";
	//log �ļ��ı��
	String Tag = "tusion";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*����������ĸ����ؼ���ʽ*/
		setContentView(R.layout.activity_main);
		
		/*�жϵ�ǰ�豸��android�汾��
		 * APK֧�ֵ���Ͱ汾��projec.properties target=android-8
		 */
		if( getApplicationInfo().targetSdkVersion <=  Build.VERSION_CODES.FROYO )
		{
			//����һ����ʾ��
			;
		}
		
		/*���ڸ��û���ʾ�����������ǰ��Ҫ�ı�ҪӲ��������Ϣ��Ҫ��֤������һ���������� */
		tvTopinfo = (TextView) findViewById(R.id.MainTextView1);
		tvMetrics = (TextView) findViewById(R.id.MainTextView2);
		btPicture = (Button) findViewById(R.id.show_picture_button);
		btHttp =    (Button) findViewById(R.id.http_button);
				
		CharSequence info_str = getString( R.string.start_info );
		tvTopinfo.setText( info_str );
	
		/*��ʾ�û��豸�ķֱ���,Ϊ��ʵ��Ƭ��׼����Ҳ����Ƭ����ѹ��Ϊ����ֱ�������ʾ*/
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm); 
		String metrics_str ="����豸�ķֱ���Ϊ�� "+ dm.widthPixels +
							" * " + dm.heightPixels;
		tvMetrics.setText( metrics_str );
		
		/*Ϊ��ť����������*/
		btPicture.setOnClickListener( new PictureButtonListener() );
		btHttp.setOnClickListener(new HttpButtonListener() );
		
		/*��ʾ�û�WIFIҪ����aircard */
		// ��������ʾ����򣬲�̫ˬ~~
		//ShowAlertDialog( );
		

	}
	

	/*Ϊ��ʾ��Ƭ��activity��ť����������*/
    class PictureButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
 			tvMetrics.setText( "���ˣ����Ǳ���������ˣ�̫��~");
 			Intent intent = new Intent();
 			intent.setClass( Main.this, ShowPicture.class);
 			/*�����µ�Activity*/
 			Main.this.startActivity(intent);
 			/*�ر��������Activity�������ذ�ťʱ���˲�������*/
 			//Main.this.finish();
			
		}
 	}
    
    /*Ϊ���������̵߳İ�ť����������*/
    class HttpButtonListener implements OnClickListener
    {	//Ϊ�˷������磬��Ҫ��manifest�ļ������������Ȩ��
		@Override
		public void onClick(View v) {
			StringBuffer sff = new StringBuffer();
			String myString = null;

			// TODO Auto-generated method stub
			try{
				// ���б��룬���������޷������ScrollingMovementMethod ʵ�ֹ�����  
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
				Toast.makeText( Main.this, "������ʱ", Toast.LENGTH_LONG).show(); 
				Log.e(Tag, "Error in network call",e);
			}
			
			
		}
		

    	
    }
    
    
    /*
     * ���ƶ���ַ����ҳԴ�ļ�HTML���أ��Ա����
     */
    public String getHtml( String ImagePath ) throws Exception
	{

		URL url = new URL(ImagePath);
		String html="";
		// ��·������---�õ�HttpURLConnection����
		HttpURLConnection connection = (HttpURLConnection ) url.openConnection();
		Log.i(Tag,"URL:" + url.toString());
		// �������ӳ�ʱ
		connection.setReadTimeout(5*1000);
		// ͨ��HTTPЭ����������HTML---��������ʽ��get/post 
		connection.setRequestMethod("GET");
		Log.i(Tag,"response code:"+ connection.getResponseCode());
		//�ж���վ���ӵĳɹ�Ŷ�������
		if( connection.getResponseCode() == 200 )
		{
			Log.i(Tag,"http connection response success!");
			// ��������ֻ�Ӧ���ڴ�������----ͨ����������ȡHTML���� 
			InputStream input_stream = connection.getInputStream();
			// ���������л�ȡHTML�Ķ���������----readInputStream() 
			byte[] data = readHttpStream(input_stream );
			html = new String(data);
			Log.i(Tag,"data length=" + data.length);	
		}
		Log.i(Tag,"data=" + html);
		return html;
	}
    /*��������������һ��������*/
	public byte[] readHttpStream( InputStream in_stream ) throws Exception 
	{
		ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
		  // ����һ�������� 
		byte[] buffer = new byte[1024];
		int length = 1;
	    // ���ϵĴ������ȡ����---in_stream.read(buffer)��ʾ�������ȡ���ݵ������� 
	    // ��ȡ��ĩβʱ������ֵ��-1��
		while( (length = in_stream.read( buffer ) )!=-1 )
		{
			out_stream.write( buffer, 0, length );
		}
		out_stream.close();
		in_stream.close();
		return out_stream.toByteArray();
		
	}
    
    
    
    
	/*��������ʱ������һ�������
	 *���ڸ��û���ʾ�����������ǰ��Ҫ�ı�ҪӲ��������Ϣ��Ҫ��֤������һ���������� 
	 *�����������豸��ת��activity���»�������������ٴγ��֣������Ҫ���ơ�
	 */
   private void ShowAlertDialog(  )
    {
	    new AlertDialog.Builder( Main.this )
		.setTitle(R.string.alert_title)
		.setMessage(R.string.alert_message)
		//���ȷ�������ˣ���һ��������
		.setPositiveButton(
							R.string.alert_ok,
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							}
							)
		//���û�������أ�
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
    
    /*menu�˵��µ�һЩѡ��*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	


}