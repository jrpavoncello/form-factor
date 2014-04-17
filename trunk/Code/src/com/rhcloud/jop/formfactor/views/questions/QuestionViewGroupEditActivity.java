package com.rhcloud.jop.formfactor.views.questions;

import java.util.HashSet;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.views.BundleKeys;
import com.rhcloud.jop.formfactor.views.FormFactorFragmentActivity;
import com.rhcloud.jop.formfactor.views.MainActivityFragment.DrawerListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;

public class QuestionViewGroupEditActivity extends FragmentActivity implements ActionBar.TabListener, DrawerListener, OnTouchListener, OnFocusChangeListener
{
	private long mQuestionID = 0;
	private Question mQuestion;
	static EditText mMinResponses;
	static EditText mMaxResponses;
	static int mConstraintMaxResponses;
	static int mConstraintMinResponses;
	static int mValidMinResponses;
	static int mValidMaxResponses;
	
	private HashSet<Integer> mHasReceivedFocus = new HashSet<Integer>();
	
	public QuestionViewGroupEditActivity()
	{
		super();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_edit);
        
        this.setTitle(this.getResources().getString(R.string.activity_question_edit_title));

		FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.activity_multiple_choice_question_edit_container);
		
		ActivityHelper.setupForKeyboardHide(this, frameLayout);
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
	}
	
	private void saveCurrentState()
	{
		if(this.mQuestion != null)
		{
    		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
    		
    		if(this.validateInput(false))
    		{
	    		this.mQuestion.MaxResponses = Integer.parseInt(mMaxResponses.getText().toString());
	    		this.mQuestion.MinResponses = Integer.parseInt(mMinResponses.getText().toString());
    		}
    		
        	dataContext.GetQuestionRepository().UpdateSettings(this.mQuestion.ID, this.mQuestion.MinResponses, this.mQuestion.MaxResponses);
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
		case R.id.view_group_question_edit_cancel:
			this.mQuestion = null;
			this.mQuestionID = 0;
			
			this.finish();
			break;
		case R.id.view_group_question_edit_save:
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
				QuestionViewGroupEditActivity.mValidMaxResponses = Integer.parseInt(maxResponses);
				if(QuestionViewGroupEditActivity.mValidMaxResponses > mConstraintMaxResponses)
				{
					result.Messages.add(maxText + constraint_must_be_equal_to_or_lower_than + minText);
					result.Success = false;
					
					QuestionViewGroupEditActivity.mValidMaxResponses = 0;
				}
			}
			
			if(shouldValidateMin)
			{
				QuestionViewGroupEditActivity.mValidMinResponses = Integer.parseInt(minResponses);
				if(QuestionViewGroupEditActivity.mValidMinResponses > mValidMaxResponses)
				{
					result.Messages.add(minText + constraint_must_be_equal_to_or_lower_than + maxText);
					result.Success = false;
					
					QuestionViewGroupEditActivity.mValidMinResponses = 0;
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
