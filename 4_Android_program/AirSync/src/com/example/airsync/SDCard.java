package com.example.airsync;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;



public class SDCard {
	
	
	/*将SD卡指定文件夹的照片路径存在一个List中，好让相册控件显示
	 * 这个照片目录是从aircard穿过来的照片存储的地方
	 * 现在实验的时候我放在：/mnt/extsd/TestPhoto
	 */
	public   List<String> GetSDPicture()
	{
		List<String> it=new ArrayList<String>();
		/*这里存放的照片不要太大，不然内存不够存放*/
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
	
	/*判断取道的图片文件的文件名是不是支持的类型 */
	private boolean getImageFile( String Name )
	{
		boolean re;
		/*去的扩展名*/
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

	/*取得SD卡现在的容量*/
	public String ShowSize()
	{
		if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			//取得SD卡路径
			File path = Environment.getExternalStorageDirectory();
			
			StatFs stat_fs = new StatFs( path.getPath() );
			
			long block_size = stat_fs.getBlockSize();
			long total_blocks = stat_fs.getBlockCount();
			long available_blocks = stat_fs.getAvailableBlocks();
			
			String total = FileSize( total_blocks * block_size );
			String available = FileSize( available_blocks * block_size );
			
			return available;
			//return total;
		
		}
		return null;
	}
	
	/*返回SD卡容量的字符串，单位MB*/
	private String FileSize( long size )
	{
		
		size = size/1024/1024;
		/*
			if( size / 1024 >= 0 )
			{
				
				size /= 1024;
			}
			else 
				size = 0;
		 */
		DecimalFormat formatter = new DecimalFormat();
		//3位分别，如，000
		formatter.setGroupingSize(3);
		return formatter.format(size);
	}
	
	
}
