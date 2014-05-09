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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.OpenImportActionType;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.views.MainMenuActivity.FormFactorPagerAdapter;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class OpenImportFormActivity extends FormFactorFragmentActivity implements DrawerListener, OnItemClickListener, IDatabaseReadyListener
{
	private ViewPager mViewPager;
	private ListView mFormList;
	private User mCurrentUser;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.views.OpenImportFormActivity";
	private List<FormListItem> mFormItems;
	private OpenImportActionType mActionType;
	private View mDownloadStatusView;
	private TextView mDownloadStatusMessageView;
	private EditText mDownloadLocation;
	private String mDownloadURL = "";
	private FormRetriever mRetriever = null;
	private String mIntentURL = null;
	
	public OpenImportFormActivity()
	{
		super.setData(this, R.id.activity_open_import_form);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_open_import_form);
		super.onCreate(savedInstanceState);
     
		if(savedInstanceState == null)
		{
			savedInstanceState = this.getIntent().getExtras();
		}
		
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));
		
        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setAdapter(new FormFactorPagerAdapter(getSupportFragmentManager(), this));

        this.mDownloadStatusMessageView = (TextView)findViewById(R.id.activity_open_import_download_status_message);
		this.mDownloadStatusView = findViewById(R.id.activity_open_import_download_status);
        
		this.mFormList = (ListView)this.findViewById((R.id.activity_open_import_items));

        this.mFormFactorDB = FormFactorDB.getInstance(this);
        
		UnitOfWork unitOfWork = new UnitOfWork(this.mFormFactorDB);
		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
		
		UserService userService = new UserService(dataContext);
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		
		String username = pref.getString("pref_username", null);
		
		String temp = pref.getString("pref_password", null);
		
		if(temp != null)
		{
			char[] password = temp.toCharArray();
			
			User user = userService.GetUser(username, password);
			
			if(user != null && user.ID != 0)
			{
				this.mCurrentUser = user;
			}
		}
		
		this.mDownloadLocation = (EditText)this.findViewById(R.id.activity_open_import_location);
		this.mDownloadLocation.setOnKeyListener(new OnKeyListener()
		{
		    public boolean onKey(View v, int keyCode, KeyEvent event)
		    {
		        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
		        {
		        	retrieveFormFromURL();
		        	return true;
		        }
		        
		        return false;
		    }
		});
		
		if(savedInstanceState.containsKey(BundleKeys.IntentURL))
		{
			this.mIntentURL = savedInstanceState.getString(BundleKeys.IntentURL);
			this.mDownloadLocation.setText(this.mIntentURL);
		}
		
        this.mActionType = OpenImportActionType.GetByIndex(savedInstanceState.getLong(BundleKeys.OpenFormActionID));
		
		if(this.mActionType == OpenImportActionType.OpenCreate)
		{
			List<FormListItem> items = gatherLocalForms();
			FormListItem[] listItems = new FormListItem[items.size()];
			
	        this.mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, items.toArray(listItems)));
	        this.mFormList.setOnItemClickListener(this);
	        this.mFormList.bringToFront();
		}
		else if(this.mActionType == OpenImportActionType.OpenComplete)
		{
			List<FormListItem> items = gatherLocalForms();
			FormListItem[] listItems = new FormListItem[items.size()];
			
			this.mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, items.toArray(listItems)));
			this.mFormList.setOnItemClickListener(this);
			this.mFormList.bringToFront();
		}
		else if(this.mActionType == OpenImportActionType.ImportCreate)
		{
			this.mDownloadLocation.setVisibility(View.VISIBLE);
			showProgress(true);
			
			if(this.mIntentURL != null)
			{
				this.executeRetriever(new String[] { this.mIntentURL });
			}
			else
			{
				this.executeRetriever(new String[] { });
			}
		}
		else if(this.mActionType == OpenImportActionType.ImportComplete)
		{
			mDownloadLocation.setVisibility(View.VISIBLE);
			showProgress(true);
			
			if(this.mIntentURL != null)
			{
				this.executeRetriever(new String[] { this.mIntentURL });
			}
			else
			{
				this.executeRetriever(new String[] { });
			}
		}
	}
	
	private void executeRetriever(String...params)
	{
		if(this.mRetriever == null)
		{
			this.mRetriever = new FormRetriever();
		}
		else
		{
			while(!this.mRetriever.isCancelled())
			{
				this.mRetriever.cancel(true);
			}
			
			this.mRetriever = new FormRetriever();
		}
		
		this.mRetriever.execute(params);
	}
	
	private List<FormListItem> gatherLocalForms()
	{
		if(this.mCurrentUser != null)	
		{
			UnitOfWork unitOfWork = new UnitOfWork(this.mFormFactorDB);
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			FormService formService = new FormService(dataContext);
			
			List<Form> localForms = null;
			
			try
			{
				if(mCurrentUser != null)
				{
					localForms = formService.GetFormsByUserID(mCurrentUser.ID);
				}
			}
			catch(Exception ex)
			{
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
			}
			finally
			{
				
			}
			
			this.mFormItems = convertToListItems(localForms);
		}
        
        return mFormItems;
	}
	
	private List<FormListItem> convertToListItems(List<Form> forms)
	{
		int size = forms.size();
        List<FormListItem> formItems = new ArrayList<FormListItem>(size);
		
		for(int i = 0; i < size; i++)
		{
			Form form = forms.get(i);
        	FormListItem item = new FormListItem(form);
        	formItems.add(item);
		}
		
		return formItems;
	}
	
	private void populateList(List<Form> forms)
	{
		this.mFormItems = this.convertToListItems(forms);
		
		if(mActionType == OpenImportActionType.ImportComplete)
		{
			List<FormListItem> items = gatherLocalForms();
			this.mFormItems.addAll(items);
		}
		
		FormListItem[] listItems = new FormListItem[this.mFormItems.size()];
		
		mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, this.mFormItems.toArray(listItems)));
        mFormList.setOnItemClickListener(this);
        mFormList.bringToFront();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.open_import_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		int id = item.getItemId();
		if (id == R.id.menu_create_settings)
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		if(this.mActionType == OpenImportActionType.OpenCreate)
		{
			FormListItem item = this.mFormItems.get(position);
			Form form = item.mForm;

			Intent intent = new Intent(this, CreateActivity.class);
			intent.putExtra(BundleKeys.CreateFormID, form.ID);
			this.startActivity(intent);
		}
		else if(this.mActionType == OpenImportActionType.OpenComplete)
		{
			FormListItem item = this.mFormItems.get(position);
			Form form = item.mForm;

			Intent intent = new Intent(this, CompleteActivity.class);
			intent.putExtra(BundleKeys.CompleteFormID, form.ID);
			this.startActivity(intent);
		}
		else if(this.mActionType == OpenImportActionType.ImportCreate)
		{
			FormListItem item = this.mFormItems.get(position);
			Form form = item.mForm;

			UnitOfWork unitOfWork = new UnitOfWork(this.mFormFactorDB);
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			FormService formService = new FormService(dataContext);
			
			formService.CreateUpdateForm(form, true);

			Intent intent = new Intent(this, CreateActivity.class);
			intent.putExtra(BundleKeys.CreateFormID, form.ID);
			this.startActivity(intent);
		}
		else if(this.mActionType == OpenImportActionType.ImportComplete)
		{
			FormListItem item = this.mFormItems.get(position);
			Form form = item.mForm;

			UnitOfWork unitOfWork = new UnitOfWork(this.mFormFactorDB);
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			FormService formService = new FormService(dataContext);
			
			formService.CreateUpdateForm(form, true);

			Intent intent = new Intent(this, CompleteActivity.class);
			intent.putExtra(BundleKeys.CompleteFormID, form.ID);
			this.startActivity(intent);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		
		bundle.putLong(BundleKeys.OpenFormActionID, this.mActionType.GetIndex());
	}
	
	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
		if(bundle.containsKey(BundleKeys.OpenFormActionID))
		{
			this.mActionType = OpenImportActionType.GetByIndex(bundle.getLong(BundleKeys.OpenFormActionID));
		}
	}

	@Override
	public void prepareDrawerLayout(Menu menu)
	{
		
	}

	private void showProgress(final boolean show)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) 
		{
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

			mDownloadStatusView.setVisibility(show ? View.VISIBLE : View.GONE );
			mDownloadStatusView.animate().setDuration(shortAnimTime)
				.alpha(show ? 1 : 0)
				.setListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						mDownloadStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});

			mFormList.setVisibility(show ? View.GONE : View.VISIBLE);
			mFormList.animate().setDuration(shortAnimTime)
				.alpha(show ? 0 : 1)
				.setListener(new AnimatorListenerAdapter()
				{
					@Override
					public void onAnimationEnd(Animator animation) 
					{
						mFormList.setVisibility(show ? View.GONE : View.VISIBLE);
					}
				});
		} 
		else
		{
			mDownloadStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mFormList.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	public class FormRetriever extends AsyncTask<String, Integer, List<Form>>
	{
		public FormRetriever(){ }
		
		@Override
	    protected List<Form> doInBackground(String... URLs)
		{
			int size = URLs.length;
			
			List<Form> forms = new ArrayList<Form>(size);
			
			int retry = 0;
			boolean success = false;
			while(retry < 2 && !success)
			{
				try
				{
					if(size > 0)
					{
				        int i = 0;
						for(String url : URLs)
						{
					        HttpClient httpclient = new DefaultHttpClient(); 
					        HttpGet httpGet = new HttpGet(url);
	
				            if (isCancelled())
				            {
				            	break;
				            }
				            
				            HttpResponse response = httpclient.execute(httpGet);
							HttpEntity entity = response.getEntity();
							
							BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
							
							Form form = new Form();
							
							String formResponse = "";
							while((formResponse = in.readLine()) != null)
							{
								JSONObject responseJSON = new JSONObject(formResponse);
								long externalID = responseJSON.getLong("ID");
								
								String formJSON = responseJSON.getString("Form");
								
								form.Read(formJSON);
								form.ExternalID = externalID;
								
								forms.add(form);
							}
							
							in.close();
							
				            publishProgress(Integer.valueOf((int)(((float)(i + 1))/((float)(size + 1))*100)));
					        
					        i++;
					        
					        success = true;
						}
					}
					else
					{
				        HttpClient httpclient = new DefaultHttpClient(); 
				        HttpGet httpGet = new HttpGet("http://formfactor-jop.rhcloud.com/form.php");
	
				        int i = 0;
			            
			            HttpResponse response = httpclient.execute(httpGet);
						HttpEntity entity = response.getEntity();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
						
						String formResponse = "";
						while((formResponse = in.readLine()) != null)
						{
				            if (isCancelled())
				            {
				            	break;
				            }
				            
							JSONObject responseJSON = new JSONObject(formResponse);
							JSONArray formsReponseJSON = responseJSON.getJSONArray("Forms");
							
							size = formsReponseJSON.length();
							for(; i < size; i++)
							{
								Form form = new Form();
								
								String unparsedJSON = formsReponseJSON.getString(i);
								
								JSONObject formResponseJSON = new JSONObject(unparsedJSON);
								
								String formJSON = formResponseJSON.getString("Form");
								
								form.Read(formJSON);
								form.ExternalID = formResponseJSON.getLong("ID");
								
								forms.add(form);
							}
						}
						
						in.close();
						
			            publishProgress(Integer.valueOf((int)(((float)(i + 1))/((float)(size + 1))*100)));
				        
				        i++;
					}
					
			        success = true;
				}
		        catch (ClientProtocolException ex)
		        {
		        	size = 0;
					Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
		        } 
		        catch (IOException ex)
		        {
		        	size = 0;
					Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
		        } 
				catch (JSONException ex) 
				{
		        	size = 0;
					Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
				}
				
				retry++;
			}
			
			return forms;
	    }

		@Override
	    protected void onProgressUpdate(Integer... progress)
	    {
	    	//progress[0];
	    }

		@Override
	    protected void onPostExecute(List<Form> result)
	    {
			showProgress(false);
			populateList(result);
	    }

		@Override
		protected void onCancelled()
		{
			showProgress(false);
			mDownloadURL = "";
			mDownloadLocation.setText("");
		}
	}

	@Override
	public void OnDatabaseReady()
	{
		// TODO Auto-generated method stub
	}

	private void retrieveFormFromURL()
	{
		this.mDownloadURL = this.mDownloadLocation.getText().toString();

		if(this.mDownloadURL != null && !this.mDownloadURL.equals(""))
		{
			this.executeRetriever(new String[] { this.mDownloadURL });
		}
		else
		{
			this.executeRetriever(new String[] { });
		}
	}
}