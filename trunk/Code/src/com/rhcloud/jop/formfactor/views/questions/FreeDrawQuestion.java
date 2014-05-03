package com.rhcloud.jop.formfactor.views.questions;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.views.BundleKeys;
import com.rhcloud.jop.formfactor.views.OnQuestionDeleteListener;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FreeDrawQuestion extends QuestionViewGroup implements OnMenuItemClickListener, View.OnClickListener
{
	private ImageView mImage;
	private LinearLayout mResponseSection;
	private com.rhcloud.jop.formfactor.domain.FreeDrawQuestion mQuestion;

	private boolean mIsCreateMode = true;

	public FreeDrawQuestion(Context context)
	{
		super(context);
		
		this.setupLayout(context);
	}

	public FreeDrawQuestion(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		this.setupLayout(context);
	}

	public FreeDrawQuestion(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		
		this.setupLayout(context);
	}
	
	private void setupLayout(Context context)
	{
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.mResponseSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		
		inflater.inflate(R.layout.view_group_free_draw, this.mResponseSection);
		
		this.mImage = (ImageView)this.findViewById(R.id.view_group_free_draw_image);
		
		this.mImage.setOnClickListener(this);
		
		super.setOnClickListener(this);
	}
	
	public Question getQuestion()
	{
		this.mQuestion.Question = this.getQuestionText();
		
		return this.mQuestion;
	}
	
	public boolean isValidForSubmission()
	{
		return false;
	}
	
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		
		switch(v.getId())
		{
		case R.id.question_overflow_button:
			
			this.mPopupMenu.getMenuInflater().inflate(R.menu.free_draw_question_edit, this.mPopupMenu.getMenu());
			
			super.setOnMenuItemClickListener(this);
			
			break;
			
		case R.id.view_group_free_draw_image:
			
			ImageView image = (ImageView)v;
			
			Intent intent = new Intent(this.getContext(), FreeDrawQuestionActivity.class);
			intent.putExtra(BundleKeys.QuestionID, this.mQuestion.ID);
			intent.putExtra(BundleKeys.FreeDrawImage, image.getDrawingCache());
			
			this.getContext().startActivity(intent);
			
			break;
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
	{
		super.onMenuItemClick(menuItem);
		
		int id = menuItem.getItemId();
		
		switch(id)
		{
		
		case R.id.menu_create_free_draw_question_edit:
			/*
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this.getContext()));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			FormService formService = new FormService(dataContext);
			
			formService.AddUpdateQuestion(this.mQuestion);
			
			Intent intent = new Intent(this.getContext(), FreeDrawQuestionEditActivity.class);
			intent.putExtra(BundleKeys.QuestionID, this.mQuestion.ID);
			
			this.getContext().startActivity(intent);*/
			
			return true;
		}
		
		return true;
	}

	@Override
	public void setData(Question question)
	{
		super.setData(question);
		
		this.mQuestion = (com.rhcloud.jop.formfactor.domain.FreeDrawQuestion)question;
	}
	
	public void setOnQuestionDeleteListener(OnQuestionDeleteListener listener)
	{
		super.setOnQuestionDeleteListener(listener);
	}

	@Override
	protected void onQuestionDeleting()
	{
		
	}
}
