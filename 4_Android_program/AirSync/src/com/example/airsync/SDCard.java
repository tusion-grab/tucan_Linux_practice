package com.example.airsync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;



public class SDCard {
	
	
	private  String Tag = "SDCard";

	/* ��SD��ָ���ļ��е���Ƭ·������һ��List�У��������ؼ���ʾ
	 * �����ƬĿ¼�Ǵ�aircard����������Ƭ�洢�ĵط�
	 * ����ʵ���ʱ���ҷ��ڣ�/mnt/extsd/TestPhoto
	 */
	public   List<String> GetSDPicture()
	{
		List<String> it=new ArrayList<String>();
		/*�����ŵ���Ƭ��Ҫ̫�󣬲�Ȼ�ڴ治�����*/
		//File file_path = new File("/mnt/extsd/TestPhoto/");
		File file_path = new File("/mnt/sdcard/Pictures/Screenshots/");
		File[] files = file_path.listFiles();
		
		for( int i=0; i<files.length; i++ )
		{
			File file = files[i];
			if( getImageFile( file.getPath()))
				it.add( file.getPath());
		}
		return it;
	}
	
	/*�ж�ȡ����ͼƬ�ļ����ļ����ǲ���֧�ֵ����� */
	private boolean getImageFile( String Name )
	{
		boolean re;
		/*ȥ����չ��*/
		String end = Name.substring( Name.lastIndexOf(".")+1,
									 Name.length()).toLowerCase();
		if( end.equals("jpg") || end.equals("gif") || end.equals("png") ||
				end.equals("bmp") || end.equals("jpeg") )
		{
			re = true;
		}
		else
		{
			re = false;
		}
		return re;
	}

	/*ȡ��SD�����ڵ�����*/
	public String ShowSize()
	{
		if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			//ȡ��SD��·��
			File path = Environment.getExternalStorageDirectory();
			
			StatFs stat_fs = new StatFs( path.getPath() );
			
			long block_size = stat_fs.getBlockSize();
			long total_blocks = stat_fs.getBlockCount();
			long available_blocks = stat_fs.getAvailableBlocks();
			
			String total = FileSize( total_blocks * block_size );
			String available = FileSize( available_blocks * block_size );
			Log.i(Tag, "total size is: "+ total);
			Log.i(Tag, "available size is: " + available);
			
			return available;
		}
		return null;
	}
	
	/*����SD���������ַ�������λMB*/
	private String FileSize( long size )
	{
		
		size = size/1024/1024;
		DecimalFormat formatter = new DecimalFormat();
		//3λ�ֱ��磬000
		formatter.setGroupingSize(3);
		return formatter.format(size);
	}
	
	
	
//private String SDPath = Environment.getExternalStorageDirectory() +"/tusion_image/";
	
	//��SD������Ŀ¼
	 public File creatSDDir( String DirName) throws IOException{
		 Log.i(Tag, "creat SD DirName is "+DirName);
		 File dir = new File( DirName);
		 if(!dir.exists())
				 // mkdirs() ��ʾ�������Ҫ�����ȴ����ϲ�Ŀ¼.
				 dir.mkdirs();
		 return dir;
	 }
	 
	 //��SD�������ļ�
	 public File creatSDFile( String DirName) throws IOException{
		 File file = new File(  DirName);
		 if(!file.exists())
				 file.createNewFile();
		 return file;
	 }
	 
	 public File WriteToSDCard(String Path, String FileName, InputStream data){
		 File file = null;
		 OutputStream output = null;
		 try{
			 creatSDDir(Path);
			 file = creatSDFile( Path + FileName);
			 output = new FileOutputStream( file);
			 
			 byte buffer[]= new byte[4*1024];
			 while((data.read(buffer) != -1)){
				 output.write(buffer);
			 }
			 output.flush(); 
		 }
		 catch(Exception e){
			 e.printStackTrace();
		 }
		 finally{
			 try{
				 output.close();
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		return file;
	 }
	
	
}