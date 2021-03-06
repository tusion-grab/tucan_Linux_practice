package com.example.airsync;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.airsync.SDCard;

public class NetConnection {
	
	
	private String Tag = "Netconnection";
	private StringBuffer sff = new StringBuffer();
	private String myString = null;
	
	
	public List<String>  HttpPraseImage( String HttpWeb) throws Exception
	{
		List<String> ImageStrings=new ArrayList<String>();
		Document doc = Jsoup.connect( HttpWeb ).get();
		//<img src="http://www.baidu.com/img/baidu_sylogo1.gif" width="270" height="129">
		Elements media = doc.select("[src]");
		Log.i(Tag,"get html's tag select");
		Log.i(Tag,"get media size:"+media.size());
		
		for( Element link: media )
		{
			if (link.tagName().equals("img"))
			{
				Log.i(Tag,link.text());
				sff.append(link.attr("abs:src")).append("\n");
				ImageStrings.add(link.attr("abs:src"));
			}
		}
		myString = sff.toString();
		Log.i(Tag,"parse image link: " + myString );
		return ImageStrings;
	}
	
	
    /*
     * 将制定网址的网页源文件HTML下载，以便解析
     */
    public String getHtmlFromUrl( String ImagePath ) throws Exception
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
   //将输入流保存在一个数组中
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
	
	
	/*
	 * 访问网络上的图片文件,并给到流中显示
	 */
	public Bitmap getURLToBitmap( String pic_url )
	{
	
		String test_url = "http://www.baidu.com/img/baidu_sylogo1.gif";
		
		URL image_Url = null;
		Bitmap bit_map = null;
		
		try 
		{
			//image_Url = new URL( pic_url );
			image_Url = new URL( test_url );
		}
		catch ( MalformedURLException e )
		{
			e.printStackTrace();
		}
		
		try
		{
			//
			HttpURLConnection connection = (HttpURLConnection) image_Url.openConnection();
			connection.connect();
		
			InputStream input_stream = connection.getInputStream();
			bit_map = BitmapFactory.decodeStream( input_stream );
			input_stream.close();
		}
		catch ( IOException e )
		{
			e.printStackTrace();
		}
		return bit_map;
	}
	
	
	

	/*
	 * 访问网络上的图片文件,并存为SD卡上的文件。
	 */
	
	public void saveImageFromUrl( String ImagePath ) throws Exception
	{
		//需要下载的图片地址
		//String ImagePath = "";
		URL url = new URL(ImagePath);
		//文件名用什么？需要考虑啊！！！！！！！！！！！！
		String FileName = ImagePath;
		 File  file = null;
		
		SDCard sd = new SDCard();
		
		//新建HTTP连接对象
		HttpURLConnection connection = (HttpURLConnection ) url.openConnection();
		Log.i(Tag, "URL:" + url.toString());
		connection.setReadTimeout(6*1000);
		connection.setRequestMethod("GET");
		Log.i(Tag, "response code:"+ connection.getResponseCode());
		//判断网站连接的成功哦功能与否
		if( connection.getResponseCode() == 200 )
		{
			Log.i(Tag, "http connection response success!");

			//新建输入流
			InputStream input_stream = connection.getInputStream();
			byte[] data = readStream(input_stream );
			
			//String FileName = GetTimeNow() +".csv";
			String SDPath = Environment.getExternalStorageDirectory().toString() +"/tusion_image/";
			Log.i(Tag, "creat SD dirsdpath is "+SDPath);
			
			try 
			{
				 sd.creatSDDir(SDPath);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(Tag, "creat SD dir error",e);
				e.printStackTrace();
			}
			
			try 
			{
				//将图片存在本地文件夹
				file = sd.creatSDFile( SDPath + FileName);
				 //file = sd.creatSDFile( FileName);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.e(Tag, "creat SD dir error",e);
				e.printStackTrace();
			}
	

			//将接受到的刘写入文件对象中
			FileOutputStream output_stream = new FileOutputStream( file); 
			output_stream.write( data );
			output_stream.close();
		}
	}


	//
	public byte[] readStream( InputStream in_stream ) throws Exception 
	{
		ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 1;
		while( (length = in_stream.read( buffer ) )!=-1 )
		{
			out_stream.write( buffer, 0, length );
		}
		out_stream.close();
		in_stream.close();
		return out_stream.toByteArray();
		
	}
	
		

}
