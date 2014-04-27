package com.rhcloud.jop.formfactor.views.questions;

import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
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
import android.widget.EditText;
import android.widget.LinearLayout;

public class FreeResponseQuestion extends QuestionViewGroup implements OnMenuItemClickListener, View.OnClickListener
{
	private LinearLayout mResponseSection;
	private EditText mResponse;
	
	private com.rhcloud.jop.formfactor.domain.FreeResponseQuestion mQuestion;

	private boolean mIsCreateMode = true;

	public FreeResponseQuestion(Context context)
	{
		super(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_free_response, (LinearLayout)super.findViewById(R.id.question_response_section));

		this.mResponseSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		this.mResponse = (EditText)this.findViewById(R.id.view_group_free_response_edit_text);
		super.setOnClickListener(this);
	}

	public FreeResponseQuestion(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_free_response, (LinearLayout)super.findViewById(R.id.question_response_section));
		
		this.mResponseSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		this.mResponse = (EditText)this.findViewById(R.id.view_group_free_response_edit_text);
		super.setOnClickListener(this);
	}

	public FreeResponseQuestion(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_free_response, (LinearLayout)super.findViewById(R.id.question_response_section));
		
		this.mResponseSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		this.mResponse = (EditText)this.findViewById(R.id.view_group_free_response_edit_text);
		super.setOnClickListener(this);
	}
	
	public int getMaxLength()
	{
		return this.mQuestion.MaxLength;
	}
	
	public int getMinLength()
	{
		return this.mQuestion.MinLength;
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
			
			this.mPopupMenu.getMenuInflater().inflate(R.menu.free_response_question_edit, this.mPopupMenu.getMenu());
			
			super.setOnMenuItemClickListener(this);
			
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
		
		case R.id.menu_create_free_response_question_edit:
			
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this.getContext()));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			FormService formService = new FormService(dataContext);
			
			formService.AddUpdateQuestion(this.mQuestion);
			
			Intent intent = new Intent(this.getContext(), FreeResponseQuestionEditActivity.class);
			intent.putExtra(BundleKeys.QuestionID, this.mQuestion.ID);
			
			this.getContext().startActivity(intent);
			
			return true;
		}
		
		return true;
	}

	@Override
	public void setData(Question question)
	{
		super.setData(question);
		
		this.mQuestion = (com.rhcloud.jop.formfactor.domain.FreeResponseQuestion)question;
		
		if(this.mQuestion.Lines > 0)
		{
			this.mResponse.setLines(this.mQuestion.Lines);
		}
	}

	public void setMaxLength(int maxLength)
	{
		this.mQuestion.MaxLength = maxLength;
	}

	public void setMinLength(int minLength)
	{
		this.mQuestion.MinLength = minLength;
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
