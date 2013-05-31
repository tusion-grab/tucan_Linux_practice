package cc.omusic.musicidentify;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import cc.omusic.musicidentify.SDRecord;



public class MainActivity extends Activity {
	
	private ToggleButton startButton;
	private Button queryButton;
	private Button playButton;
	private Button deleteButton;
	private TextView infoText;
	private ListView musicList;
	private ArrayList<String> recordFiles;
	private ArrayAdapter<String> adapter;
	
	private File RecordMusicFile;
	private File RecordMusicDir;
	private File SelectedFile;
	private MediaRecorder mMediaRecorder;
	private boolean isStopRecord;
	private boolean sdCardExit;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startButton = (ToggleButton) findViewById( R.id.start_button);
		queryButton = (Button) findViewById( R.id.query_button);
		playButton = (Button) findViewById( R.id.play_button);
		deleteButton = (Button) findViewById( R.id.delete_button);
		infoText = (TextView) findViewById( R.id.info_text);
		musicList = (ListView) findViewById( R.id.music_List);
		
		if( SDRecord.checkSD() ){
			RecordMusicDir = SDRecord.createSDDir( "omusic" );
			//RecordMusicDir = Environment.getExternalStorageDirectory();
			
			sdCardExit = true;
		}
			
		else
			sdCardExit = false;
		
		//list all  .amr files
		getRecordFiles();
		
		adapter = new ArrayAdapter<String>(this,
				R.layout.my_simple_list_item, recordFiles );
		musicList.setAdapter(adapter);
		
		startButton.setOnCheckedChangeListener( new startButtonListener()); 
		playButton.setOnClickListener( new playButtonListener() );
		deleteButton.setOnClickListener( new deleteButtonListener() );
		musicList.setOnItemClickListener(new musicListClickListener());
		
	}


	
	

	//start record button
	class startButtonListener implements OnCheckedChangeListener{
		public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			// TODO Auto-generated method stub
			// press to start record
			if(isChecked){ 
				try{
					if( !sdCardExit ){
						Toast.makeText(MainActivity.this, "please check SD card", Toast.LENGTH_SHORT).show();
						infoText.setText("SD card is not insert.");
						return;
					}
					else{
						
						Toast.makeText(MainActivity.this, "Start to record voice.", Toast.LENGTH_LONG).show(); 
						
						String file_prefix = SDRecord.GetTimeNow();
						RecordMusicFile = File.createTempFile( file_prefix, ".amr", RecordMusicDir);
						mMediaRecorder = new MediaRecorder();
						
						mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
						mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
						mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						mMediaRecorder.setOutputFile(RecordMusicFile.getAbsolutePath());
						mMediaRecorder.prepare();
						mMediaRecorder.start();
						
						queryButton.setEnabled(false);
						playButton.setEnabled(false);
						deleteButton.setEnabled(false);
						isStopRecord = false;
						
						infoText.setText("recording ...");
					}
				}
				catch( IOException e){
					e.printStackTrace();
				}
			}
			//press to stop record
			else{
				mMediaRecorder.stop();
				mMediaRecorder.release();
				mMediaRecorder = null;
				adapter.add(RecordMusicFile.getName());
				SelectedFile = RecordMusicFile;
				
				Toast.makeText(MainActivity.this, 
					"record voice stopped.", Toast.LENGTH_SHORT).show();
				queryButton.setEnabled(true);
				playButton.setEnabled(true);
				deleteButton.setEnabled(true);
				infoText.setText("recorder stopped. file:" + RecordMusicFile.getName());

				isStopRecord = true;
				
				/*
				//debug for play recent record
				SelectedFile = new File( RecordMusicDir.getAbsolutePath()
									+ File.separator +  RecordMusicFile.getName() );
				infoText.setText("recent to play file " + SelectedFile.getName());
				*/
			}
		}
	}
	
	//select a music file from list
	public class musicListClickListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			playButton.setEnabled(true);
			deleteButton.setEnabled(true);
			queryButton.setEnabled(true);
			
			SelectedFile = new File( RecordMusicDir.getAbsolutePath()
						+ File.separator 
						+ ( (CheckedTextView) arg1).getText());
			infoText.setText("you choose: "
							+ RecordMusicDir.getAbsolutePath()
							+ File.separator 
							+ ( (CheckedTextView) arg1).getText());
						//+( (CheckedTextView) arg1).getText());
			
		}
		
	}
	
	//play a music that selected
	public class playButtonListener implements OnClickListener{
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			//SelectedFile = RecordMusicFile;
			if( SelectedFile != null && SelectedFile.exists() ){
				//openFile( SelectedFile );
				Intent intent = new Intent();
				intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setAction( android.content.Intent.ACTION_VIEW);
				String type = getMIMEType( SelectedFile );
				intent.setDataAndType(Uri.fromFile( SelectedFile ), type);
				startActivity( intent );	
			}
		}
		
	}
	
	//delete the selected music file
	public class deleteButtonListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if( SelectedFile != null){
				//remove file name from adapter
				adapter.remove(SelectedFile.getName());
				Log.e("tusion","success remow file name");
				//delete file data in sd card
				if( SelectedFile.exists() )
					SelectedFile.delete();
				infoText.setText("delete complete.");		
			}
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	

	private String getMIMEType( File f)
	{
		String end = f.getName().substring( f.getName().lastIndexOf(".") + 1,
					f.getName().length()).toLowerCase();
		String type = "";
		if( end.equals("mp3") || end.equals("aac") || end.equals("amr")
				|| end.equals("mpeg") || end.equals("mp4") ){
			type = "audio";
		}
		else if( end.equals("jpg") || end.equals("gif")||
				end.equals("png") || end.equals("jpeg") ) {
			type = "image";
		}
		else{
			type = "";
		}
		type += "/*";
		return type;
	}
	
	
	
	private void getRecordFiles( ){
		recordFiles = new ArrayList<String>();
		 if( sdCardExit ){
			 File files[] = RecordMusicDir.listFiles();
			 if(files != null ){
				 //Log.d("tusion"," get files!");
				 for( int i=0; i<files.length; i++ ){
					 if( files[i].getName().indexOf(".") >= 0 )
					 {
						 //read all .amr files
						 String file_str = files[i].getName().substring(
								 	files[i].getName().indexOf("."));
						 //Log.d("file_str", file_str);
						 if( file_str.toLowerCase().equals(".amr"))
							 recordFiles.add( files[i].getName());
					 }
				 }
			 }
			 
			 
		 }
		 
	 }
	

}


		