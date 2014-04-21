package com.rhcloud.jop.formfactor.views;

import java.util.List;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainMenuActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener
{
    private ViewPager mViewPager;
	private FormFactorPagerAdapter mAppSectionsPagerAdapter;
	static MainMenuActivity mActivity = null;
	User mCurrentUser = null;
	
    private static final String LOG_TAG = "MainMenuActivity";

	public MainMenuActivity()
	{
		super.setData(this, R.id.activity_main);
	}
	
	FormFactorDb getDatabaseInstance()
	{
		return super.formFactorDb;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//setContentView must be called first so that super.onCreate has access to this activity's layout
		setContentView(R.layout.activity_main_menu);
		super.onCreate(savedInstanceState);
		formFactorDb = FormFactorDb.getInstance(this);

        // Create the adapter that will return a fragment for each of the sections
        mAppSectionsPagerAdapter = new FormFactorPagerAdapter(getSupportFragmentManager(), this);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        this.mViewPager = (ViewPager)findViewById(R.id.pager);
        this.mViewPager.setAdapter(mAppSectionsPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
                
                if(position == 0)
                {
                	MainMenuActivity.launchCreateActivity();
                }
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++)
        {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(actionBar.newTab().setText(mAppSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
        }
        
        this.mViewPager.setCurrentItem(1);
        
        MainMenuActivity.mActivity = this;
    }
	
	public synchronized static void setData()
	{
		try
		{
			UnitOfWork unitOfWork = new UnitOfWork(mActivity.getDatabaseInstance());
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			UserService userService = new UserService(dataContext);
			
			List<User> users = userService.GetAllUsers();
			
			if(!users.isEmpty())
			{
				mActivity.mCurrentUser = users.get(0);
			}
			else
			{
				mActivity.mCurrentUser = new User();
				
				mActivity.mCurrentUser.Username = "default";
				mActivity.mCurrentUser.Email = "default";
				
				userService.CreateUpdateUser(mActivity.mCurrentUser);
			}
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}
	
	public static void launchCreateActivity()
	{
		if(mActivity != null)
		{
			Intent intent = new Intent(mActivity, CreateActivity.class);
			mActivity.startActivity(intent);
		}
	}
	
	@Override
	public void onRestart()
	{
        mViewPager.setCurrentItem(1);
		mActivity = this;
		super.onRestart();
	}
	
	@Override
	public void onStop()
	{
		mActivity = null;
		super.onStop();
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
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    	
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    	
    }

	@Override
	public void prepareDrawerLayout(Menu menu)
	{
		this.onPrepareOptionsMenu(menu);
	}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class FormFactorPagerAdapter extends FragmentPagerAdapter
    {
    	private String activityTitles[];

        public FormFactorPagerAdapter(FragmentManager fm, Context context)
        {
            super(fm);
            
            this.activityTitles = new String[getCount()];
            
            this.activityTitles[0] = context.getResources().getString(R.string.activity_create_title);
            this.activityTitles[1] = context.getResources().getString(R.string.activity_main_title);
            this.activityTitles[2] = context.getResources().getString(R.string.activity_complete_title);
        }

        @Override
        public Fragment getItem(int tabIndex)
        {
            switch (tabIndex)
            {
                case 0:
                    return new CreateActivityFragment();
                case 1:
                	return MainMenuActivityFragment.newInstance();
                default:
                	return new CompleteActivityFragment();
            }
        }

        @Override
        public int getCount() 
        {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int tabIndex) 
        {
        	return this.activityTitles[tabIndex];
        }
    }

    public static class CreateActivityFragment extends Fragment
    {
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.activity_create_placeholder, container, false);
            return rootView;
        }
    }

    public static class CompleteActivityFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.activity_complete, container, false);
            return rootView;
        }
    }
}
