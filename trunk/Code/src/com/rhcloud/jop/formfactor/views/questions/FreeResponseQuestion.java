package com.rhcloud.jop.formfactor.views.questions;

import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
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
	
	public int getMaxResponses()
	{
		return ((com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion)this.mQuestion).MaxResponses;
	}
	
	public int getMinResponses()
	{
		return ((com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion)this.mQuestion).MinResponses;
	}
	
	public Question getQuestion()
	{
		this.mQuestion.Question = this.getQuestionText();
		
		return this.mQuestion;
	}
	
	public List<ResponseChoice> getResponseChoies()
	{
		return this.mQuestion.ResponseChoices;
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
			
			this.mPopupMenu.getMenuInflater().inflate(R.menu.multiple_choice_question_edit, this.mPopupMenu.getMenu());
			
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
		
		case R.id.menu_create_question_edit:
			
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this.getContext()));
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
	}

	public void setMaxResponses(int maxResponses)
	{
		((com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion)this.mQuestion).MaxResponses = maxResponses;
	}

	public void setMinResponses(int minResponses)
	{
		((com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion)this.mQuestion).MinResponses = minResponses;
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
