package com.rhcloud.jop.formfactor.views.questions;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.views.OnQuestionDeleteListener;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class QuestionViewGroup extends LinearLayout implements OnMenuItemClickListener, View.OnClickListener
{
	protected Question mQuestion = new Question();
	private float mImageDimensions = 0;
	private Drawable mImage;
	private EditText mQuestionEditText;
	private ImageButton mOverflowButton;
	private ImageView mQuestionImage;
	private Activity mActivity;
	private List<OnQuestionDeleteListener> mQuestionDeleteListeners = new ArrayList<OnQuestionDeleteListener>(1);
	private List<OnMenuItemClickListener> mOnMenuItemClickListener = new ArrayList<OnMenuItemClickListener>(1);
	protected PopupMenu mPopupMenu;
	private TextView mQuestionNumber;

	private boolean mIsCreateMode = true;
	private AttributeSet attrs;
	private int defStyle;

	protected QuestionViewGroup(Context context) 
	{
		super(context);
		
		if(!this.isInEditMode())
		{
			this.mActivity = (Activity)context;
		}

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_question, this);
		
		setData();
	}

	protected QuestionViewGroup(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		if(!this.isInEditMode())
		{
			this.mActivity = (Activity)context;
			this.attrs = attrs;
		}

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_question, this);
		
		setData();
	}

	protected QuestionViewGroup(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		if(!this.isInEditMode())
		{
			this.mActivity = (Activity)context;
			this.attrs = attrs;
			this.defStyle = defStyle;
		}

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_question, this);
		
		setData();
	}
	
	protected abstract void onQuestionDeleting();
	
	public Drawable getImage()
	{
		return mImage;
	}
	
	public float getImageDimensions()
	{
		return mImageDimensions;
	}
	
	public Question getQuestion()
	{
		this.mQuestion.Question = this.mQuestionEditText.getText().toString();
		
		return this.mQuestion;
	}
	
	public String getQuestionText()
	{
		this.mQuestion.Question = this.mQuestionEditText.getText().toString();
		return this.mQuestion.Question;
	}
	
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
		case R.id.question_overflow_button:
			
			PopupMenu popup = new PopupMenu(this.getContext(), v);
			
			popup.getMenuInflater().inflate(R.menu.question_view_group_overflow, popup.getMenu());
			
			this.mPopupMenu = popup;
			
			this.mPopupMenu.setOnMenuItemClickListener(this);
			
			this.mPopupMenu.show();
			
			break;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		int paddingLeft = getPaddingLeft();
		int paddingTop = getPaddingTop();
		int paddingRight = getPaddingRight();
		int paddingBottom = getPaddingBottom();

		int contentWidth = getWidth() - paddingLeft - paddingRight;
		int contentHeight = getHeight() - paddingTop - paddingBottom;

		if (mImage != null) 
		{
			mImage.setBounds(paddingLeft, paddingTop, paddingLeft + contentWidth, paddingTop + contentHeight);
			mImage.draw(canvas);
		}
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
	{
		int id = menuItem.getItemId();
		
		switch(id)
		{
			
		case R.id.menu_create_question_set_image:
			
			return true;
			
		case R.id.menu_create_question_delete:

			this.onQuestionDeleting();
			
			this.onQuestionDelete();
			
			return true;
		}
		
		return false;
	}

	private void onQuestionDelete()
	{
		for(OnQuestionDeleteListener listener : this.mQuestionDeleteListeners)
		{
			listener.deleteQuestion(this);
		}
	}
	
	private void setData()
	{
		// Load attributes
		final TypedArray a = this.mActivity.obtainStyledAttributes(attrs, R.styleable.view_group_question, defStyle, 0);
		
		// Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
		// values that should fall on pixel boundaries.
		mImageDimensions = a.getDimension(R.styleable.view_group_question_image_dimensions, mImageDimensions);

		if (a.hasValue(R.styleable.view_group_question_image))
		{ 
			mImage = a.getDrawable(R.styleable.view_group_question_image);
			mImage.setCallback(this);
		}
		
		this.mQuestionEditText = (EditText)this.findViewById(R.id.question_edit_text);
		this.mQuestionImage = (ImageView)this.findViewById(R.id.question_image);
		
		this.mOverflowButton = (ImageButton)this.findViewById(R.id.question_overflow_button);
		this.mOverflowButton.setOnClickListener(this);
		
		this.mQuestionNumber = (TextView)this.findViewById(R.id.view_group_question_number);
		this.mQuestionNumber.setText("" + this.mQuestion.Number);
		
		a.recycle();
	}
	
	public void setData(Question question)
	{
		this.mQuestion = question;
		
		this.mQuestionEditText.setText(question.Question);
		
		if(this.mQuestion.Image != null)
		{
			Bitmap bmp = BitmapFactory.decodeByteArray(this.mQuestion.Image, 0, this.mQuestion.Image.length);

			this.mQuestionImage.setImageBitmap(bmp);
		}
	}
	
	protected void setImage(Drawable image) 
	{
		mImage = image;
		this.invalidate();
	}
	
	protected void setImageDimensions(float dimensions)
	{
		mImageDimensions = dimensions;
		this.invalidate();
	}

	protected void setOnQuestionDeleteListener(OnQuestionDeleteListener listener)
	{
		this.mQuestionDeleteListeners.add(listener);
	}
	
	protected void setOnMenuItemClickListener(OnMenuItemClickListener listener)
	{
		this.mOnMenuItemClickListener.add(listener);
	}

	protected void setQuestionText(String question)
	{
		this.mQuestion.Question = question;
		this.mQuestionEditText.setText(question);
	}
}
