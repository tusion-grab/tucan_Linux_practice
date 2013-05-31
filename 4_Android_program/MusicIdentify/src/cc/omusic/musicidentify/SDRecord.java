package cc.omusic.musicidentify;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Environment;


public class SDRecord {

	
	
	
	//private String SDPath = Environment.getExternalStorageDirectory() +"/OmusicRecordVoice/";
	
	
	public static  boolean  checkSD(  ){
		boolean sdExit;
		//check SD card is  insert?
		sdExit = Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED );
		//if( sdExit )
			//SDPath = Environment.getExternalStorageDirectory() +"/OmusicRecordVoice/";
			
		return sdExit;
	}
	
	
	
	//create directory in SD card
	 public static File createSDDir(  String DirName){
		 String SDPath = null;
		 File dir = null;
		 
		 SDPath = Environment.getExternalStorageDirectory()+"";
		 dir = new File(  SDPath + File.separator + DirName);
		 if(!dir.exists()){
			 try{
				 // mkdirs() ��ʾ�������Ҫ�����ȴ����ϲ�Ŀ¼.
				 dir.mkdirs();
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		 
		 return dir;
	 }
	 
	 //create files in SD card
	 public File createSDFile(String  FilePath) throws IOException{
		 File file = new File( FilePath );
		 if(!file.exists()){
			 try{
				 file.createNewFile();
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }
		
		 return file;
	 }
	 
	 public File WriteToSDCard( String FilePath, String FileName, InputStream data){
		 File file = null;
		 OutputStream output = null;
		 String SDPath = null;
		 //String FilePath = null;
		 if( checkSD() )
		 {
			 try{
				 createSDDir(FilePath);
				 file = createSDFile( FilePath + FileName);
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
		 }
		return file;
	 }
	 
	//��ȡʵʱʱ��
	public static String GetTimeNow() {
		SimpleDateFormat    formatter    =   new    SimpleDateFormat    ("yyyyMMdd_HHmmss_SS");
    	Date    curDate    =   new    Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
    	String TimerStr    =    formatter.format(curDate);   
    	return TimerStr;
		
	}

}