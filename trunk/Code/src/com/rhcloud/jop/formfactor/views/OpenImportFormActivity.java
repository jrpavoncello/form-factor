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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OpenImportFormActivity extends FormFactorFragmentActivity implements DrawerListener, OnItemClickListener, IDatabaseReadyListener
{
	private ViewPager mViewPager;
	private ListView mFormList;
	private User mCurrentUser;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.views.OpenImportFormActivity";
	private FormListItem[] mFormItems;
	private OpenImportActionType mActionType;
	
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
		
        this.setTitle(this.getResources().getString(R.string.activity_open_import_form));
		
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new FormFactorPagerAdapter(getSupportFragmentManager(), this));
        
        mFormList = (ListView)this.findViewById((R.id.activity_open_import_items));

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
		
        this.mActionType = OpenImportActionType.GetByIndex(savedInstanceState.getLong(BundleKeys.OpenFormActionID));
		
		if(this.mActionType == OpenImportActionType.OpenCreate)
		{
	        mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, gatherLocalForms()));
	        mFormList.setOnItemClickListener(this);
	        mFormList.bringToFront();
		}
		else if(this.mActionType == OpenImportActionType.OpenComplete)
		{
	        mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, gatherLocalForms()));
	        mFormList.setOnItemClickListener(this);
	        mFormList.bringToFront();
		}
		else if(this.mActionType == OpenImportActionType.ImportCreate)
		{
			FormRetriever formRetriever = new FormRetriever();
			formRetriever.execute();
		}
		else if(this.mActionType == OpenImportActionType.ImportComplete)
		{
			FormRetriever formRetriever = new FormRetriever();
			formRetriever.execute();
		}
	}
	
	private FormListItem[] gatherLocalForms()
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
			
	        mFormItems = convertToListItems(localForms);
		}
        
        return mFormItems;
	}
	
	private FormListItem[] convertToListItems(List<Form> forms)
	{
		int size = forms.size();
        FormListItem[] formItems = new FormListItem[size];
		
		for(int i = 0; i < size; i++)
		{
			Form form = forms.get(i);
        	FormListItem item = new FormListItem(form);
        	formItems[i] = item;
		}
		
		return formItems;
	}
	
	private void populateList(List<Form> forms)
	{
		this.mFormItems = this.convertToListItems(forms);
		mFormList.setAdapter(new FormListItemArrayAdapter(this, R.layout.form_list_item, this.mFormItems));
        mFormList.setOnItemClickListener(this);
        mFormList.bringToFront();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.open_import_form, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
			FormListItem item = this.mFormItems[position];
			Form form = item.mForm;

			Intent intent = new Intent(this, CreateActivity.class);
			intent.putExtra(BundleKeys.CreateFormID, form.ID);
			this.startActivity(intent);
		}
		else if(this.mActionType == OpenImportActionType.OpenComplete)
		{
			
		}
		else if(this.mActionType == OpenImportActionType.ImportCreate)
		{
			FormListItem item = this.mFormItems[position];
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
			
		}
	}

	@Override
	public void prepareDrawerLayout(Menu menu)
	{
		
	}
	
	public class FormRetriever extends AsyncTask<Long, Integer, List<Form>>
	{
		public FormRetriever(){ }
		
		@Override
	    protected List<Form> doInBackground(Long... extFormIDs)
		{
			int size = extFormIDs.length;
			
			List<Form> forms = new ArrayList<Form>(size);
			
			try
			{
				if(size > 0)
				{
					for(Long id : extFormIDs)
					{
				        HttpClient httpclient = new DefaultHttpClient(); 
				        HttpGet httpGet = new HttpGet("http://formfactor-jop.rhcloud.com/form.php?id=" + id);

				        int i = 0;

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
							JSONObject formJSON = responseJSON.getJSONObject("Form");
							
							form.Read(formJSON.toString());
							form.ExternalID = externalID;
							
							forms.add(form);
						}
						
						in.close();
						
			            publishProgress(Integer.valueOf((int)(((float)(i + 1))/((float)(size + 1))*100)));
				        
				        i++;
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
			}
	        catch (ClientProtocolException ex)
	        {
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
	        } 
	        catch (IOException ex)
	        {
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
	        } 
			catch (JSONException ex) 
			{
				Log.e(TAG_NAME , ex.getMessage() + "\r\n" + ex.getStackTrace());
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
			populateList(result);
	    }
	}

	@Override
	public void OnDatabaseReady() {
		// TODO Auto-generated method stub
		
	}
}