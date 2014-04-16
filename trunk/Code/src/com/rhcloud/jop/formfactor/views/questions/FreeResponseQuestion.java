package com.rhcloud.jop.formfactor.views.questions;

import java.util.ArrayList;
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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class FreeResponseQuestion extends QuestionViewGroup implements OnCheckedChangeListener, OnMenuItemClickListener, View.OnClickListener, View.OnLongClickListener
{
	private LinearLayout mChoicesSection;
	
	private List<CheckBox> mCheckBoxes = new ArrayList<CheckBox>();

	private boolean mIsCreateMode = true;
	private boolean mIsEditChoiceMode = false;
	private CheckBox mSelectedCheckBox;

	public FreeResponseQuestion(Context context)
	{
		super(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_multiple_choice, (LinearLayout)super.findViewById(R.id.question_response_section));
		
		this.mChoicesSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		super.setOnClickListener(this);
	}

	public FreeResponseQuestion(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_multiple_choice, (LinearLayout)super.findViewById(R.id.question_response_section));
		
		this.mChoicesSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		super.setOnClickListener(this);
	}

	public FreeResponseQuestion(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_group_multiple_choice, (LinearLayout)super.findViewById(R.id.question_response_section));
		
		this.mChoicesSection = (LinearLayout)this.findViewById(R.id.question_response_section);
		super.setOnClickListener(this);
	}

	private void addResponseChoice(ResponseChoice response)
	{
		LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		CheckBox checkBox = (CheckBox)inflater.inflate(R.layout.view_multiple_choice_checkbox, null);

		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this.getContext()));
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);

		if(this.mQuestion != null)
		{
			response.QuestionID = this.mQuestion.ID;
		}
		
		if(response.ID == 0)
		{
			response.Choice = this.getResources().getString(R.string.default_multiple_choice_response);
			
			dataContext.GetResponseChoiceRepository().Add(response);
			
			this.mQuestion.ResponseChoices.add(response);
		}
		else
		{
			if(!this.mQuestion.ResponseChoices.contains(response))
			{
				this.mQuestion.ResponseChoices.add(response);
			}
		}
		
		checkBox.setText(response.Choice);
		checkBox.setChecked(false);
		
		checkBox.setOnCheckedChangeListener(this);
		checkBox.setOnLongClickListener(this);
		
		this.mCheckBoxes.add(checkBox);
		
		this.mChoicesSection.addView(checkBox);
		
		this.invalidate();
	}
	
	private void deleteResponseChoice()
	{
		for(int i = 0; i < this.mCheckBoxes.size(); i++)
		{
			if(this.mSelectedCheckBox == mCheckBoxes.get(i))
			{
				this.mQuestion.ResponseChoices.remove(i);
				this.mCheckBoxes.remove(i);
			}
		}

		this.mChoicesSection.removeView(this.mSelectedCheckBox);
	}
	
	public int getMaxResponses()
	{
		return this.mQuestion.Max;
	}
	
	public int getMinResponses()
	{
		return this.mQuestion.Min;
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
		int checkedBoxes = 0;
		
		for(int i = 0; i < this.mCheckBoxes.size(); i++)
		{
			if(mCheckBoxes.get(i).isChecked())
			{
				checkedBoxes++;
			}
		}
		
		if(checkedBoxes >= this.mQuestion.Min && checkedBoxes <= this.mQuestion.Max)
		{
			return true;
		}
			
		return false;
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		CheckBox checkBox = (CheckBox)buttonView;
		
		if(this.mIsEditChoiceMode)
		{
			if(checkBox.isChecked())
			{
				checkBox.setChecked(false);
				
				this.mSelectedCheckBox = checkBox;
				
				PopupMenu popup = new PopupMenu(this.getContext(), checkBox);
				
				popup.getMenuInflater().inflate(R.menu.multiple_choice_response_edit, popup.getMenu());
				
				popup.setOnMenuItemClickListener(this);
				
				popup.show();
			}
		}
		else
		{
			if(checkBox.isChecked())
			{
				int checkedBoxes = 0;
				
				for(int i = 0; i < this.mCheckBoxes.size(); i++)
				{
					if(this.mCheckBoxes.get(i).isChecked())
					{
						checkedBoxes++;
					}
				}
				
				if(checkedBoxes > this.mQuestion.Max)
				{
					checkBox.setChecked(false);
				}
			}
		}
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
	public boolean onLongClick(View v)
	{
		if(this.mIsCreateMode)
		{
			if(v instanceof CheckBox)
			{
				this.mSelectedCheckBox = (CheckBox)v;
				
				PopupMenu popup = new PopupMenu(this.getContext(), v);
				
				popup.getMenuInflater().inflate(R.menu.multiple_choice_response_edit, popup.getMenu());
				
				popup.setOnMenuItemClickListener(this);
				
				popup.show();
			}
		}
		
		return false;
	}

	@Override
	public boolean onMenuItemClick(MenuItem menuItem)
	{
		super.onMenuItemClick(menuItem);
		
		int id = menuItem.getItemId();
		
		switch(id)
		{
		case R.id.menu_create_response_edit:
			
			for(int i = 0; i < this.mCheckBoxes.size(); i++)
			{
				if(this.mSelectedCheckBox == mCheckBoxes.get(i))
				{
					Intent intent = new Intent(this.getContext(), MultipleChoiceResponseEditActivity.class);
					
					ResponseChoice choice = this.mQuestion.ResponseChoices.get(i);
					
					intent.putExtra(BundleKeys.ResponseChoiceID, choice.ID);
					
					this.getContext().startActivity(intent);
					
					break;
				}
			}
		
			return true;
			
		case R.id.menu_create_edit_response_delete:

			this.deleteResponseChoice();
			
			return true;
			
		case R.id.menu_create_question_add_response:
			ResponseChoice choice = new ResponseChoice();
			this.addResponseChoice(choice);
			
			return true;
			
		case R.id.menu_create_question_edit:
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this.getContext()));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			FormService formService = new FormService(dataContext);
			
			formService.AddUpdateQuestion(this.mQuestion);
			
			Intent intent = new Intent(this.getContext(), MultipleChoiceQuestionEditActivity.class);
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
		
		for(ResponseChoice choice : question.ResponseChoices)
		{
			this.addResponseChoice(choice);
		}
	}

	public void setMaxResponses(int maxResponses)
	{
		this.mQuestion.Max = maxResponses;
	}

	public void setMinResponses(int minResponses)
	{
		this.mQuestion.Min = minResponses;
	}
	
	public void setOnQuestionDeleteListener(OnQuestionDeleteListener listener)
	{
		super.setOnQuestionDeleteListener(listener);
	}
}
