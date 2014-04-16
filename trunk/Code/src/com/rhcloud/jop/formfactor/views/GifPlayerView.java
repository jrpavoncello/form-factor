package com.rhcloud.jop.formfactor.views;

import java.io.InputStream;

import com.rhcloud.jop.formfactor.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public class GifPlayerView extends View
{
	private Movie mMovie;
	private long mStartTime;
	private int mResID;
	
	public GifPlayerView(Context context)
	{
	    super(context);
	    initializeView();
	}
	
	public GifPlayerView(Context context, AttributeSet attrs)
	{	
		super(context, attrs);
		setAttrs(attrs);
	    initializeView();
	}
	
	public GifPlayerView(Context context, AttributeSet attrs, int defStyle)
	{
	    super(context, attrs, defStyle);
		setAttrs(attrs);
	    initializeView();
	}
 
    public void setGIFResource(int resId)
    {
        this.mResID = resId;
        initializeView();
    }
 
    public int getGIFResource()
    {
        return this.mResID;
    }
 
    private void initializeView()
    {
        if (mResID != 0)
        {
            InputStream is = getContext().getResources().openRawResource(mResID);
            mMovie = Movie.decodeStream(is);
            mStartTime = 0;
            this.invalidate();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawColor(Color.TRANSPARENT);
        
        super.onDraw(canvas);
        
        long now = android.os.SystemClock.uptimeMillis();
        
        if (mStartTime == 0)
        {
            mStartTime = now;
        }
        
        if (mMovie != null)
        {
            int realTime = (int)((now - mStartTime) % mMovie.duration());
            
            mMovie.setTime(realTime);
            
            int width = getWidth() - mMovie.width();
            int height = getHeight() - mMovie.height();
            
            mMovie.draw(canvas, width, height);
            
            this.invalidate();
        }
    }
	
	private void setAttrs(AttributeSet attrs)
	{
        if (attrs != null)
        {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.GIFPlayerView, 0, 0);
            String gifSource = a.getString(R.styleable.GIFPlayerView_src);
            //little workaround here. Who knows better approach on how to easily get resource id - please share
            String sourceName = Uri.parse(gifSource).getLastPathSegment().replace(".gif", "");
            setGIFResource(getResources().getIdentifier(sourceName, "drawable", getContext().getPackageName()));
            a.recycle();
        }
    }
}