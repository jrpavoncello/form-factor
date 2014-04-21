package com.rhcloud.jop.formfactor.views;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.domain.*;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;
import com.rhcloud.jop.formfactor.views.questions.FreeDrawQuestionActivity;
import com.rhcloud.jop.formfactor.views.questions.FreeResponseQuestionEditActivity;
import com.rhcloud.jop.formfactor.views.questions.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.views.questions.QuestionViewGroup;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CreateActivity extends FormFactorFragmentActivity implements OnQuestionDeleteListener, ActionBar.TabListener, DrawerListener, OnTouchListener
{
	private Form mForm;
	private User mCurrentUser = null;
	private List<QuestionViewGroup> mQuestions = new ArrayList<QuestionViewGroup>();
	private static final String TAG_NAME = "CreateActivity";
	private boolean mHasResumedState = false;
	private boolean mHasSavedState = false;
	private LinearLayout mQuestionsViewGroup;
	
	public CreateActivity()
	{
		super.setData(this, R.id.activity_create);
	}

	public void addFreeDraw()
	{
		com.rhcloud.jop.formfactor.domain.FreeDrawQuestion question = new com.rhcloud.jop.formfactor.domain.FreeDrawQuestion();
		question.FormID = this.mForm.ID;
		question.Type = QuestionType.FreeDraw;
		
		this.addFreeDraw(question);
	}
	
	public void addFreeDraw(Question question)
	{
		question.Number = this.mQuestionsViewGroup.getChildCount() + 1;
		
		com.rhcloud.jop.formfactor.views.questions.FreeDrawQuestion viewGroup = new com.rhcloud.jop.formfactor.views.questions.FreeDrawQuestion(this);
		
		viewGroup.setOnQuestionDeleteListener(this);
		
		viewGroup.setData(question);
		
		this.mQuestions.add(viewGroup);
		
		this.mQuestionsViewGroup.addView(viewGroup);
		
		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		FormService formService = new FormService(dataContext);
		
		formService.AddUpdateQuestion(question);
	}
	
	public void addFreeResponse()
	{
		com.rhcloud.jop.formfactor.domain.FreeResponseQuestion question = new com.rhcloud.jop.formfactor.domain.FreeResponseQuestion();
		question.FormID = this.mForm.ID;
		question.Type = QuestionType.FreeResponse;
		question.MinLength = this.getResources().getInteger(R.integer.default_min_length);
		question.MaxLength = this.getResources().getInteger(R.integer.default_max_length);
		question.Lines = this.getResources().getInteger(R.integer.default_lines);
		
		this.addFreeResponse(question);
	}
	
	public void addFreeResponse(Question question)
	{
		question.Number = this.mQuestionsViewGroup.getChildCount() + 1;
		
		com.rhcloud.jop.formfactor.views.questions.FreeResponseQuestion freeResponseViewGroup = new com.rhcloud.jop.formfactor.views.questions.FreeResponseQuestion(this);
		
		freeResponseViewGroup.setOnQuestionDeleteListener(this);
		
		freeResponseViewGroup.setData(question);
		
		this.mQuestions.add(freeResponseViewGroup);
		
		this.mQuestionsViewGroup.addView(freeResponseViewGroup);
		
		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		FormService formService = new FormService(dataContext);
		
		formService.AddUpdateQuestion(question);
	}
	
	public void addMultipleChoice()
	{
		com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion question = new com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion();
		question.FormID = this.mForm.ID;
		question.Type = QuestionType.MultipleChoice;
		question.MinResponses = this.getResources().getInteger(R.integer.default_min_responses);
		question.MaxResponses = this.getResources().getInteger(R.integer.default_max_responses);
		
		this.addMultipleChoice(question);
	}
	
	public void addMultipleChoice(Question question)
	{
		question.Number = this.mQuestionsViewGroup.getChildCount() + 1;
		
		MultipleChoiceQuestion multipleChoiceViewGroup = new MultipleChoiceQuestion(this);
		
		multipleChoiceViewGroup.setOnQuestionDeleteListener(this);
		
		multipleChoiceViewGroup.setData(question);
		
		this.mQuestions.add(multipleChoiceViewGroup);
		
		this.mQuestionsViewGroup.addView(multipleChoiceViewGroup);
		
		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		FormService formService = new FormService(dataContext);
		
		formService.AddUpdateQuestion(question);
	}
	
	private void setQuestionNumbers()
	{
		for(int i = 0; i < this.mQuestionsViewGroup.getChildCount(); i++)
		{
			QuestionViewGroup viewGroup = (QuestionViewGroup)this.mQuestionsViewGroup.getChildAt(i);
			viewGroup.setQuestionNumber(i + 1);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_create);
		super.onCreate(savedInstanceState);
        
        DrawerLayout drawer = (DrawerLayout)this.findViewById(R.id.activity_create);
        
		this.mQuestionsViewGroup = (LinearLayout)this.findViewById(R.id.activity_create_questions);
		
		this.setData(savedInstanceState);
        
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));
		
		ActivityHelper.setupForKeyboardHide(this, drawer);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create, menu);
	    
		return true;
	}
	
	public void onDescriptionCollapse(View v)
	{
		ImageButton collapseButton = (ImageButton)v;
		ImageButton expandButton = (ImageButton)this.findViewById(R.id.activity_create_description_expand);
		
		EditText description = (EditText)this.findViewById(R.id.activity_create_description_full);
		
		description.setVisibility(View.GONE);
		collapseButton.setVisibility(View.GONE);
		expandButton.setVisibility(View.VISIBLE);
	}
	
	public void onDescriptionExpand(View v)
	{
		ImageButton expandButton = (ImageButton)v;
		ImageButton collapseButton = (ImageButton)this.findViewById(R.id.activity_create_description_collapse);
		
		EditText description = (EditText)this.findViewById(R.id.activity_create_description_full);
		
		description.setVisibility(View.VISIBLE);
		expandButton.setVisibility(View.GONE);
		collapseButton.setVisibility(View.VISIBLE);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		
		switch(id)
		{
			case R.id.menu_create_settings:
				return true;
				
			case R.id.menu_create_edit_response_delete:
				this.addMultipleChoice();
				return true;
				
			case R.id.activity_create_add_new_free_draw:
				this.addFreeDraw();
				return true;
				
			case R.id.activity_create_add_new_free_response:
				this.addFreeResponse();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPause()
	{
		this.mHasResumedState = false;
		
		this.mHasSavedState = false;
		
		super.onPause();
	}
	
	@Override
	protected void onRestart()
	{
		super.onRestart();
		
		this.mQuestions.clear();
		
		this.setData(null);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		if(!this.mHasResumedState)
		{
			this.mQuestions.clear();
			
			this.setData(null);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		if(savedInstanceState != null)
		{
			if(mForm != null)
			{
				this.saveCurrentForm();
				
				if(savedInstanceState.containsKey(BundleKeys.CreateFormID))
				{
					savedInstanceState.remove(BundleKeys.CreateFormID);
				}
	
				savedInstanceState.putLong(BundleKeys.CreateFormID, mForm.ID);
			}
		}
	}
	
	@Override
	public void onStop()
	{
		this.saveCurrentForm();
		
		super.onStop();
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft)
	{
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	    
	    return false;
	}

	@Override
	public void prepareDrawerLayout(Menu menu)
	{
		// TODO Auto-generated method stub
	}

	private void saveCurrentForm()
	{
		if(!this.mHasSavedState)
		{
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			try
			{
				EditText titleView = (EditText)this.findViewById(R.id.activity_create_title);
				this.mForm.Title = titleView.getText().toString();
				
				EditText descriptionView = (EditText)this.findViewById(R.id.activity_create_description_full);
				this.mForm.Description = descriptionView.getText().toString();
				
				this.mForm.Questions.clear();
				
				for(QuestionViewGroup question : this.mQuestions)
				{ 
					this.mForm.Questions.add(question.getQuestion());
				}
				
				FormService formService = new FormService(dataContext);
				formService.CreateUpdateForm(mForm);
				
				this.mHasSavedState = true;
			}
			catch(Exception ex)
			{
				Log.e(TAG_NAME, ex.getMessage() + "\r\n" + ex.getStackTrace());
			}
			finally
			{
				
			}
		}
	}

	private void setData(Bundle savedInstanceState)
	{
		if(!this.mHasResumedState)
		{
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
	
			try
			{
				UserService userService = new UserService(dataContext);
				
				List<User> users = userService.GetAllUsers();
				
				if(!users.isEmpty())
				{
					mCurrentUser  = users.get(0);
				}
			}
			catch(Exception ex)
			{
				Log.e(TAG_NAME, ex.getMessage() + "\r\n" + ex.getStackTrace());
			}
			finally
			{
				
			}
			
			if(savedInstanceState != null)
			{
				if(savedInstanceState.containsKey(BundleKeys.CreateFormID))
				{
					long formID = savedInstanceState.getLong(BundleKeys.CreateFormID);
					
					try
					{
						FormService formService = new FormService(dataContext);
						this.mForm = formService.GetFormByID(formID);
					}
					catch(Exception ex)
					{
						Log.e(TAG_NAME, ex.getMessage() + "\r\n" + ex.getStackTrace());
					}
					finally
					{
						
					}
				}
			}
			else
			{
				FormService formService = new FormService(dataContext);
				
				if(this.mForm == null)
				{
					try
					{
						if(mCurrentUser != null)
						{
							List<Form> lastForms = formService.GetFormsByUserID(mCurrentUser.ID);
							
							if(!lastForms.isEmpty())
							{
								this.mForm = lastForms.get(0);
							}
							else
							{
								if(mCurrentUser.ID != 0)
								{
									this.mForm = new Form();
									this.mForm.UserID = mCurrentUser.ID;
	
									formService.CreateUpdateForm(mForm);
								}
							}
						}
					}
					catch(Exception ex)
					{
						Log.e(TAG_NAME, ex.getMessage() + "\r\n" + ex.getStackTrace());
					}
					finally
					{
						
					}
				}
				else
				{
			        this.mForm = formService.GetFormByID(this.mForm.ID);
					
			        LinearLayout questions = (LinearLayout)this.findViewById(R.id.activity_create_questions);
			        questions.removeAllViews();
				}
			}
			
			if(this.mForm != null)
			{
				EditText titleView = (EditText)this.findViewById(R.id.activity_create_title);
				titleView.setText(this.mForm.Title);
				
				EditText descriptionView = (EditText)this.findViewById(R.id.activity_create_description_full);
				descriptionView.setText(this.mForm.Description);
				
				for(Question question : this.mForm.Questions)
				{
					if(question.Type == QuestionType.MultipleChoice)
					{
						this.addMultipleChoice(question);
					}
					else
					{
						if(question.Type == QuestionType.FreeResponse)
						{
							this.addFreeResponse(question);
						}
						else
						{
							if(question.Type == QuestionType.FreeDraw)
							{
								this.addFreeDraw(question);
							}
						}
					}
				}
			}
			else
			{
				EditText titleView = (EditText)this.findViewById(R.id.activity_create_title);
				titleView.setText("Oops, you are not a valid user..");
			}
			
			this.mHasResumedState = true;
		}
	}

	@Override
	public void deleteQuestion(QuestionViewGroup questionView)
	{		
		Question question = questionView.getQuestion();
		
		LinearLayout questionsView = (LinearLayout)this.findViewById(R.id.activity_create_questions);
		
		int size = this.mQuestions.size();
		
		for(int i = 0; i < size; i++)
		{
			if(question.ID == this.mQuestions.get(i).getQuestion().ID)
			{
				questionsView.removeViewAt(i);
				this.mQuestions.remove(i);
				break;
			}
		}
		
		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		
		dataContext.GetQuestionRepository().DeleteByID(question.ID);
		
		this.setQuestionNumbers();
	}
}