package com.rhcloud.jop.formfactor.views;

import java.util.List;

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

public class OpenImportFormActivity extends FormFactorFragmentActivity implements DrawerListener, OnItemClickListener
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
		}
		else if(this.mActionType == OpenImportActionType.ImportComplete)
		{
			
		}
	}
	
	private FormListItem[] gatherLocalForms()
	{
		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDB.getInstance(this));
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
			
			int size = localForms.size();
	        mFormItems = new FormListItem[size];
			
			for(int i = 0; i < size; i++)
			{
				Form form = localForms.get(i);
	        	FormListItem item = new FormListItem(form);
	        	mFormItems[i] = item;
			}
		}
        
        return mFormItems;
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
		else if(this.mActionType == OpenImportActionType.ImportComplete)
		{
			
		}
	}

	@Override
	public void prepareDrawerLayout(Menu menu)
	{
		
	}
}