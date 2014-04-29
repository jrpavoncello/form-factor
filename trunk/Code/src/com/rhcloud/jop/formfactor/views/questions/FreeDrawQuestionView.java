package com.rhcloud.jop.formfactor.views.questions;

import com.rhcloud.jop.formfactor.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

public class FreeDrawQuestionView extends View
{
	private Path mDrawPath;
	private Paint mDrawPaint;
	private Paint mCanvasPaint;
	private int mPaintColorValue = 0xFF000000;
	private Canvas mDrawCanvas;
	private Bitmap mCanvasBitmap;
	private boolean mIsErasing = false;
	
	public FreeDrawQuestionView(Context context)
	{
		super(context);
		
		if(!this.isInEditMode())
		{
			this.setupDrawing();
		}
	}

	public FreeDrawQuestionView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		if(!this.isInEditMode())
		{
			this.setupDrawing();
		}
	}

	public FreeDrawQuestionView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		if(!this.isInEditMode())
		{
			this.setupDrawing();
		}
	}
	
	public void setErase(boolean isErasing)
	{
		mIsErasing = isErasing;
		
		if(mIsErasing)
		{
			mDrawPaint.setStrokeWidth(60);
			mDrawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			this.invalidate();
		}
		else 
		{
			mDrawPaint.setStrokeWidth(10);
			mDrawPaint.setColor(mPaintColorValue);
			mDrawPaint.setXfermode(null);
		}
	}
	
	public void clearDrawing()
	{
	    this.mDrawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    this.invalidate();
	}
	
	public boolean isErasing()
	{
		return mIsErasing;
	}
	
	public void setupDrawing()
	{
		mDrawPath = new Path();
		mDrawPaint = new Paint();
		mDrawPaint.setColor(getResources().getColor(R.color.free_draw_color_black));
		mDrawPaint.setAntiAlias(true);
		mDrawPaint.setStrokeWidth(10);
		mDrawPaint.setStyle(Paint.Style.STROKE);
		mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
		mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		mCanvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void setExistingBitmap(Bitmap bmp)
	{
		mCanvasBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		mDrawCanvas = new Canvas(mCanvasBitmap);
		
		if(bmp != null)
		{
			mDrawCanvas.drawBitmap(bmp, 0, 0, mCanvasPaint);
		}
	}
	
	public void setColor(String newColor)
	{
		this.invalidate();
		mPaintColorValue = Color.parseColor(newColor);
		mDrawPaint.setColor(mPaintColorValue);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mDrawCanvas = new Canvas(mCanvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if(!this.isInEditMode())
		{
			canvas.drawBitmap(mCanvasBitmap, 0, 0, mCanvasPaint);
			canvas.drawPath(mDrawPath, mDrawPaint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float touchX = event.getX();
		float touchY = event.getY();
		
		switch (event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
		    mDrawPath.moveTo(touchX, touchY);
		    break;
		    
		case MotionEvent.ACTION_MOVE:
		    mDrawPath.lineTo(touchX, touchY);
		    break;
		    
		case MotionEvent.ACTION_UP:
		    mDrawCanvas.drawPath(mDrawPath, mDrawPaint);
		    mDrawPath.reset();
		    break;
		    
		default:
		    return false;
		}
		
		this.invalidate();
		return true;
	}
}
