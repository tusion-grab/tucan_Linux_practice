package com.example.airreceiver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

//参考：http://www.eoeandroid.com/forum.php?mod=viewthread&tid=455

public class imageswitcher extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory
{
	private ImageSwitcher mSwitcher;
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //使得这个 Activity没有标题栏，进而这个图片显示区域会增大
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.main);

        mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        mSwitcher.setFactory(this);
        //图片进入时的特效，现在是渐进进出的，还可以是幻灯片模式的
        mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        /*gallery的插件已经不推荐使用，google有HorizontalScrollview和VP代替*/
        Gallery g = (Gallery) findViewById(R.id.gallery);
        g.setAdapter(new ImageAdapter(this));
        g.setOnItemSelectedListener(this);
    }
    
	public void onItemSelected(AdapterView parent, View v, int position, long id)
	{
		//设定大图现实的源文件
        mSwitcher.setImageResource(mImageIds[position]);
    }

    public void onNothingSelected(AdapterView parent)
    {
    	;
    }
    
    public View makeView() 
    {
        ImageView i = new ImageView(this);
        
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        i.setLayoutParams(new ImageSwitcher.LayoutParams(
        		LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT));//使大图片下面有空间给小图片，之间不重叠
        return i;
    }

    

    public class ImageAdapter extends BaseAdapter
    {
        public ImageAdapter(Context c) 
        {
            mContext = c;
        }

        public int getCount() 
        {
            return mImageIds.length;
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position) 
        {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageView i = new ImageView(mContext);

            i.setImageResource(mImageIds[position]);
            i.setAdjustViewBounds(true);
            i.setLayoutParams(new Gallery.LayoutParams(
                    LayoutParams.WRAP_CONTENT, 
                    LayoutParams.WRAP_CONTENT));
            return i;
        }

        private Context mContext;
    }

    private Integer[] mImageIds = {
            R.drawable.photo1, R.drawable.photo2, 
            R.drawable.photo3, R.drawable.photo4,
            R.drawable.photo5, R.drawable.photo6,
            R.drawable.photo7, R.drawable.photo8};
}