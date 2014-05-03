package com.rhcloud.jop.formfactor.views.questions;

import java.util.HashSet;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.views.BundleKeys;
import com.rhcloud.jop.formfactor.views.FormFactorFragmentActivity;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

public class MultipleChoiceQuestionEditActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener, OnTouchListener, OnFocusChangeListener
{
	private long mQuestionID = 0;
	private com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion mQuestion;
	static EditText mMinResponses;
	static EditText mMaxResponses;
	static int mConstraintMaxResponses;
	static int mConstraintMinResponses;
	static int mValidMinResponses;
	static int mValidMaxResponses;
	
	private HashSet<Integer> mHasReceivedFocus = new HashSet<Integer>();
	
	public MultipleChoiceQuestionEditActivity()
	{
		super.setData(this, R.id.activity_multiple_choice_question_edit);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
 		setContentView(R.layout.activity_multiple_choice_question_edit);
		super.onCreate(savedInstanceState);
        
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));
        
        if(savedInstanceState == null)
        {
            Intent intent = this.getIntent();
            if(intent != null)
        	{
                savedInstanceState = intent.getExtras();
        	}
        }
        
        if(savedInstanceState != null)
        {
        	if(savedInstanceState.containsKey(BundleKeys.QuestionID))
        	{
            	this.mQuestionID = savedInstanceState.getLong(BundleKeys.QuestionID, 0);
        		
        		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this));
        		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
        		FormService formService = new FormService(dataContext);
        		
            	this.mQuestion = (com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion)formService.GetQuestionByID(this.mQuestionID); 
        	}
        }
		
		mMaxResponses = (EditText)this.findViewById(R.id.activity_multiple_choice_question_edit_answer_max);
		mMaxResponses.setOnFocusChangeListener(this);
		
		mConstraintMaxResponses = this.getResources().getInteger(R.integer.constraint_question_max);
		
		if(this.mQuestion != null && mMaxResponses.getText() != null && mMaxResponses.getText().toString().equals(""))
		{
			mValidMaxResponses = this.mQuestion.MaxResponses;
			mMaxResponses.setText("" + this.mQuestion.MaxResponses);
		}
		
        mMinResponses = (EditText)this.findViewById(R.id.activity_multiple_choice_question_edit_answer_min);
        mMinResponses.setOnFocusChangeListener(this);
		
		if(this.mQuestion != null && mMinResponses.getText() != null && mMinResponses.getText().toString().equals(""))
		{
			mValidMinResponses = this.mQuestion.MinResponses;
			mMinResponses.setText("" + this.mQuestion.MinResponses);
		}
		
        mMinResponses.addTextChangedListener(new TextWatcher()
        {
		    public void afterTextChanged(Editable s){ }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		    
		    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{
		    	if(mMinResponses != null)
		    	{
			        String newText = mMinResponses.getText().toString();
	
			        if(!newText.equals("") && newText.length() < 9)
	        		{
				        int newNum = Integer.parseInt(newText);
				        
				        if(newNum > mValidMaxResponses)
				        {
				        	mMinResponses.setText("" + mValidMaxResponses);
				        }
				        
			        	mValidMinResponses = newNum;
	        		}
		    	}
		    }
        });

        mMaxResponses.addTextChangedListener(new TextWatcher()
        {
		    public void afterTextChanged(Editable s){ }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		    
		    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{
		    	if(mMaxResponses != null)
		    	{
			        String newText = mMaxResponses.getText().toString();
	
			        if(!newText.equals("") && newText.length() < 9)
	        		{
				        int newNum = Integer.parseInt(newText);
				        
				        if(newNum > mConstraintMaxResponses)
				        {
				        	mMaxResponses.setText("" + mConstraintMaxResponses);
				        }
				        
			        	mValidMaxResponses = newNum;
	        		}
		    	}
		    }
        });

		FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.activity_multiple_choice_question_edit_container);
		
		ActivityHelper.setupForKeyboardHide(this, frameLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		
		if(bundle != null)
		{
			bundle.putLong(BundleKeys.ResponseChoiceID, this.mQuestionID);
		}
	}
	
	private void saveCurrentState()
	{
		if(this.mQuestion != null)
		{
    		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
    		
    		if(this.validateInput(false))
    		{
	    		this.mQuestion.MaxResponses = Integer.parseInt(mMaxResponses.getText().toString());
	    		this.mQuestion.MinResponses = Integer.parseInt(mMinResponses.getText().toString());
    		}

        	dataContext.GetMultipleChoiceQuestionRepository().Update(this.mQuestion);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		if (id == R.id.action_settings)
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void onActionClicked(View v)
	{
		int id = v.getId();
		
		switch(id)
		{
		case R.id.activity_multiple_choice_question_edit_cancel:
			this.mQuestion = null;
			this.mQuestionID = 0;
			
			this.finish();
			break;
		case R.id.activity_multiple_choice_question_edit_save:
			if(this.validateInput(true))
			{
				this.saveCurrentState();
				this.finish();
			}
			break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	    
	    return false;
	}

	@Override
	public void prepareDrawerLayout(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		if(!hasFocus)
		{
			this.validateInput(true);
		}
		else
		{
			if(!this.mHasReceivedFocus.contains(v.getId()))
			{
				this.mHasReceivedFocus.add(v.getId());
			}
		}
	}
	
	private boolean validateInput(boolean displayResults)
	{
		Result result = new Result();
		
		String maxResponses = mMaxResponses.getText().toString();
		String minResponses = mMinResponses.getText().toString();
		
		String minText = ((TextView)this.findViewById(R.id.activity_multiple_choice_question_edit_answer_min_text)).getText().toString();
		String maxText = ((TextView)this.findViewById(R.id.activity_multiple_choice_question_edit_answer_max_text)).getText().toString();
		
		boolean shouldValidateMin = this.mHasReceivedFocus.contains(mMinResponses.getId());
		boolean shouldValidateMax = this.mHasReceivedFocus.contains(mMaxResponses.getId());
		
		String constraint_must_be_a_smaller_number = this.getResources().getString(R.string.constraint_must_be_a_smaller_number);
		String constraint_must_be_equal_to_or_lower_than = this.getResources().getString(R.string.constraint_must_be_equal_to_or_lower_than);
		String Specify_a = this.getResources().getString(R.string.Specify_a);
				
		if(shouldValidateMax)
		{
			if(maxResponses.length() > 9)
			{
				result.Messages.add(maxText + constraint_must_be_a_smaller_number);
				
				result.Success = false;
			}
			else
			{
				if(maxResponses.equals(""))
				{
					result.Messages.add(Specify_a + maxText);
					
					result.Success = false;
				}
			}
		}

		if(shouldValidateMin)
		{
			if(minResponses.length() > 9 && !maxResponses.equals(""))
			{
				result.Messages.add(minText + constraint_must_be_a_smaller_number);
				
				result.Success = false;
			}
			else
			{
				if(minResponses.equals(""))
				{
					result.Messages.add(Specify_a + minText);
					
					result.Success = false;
				}
			}
		}
		
		if(result.Success)
		{
			if(shouldValidateMax)
			{
				MultipleChoiceQuestionEditActivity.mValidMaxResponses = Integer.parseInt(maxResponses);
				if(MultipleChoiceQuestionEditActivity.mValidMaxResponses > mConstraintMaxResponses)
				{
					result.Messages.add(maxText + constraint_must_be_equal_to_or_lower_than + minText);
					result.Success = false;
					
					MultipleChoiceQuestionEditActivity.mValidMaxResponses = 0;
				}
			}
			
			if(shouldValidateMin)
			{
				MultipleChoiceQuestionEditActivity.mValidMinResponses = Integer.parseInt(minResponses);
				if(MultipleChoiceQuestionEditActivity.mValidMinResponses > mValidMaxResponses)
				{
					result.Messages.add(minText + constraint_must_be_equal_to_or_lower_than + maxText);
					result.Success = false;
					
					MultipleChoiceQuestionEditActivity.mValidMinResponses = 0;
				}
			}
		}
		
		if(displayResults && !result.Success)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(this.getResources().getString(R.string.alert_dialog_problem));
			builder.setMessage(result.Join(". "));
			builder.setIcon(R.drawable.ic_warning);
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) { }
			});
			
			builder.create().show();
		}
		
		return result.Success;
	}
}
