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
	//drawing path
	private Path drawPath;
	//drawing and canvas paint
	private Paint drawPaint, canvasPaint;
	//initial color
	private int paintColor = 0xFF000000;
	//canvas
	private Canvas drawCanvas;
	//canvas bitmap
	private Bitmap canvasBitmap;
	private boolean erase = false;
	
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
		erase = isErasing;
		
		if(erase)
		{
			this.paintColor = getResources().getColor(R.color.free_draw_color_white);

			drawPaint.setStrokeWidth(60);
			drawPaint.setColor(this.paintColor);
			drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
			this.invalidate();
		}
		else 
		{
			drawPaint.setStrokeWidth(10);
			drawPaint.setColor(paintColor);
			drawPaint.setXfermode(null);
		}
	}
	
	public void clearDrawing()
	{
	    this.drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
	    this.invalidate();
	}
	
	public boolean isErasing()
	{
		return erase;
	}
	
	public void setupDrawing()
	{
		drawPath = new Path();
		drawPaint = new Paint();
		drawPaint.setColor(getResources().getColor(R.color.free_draw_color_black));
		drawPaint.setAntiAlias(true);
		drawPaint.setStrokeWidth(10);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		
		canvasPaint = new Paint(Paint.DITHER_FLAG);
	}
	
	public void setExistingBitmap(Bitmap bmp)
	{
		canvasBitmap = Bitmap.createBitmap(this.getWidth(), this.getHeight(), Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
		
		if(bmp != null)
		{
			drawCanvas.drawBitmap(bmp, 0, 0, canvasPaint);
		}
	}
	
	public void setColor(String newColor)
	{
		this.invalidate();
		paintColor = Color.parseColor(newColor);
		drawPaint.setColor(paintColor);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		drawCanvas = new Canvas(canvasBitmap);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		if(!this.isInEditMode())
		{
			canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
			canvas.drawPath(drawPath, drawPaint);
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
		    drawPath.moveTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_MOVE:
		    drawPath.lineTo(touchX, touchY);
		    break;
		case MotionEvent.ACTION_UP:
		    drawCanvas.drawPath(drawPath, drawPaint);
		    drawPath.reset();
		    break;
		default:
		    return false;
		}
		
		this.invalidate();
		return true;
	}
}
