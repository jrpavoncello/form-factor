package com.rhcloud.jop.formfactor.views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.UserResponse;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.domain.services.ResponseService;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;
import com.rhcloud.jop.formfactor.views.questions.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.views.questions.QuestionViewGroup;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CompleteActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener, OnTouchListener
{
	private Form mForm;
	private UserResponse mUserResponse;
	private User mCurrentUser = null;
	private List<QuestionViewGroup> mQuestions = new ArrayList<QuestionViewGroup>();
	private static final String TAG_NAME = "CompleteActivity";
	private boolean mHasResumedState = false;
	private boolean mHasSavedState = false;
	private LinearLayout mQuestionsViewGroup;
	
	public CompleteActivity()
	{
		super.setData(this, R.id.activity_complete);
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
		
		com.rhcloud.jop.formfactor.views.questions.FreeDrawQuestion viewGroup = new com.rhcloud.jop.formfactor.views.questions.FreeDrawQuestion(this, false);
		
		viewGroup.setData(question);
		
		this.mQuestions.add(viewGroup);
		
		this.mQuestionsViewGroup.addView(viewGroup);
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
		
		com.rhcloud.jop.formfactor.views.questions.FreeResponseQuestion freeResponseViewGroup = new com.rhcloud.jop.formfactor.views.questions.FreeResponseQuestion(this, false);
		
		freeResponseViewGroup.setData(question);
		
		this.mQuestions.add(freeResponseViewGroup);
		
		this.mQuestionsViewGroup.addView(freeResponseViewGroup);
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
		
		MultipleChoiceQuestion multipleChoiceViewGroup = new MultipleChoiceQuestion(this, false);
		
		multipleChoiceViewGroup.setData(question);
		
		this.mQuestions.add(multipleChoiceViewGroup);
		
		this.mQuestionsViewGroup.addView(multipleChoiceViewGroup);
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
		setContentView(R.layout.activity_complete);
		super.onCreate(savedInstanceState);
        
        DrawerLayout drawer = (DrawerLayout)this.findViewById(R.id.activity_complete);
        
		this.mQuestionsViewGroup = (LinearLayout)this.findViewById(R.id.activity_complete_questions);
		
		this.setData(savedInstanceState);
        
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));
		
		ActivityHelper.setupForKeyboardHide(this, drawer);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.complete, menu);
	    
		return true;
	}
	
	public void onDescriptionCollapse(View v)
	{
		ImageButton collapseButton = (ImageButton)v;
		ImageButton expandButton = (ImageButton)this.findViewById(R.id.activity_complete_description_expand);
		
		TextView description = (TextView)this.findViewById(R.id.activity_complete_description_full);
		
		description.setVisibility(View.GONE);
		collapseButton.setVisibility(View.GONE);
		expandButton.setVisibility(View.VISIBLE);
	}
	
	public void onDescriptionExpand(View v)
	{
		ImageButton expandButton = (ImageButton)v;
		ImageButton collapseButton = (ImageButton)this.findViewById(R.id.activity_complete_description_collapse);
		
		TextView description = (TextView)this.findViewById(R.id.activity_complete_description_full);
		
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
			case R.id.menu_complete_edit_question_save:
				this.mHasSavedState = false;
				this.saveCurrentUserResponse();
				return true;
				
			case R.id.menu_complete_edit_question_export:
				this.mHasSavedState = false;
				this.saveCurrentUserResponse();
				this.sendUserResponse();
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void sendUserResponse()
	{
		ResponseUploader responseUploader = new ResponseUploader();
		responseUploader.execute(this.mUserResponse);
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
				this.saveCurrentUserResponse();
				
				if(savedInstanceState.containsKey(BundleKeys.CompleteFormID))
				{
					savedInstanceState.remove(BundleKeys.CompleteFormID);
				}
	
				savedInstanceState.putLong(BundleKeys.CompleteFormID, mForm.ID);
			}
		}
	}
	
	@Override
	public void onStop()
	{
		this.saveCurrentUserResponse();
		
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
		
	}

	private void saveCurrentUserResponse()
	{
		if(!this.mHasSavedState)
		{
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			try
			{				
				ResponseService responseService = new ResponseService(dataContext);
				responseService.CreateUpdateUserResponse(mUserResponse);
				
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
			if(savedInstanceState == null)
			{
				savedInstanceState = this.getIntent().getExtras();
			}
			
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			UserService userService = new UserService(dataContext);
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
			
			String username = pref.getString("pref_username", null);
			
			String temp = pref.getString("pref_password", null);
			
			if(temp != null && !temp.equals(""))
			{
				char[] password = temp.toCharArray();
				
				User user = userService.GetUser(username, password);
				
				if(user != null && user.ID != 0)
				{
					this.mCurrentUser = user;
				}
				else
				{
					this.mCurrentUser = null;
				}
				
				if(savedInstanceState != null)
				{
					if(savedInstanceState.containsKey(BundleKeys.CompleteFormID))
					{
						long formID = savedInstanceState.getLong(BundleKeys.CompleteFormID);
						long responseID = savedInstanceState.getLong(BundleKeys.CompleteResponseID);
						
						try
						{
							ResponseService responseService = new ResponseService(dataContext);
							this.mUserResponse = responseService.GetUserResponseByID(responseID);
							
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
			}
			
			if(this.mForm != null)
			{
				TextView titleView = (TextView)this.findViewById(R.id.activity_complete_title);
				titleView.setText(this.mForm.Title);
				
				TextView descriptionView = (TextView)this.findViewById(R.id.activity_complete_description_full);
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
				TextView titleView = (TextView)this.findViewById(R.id.activity_complete_title);
				titleView.setText("Oops, you are not a valid user..");
			}
			
			this.mHasResumedState = true;
		}
	}
	
	public class ResponseUploader extends AsyncTask<UserResponse, Integer, Void>
	{
		public ResponseUploader(){ }
		
		@Override
	    protected Void doInBackground(UserResponse... responses)
		{
			try
			{
		        HttpClient httpclient = new DefaultHttpClient(); 
		        HttpPost httppost = new HttpPost("http://formfactor-jop.rhcloud.com/create.php");
		        
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(responses.length);
		        
		        int size = responses.length;
		        int i = 0;
		        for(UserResponse userResponse : responses)
		        {
		            if (isCancelled())
		            {
		            	break;
		            }
					
		            String superCerealForm = userResponse.Serialize();
		            
					nameValuePairs.add(new BasicNameValuePair("FormJSON", superCerealForm));
			    	
		            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		            
		            HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					
					BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
					
					String responseWithID = "";
					while((responseWithID = in.readLine()) != null)
					{
						userResponse.ExternalID = Long.parseLong(responseWithID);
					}
					
					in.close();
					
		            publishProgress(Integer.valueOf((int)(((float)(i + 1))/((float)(size + 1))*100)));
		        }
		        
		        i++;
			}
	        catch (ClientProtocolException ex)
	        {
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
	        } 
	        catch (IOException ex)
	        {
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
	        }
			
			return null;
	    }

		@Override
	    protected void onProgressUpdate(Integer... progress)
	    {
	    	//progress[0];
	    }

		@Override
	    protected void onPostExecute(Void result)
	    {
	    	
	    }
	}
}
