package com.rhcloud.jop.formfactor.views.questions;

import java.util.HashSet;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
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

public class FreeResponseQuestionEditActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener, OnTouchListener, OnFocusChangeListener
{
	private long mQuestionID = 0;
	private com.rhcloud.jop.formfactor.domain.FreeResponseQuestion mQuestion;
	static EditText mMinLength;
	static EditText mMaxLength;
	static EditText mResponseLines;
	static int mConstraintMaxLength;
	static int mConstraintMinResponses;
	static int mConstraintResponseLines;
	static int mValidMinLength;
	static int mValidMaxLength;
	static int mValidResponseLines;
	
	private HashSet<Integer> mHasReceivedFocus = new HashSet<Integer>();
	
	public FreeResponseQuestionEditActivity()
	{
		super.setData(this, R.id.activity_free_response_question_edit);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_free_response_question_edit);
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
        		
        		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
        		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
        		FormService formService = new FormService(dataContext);
        		
            	this.mQuestion = (com.rhcloud.jop.formfactor.domain.FreeResponseQuestion)formService.GetQuestionByID(this.mQuestionID); 
        	}
        }
		
		mMaxLength = (EditText)this.findViewById(R.id.activity_free_response_question_edit_answer_max);
		mMaxLength.setOnFocusChangeListener(this);
		
		mConstraintMaxLength = this.getResources().getInteger(R.integer.constraint_answer_max_length);
		mConstraintResponseLines = this.getResources().getInteger(R.integer.constraint_question_response_max_lines);
		
		if(this.mQuestion != null && mMaxLength.getText() != null && mMaxLength.getText().toString().equals(""))
		{
			mValidMaxLength = this.mQuestion.MaxLength;
			mMaxLength.setText("" + this.mQuestion.MaxLength);
		}
		
        mMinLength = (EditText)this.findViewById(R.id.activity_free_response_question_edit_min);
        mMinLength.setOnFocusChangeListener(this);
		
		if(this.mQuestion != null && mMinLength.getText() != null && mMinLength.getText().toString().equals(""))
		{
			mValidMinLength = this.mQuestion.MinLength;
			mMinLength.setText("" + this.mQuestion.MinLength);
		}
		
        mResponseLines = (EditText)this.findViewById(R.id.activity_free_response_question_edit_lines);
        mResponseLines.setOnFocusChangeListener(this);
		
		if(this.mQuestion != null && mResponseLines.getText() != null && mResponseLines.getText().toString().equals(""))
		{
			mValidResponseLines = this.mQuestion.Lines;
			mResponseLines.setText("" + this.mQuestion.Lines);
		}
		
        mMinLength.addTextChangedListener(new TextWatcher()
        {
		    public void afterTextChanged(Editable s){ }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		    
		    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{
		    	if(mMinLength != null)
		    	{
			        String newText = mMinLength.getText().toString();
	
			        if(!newText.equals("") && newText.length() < 9)
	        		{
				        int newNum = Integer.parseInt(newText);
				        
				        if(newNum > mValidMaxLength)
				        {
				        	mMinLength.setText("" + mValidMaxLength);
				        }
				        
			        	mValidMinLength = newNum;
	        		}
		    	}
		    }
        });

        mMaxLength.addTextChangedListener(new TextWatcher()
        {
		    public void afterTextChanged(Editable s){ }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		    
		    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{
		    	if(mMaxLength != null)
		    	{
			        String newText = mMaxLength.getText().toString();
	
			        if(!newText.equals("") && newText.length() < 9)
	        		{
				        int newNum = Integer.parseInt(newText);
				        
				        if(newNum > mConstraintMaxLength)
				        {
				        	mMaxLength.setText("" + mConstraintMaxLength);
				        }
				        
			        	mValidMaxLength = newNum;
	        		}
		    	}
		    }
        });
		
        mResponseLines.addTextChangedListener(new TextWatcher()
        {
		    public void afterTextChanged(Editable s){ }
		    
		    public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		    
		    public void onTextChanged(CharSequence s, int start, int before, int count)
    		{
		    	if(mResponseLines != null)
		    	{
			        String newText = mResponseLines.getText().toString();
	
			        if(!newText.equals("") && newText.length() < 9)
	        		{
				        int newNum = Integer.parseInt(newText);
				        
				        if(newNum > mConstraintResponseLines)
				        {
				        	mResponseLines.setText("" + mConstraintResponseLines);
				        }
				        
			        	mValidResponseLines = newNum;
	        		}
		    	}
		    }
        });

		FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.activity_free_response_question_edit_container);
		
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
    		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
    		
    		if(this.validateInput(false))
    		{
    			this.mQuestion.MaxLength = Integer.parseInt(mMaxLength.getText().toString());
    			this.mQuestion.MinLength = Integer.parseInt(mMinLength.getText().toString());
    			this.mQuestion.Lines = Integer.parseInt(mResponseLines.getText().toString());
    		}
    		
        	dataContext.GetFreeResponseQuestionRepository().Update(this.mQuestion);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
		case R.id.activity_free_response_question_edit_cancel:
			this.mQuestion = null;
			this.mQuestionID = 0;
			
			this.finish();
			break;
		case R.id.activity_free_response_question_edit_save:
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
		
		String maxResponses = mMaxLength.getText().toString();
		String minResponses = mMinLength.getText().toString();
		String responseLines = mResponseLines.getText().toString();
		
		String minText = ((TextView)this.findViewById(R.id.activity_free_response_question_edit_min)).getText().toString();
		String maxText = ((TextView)this.findViewById(R.id.activity_free_response_question_edit_min)).getText().toString();
		String lengthText = ((TextView)this.findViewById(R.id.activity_free_response_question_edit_lines)).getText().toString();
		
		boolean shouldValidateMin = this.mHasReceivedFocus.contains(mMinLength.getId());
		boolean shouldValidateMax = this.mHasReceivedFocus.contains(mMaxLength.getId());
		
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
				FreeResponseQuestionEditActivity.mValidMaxLength = Integer.parseInt(maxResponses);
				if(FreeResponseQuestionEditActivity.mValidMaxLength > mConstraintMaxLength)
				{
					result.Messages.add(maxText + constraint_must_be_equal_to_or_lower_than + minText);
					result.Success = false;
					
					FreeResponseQuestionEditActivity.mValidMaxLength = 0;
				}
			}
			
			if(shouldValidateMin)
			{
				FreeResponseQuestionEditActivity.mValidMinLength = Integer.parseInt(minResponses);
				if(FreeResponseQuestionEditActivity.mValidMinLength > mValidMaxLength)
				{
					result.Messages.add(minText + constraint_must_be_equal_to_or_lower_than + maxText);
					result.Success = false;
					
					FreeResponseQuestionEditActivity.mValidMinLength = 0;
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
