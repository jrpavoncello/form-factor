package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.OpenImportActionType;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class FormFactorFragmentActivity extends FragmentActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private FragmentActivity mActivity;
	private DrawerItem[] mDrawerItems;

	private int mDrawerResourceID;

	protected FormFactorDB mFormFactorDb;

	private DrawerItem[] gatherDrawerItems()
	{
		String[] textRefs = getResources().getStringArray(R.array.drawer_items_text);

		TypedArray imageRefs = getResources().obtainTypedArray(R.array.drawer_items_image);

		DrawerItem[] items = new DrawerItem[textRefs.length];
		
		for (int i = 0; i < textRefs.length; i++)
		{
			DrawerItem item = new DrawerItem(imageRefs.getDrawable(i), textRefs[i]);
			items[i] = item;
		}

		imageRefs.recycle();

		this.mDrawerItems = items;

		return items;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		mDrawerTitle = getResources().getString(R.string.drawer_menu_title);
		this.setTitle(R.string.drawer_menu_title);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(mDrawerResourceID);
		mDrawerToggle = new ActionBarDrawerToggle(mActivity, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.action_open,
				R.string.action_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) 
			{
				super.onDrawerClosed(view);
				getActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView)
			{
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
				mDrawerList.bringToFront();
			}
		};

		mDrawerList = (ListView) this.mActivity.findViewById((R.id.fragment_menu));

		mDrawerList.setAdapter(new DrawerItemArrayAdapter(this, R.layout.drawer_list_item, gatherDrawerItems()));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerLayout.setDrawerListener(mDrawerToggle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	public void setData(FragmentActivity activity, int drawerResourceID)
	{
		this.mActivity = activity;
		mDrawerResourceID = drawerResourceID;
	}

	protected void selectItem(DrawerItem item, int position)
	{
		/*
        <item>@drawable/ic_home</item>
        <item>@drawable/ic_action_new_small</item>
        <item>@drawable/ic_action_collection</item>
        <item>@drawable/ic_action_save</item>
        <item>@drawable/ic_action_cloud</item>
        <item>@drawable/ic_action_add_to_queue</item>
        <item>@drawable/ic_action_logoff</item>*/
		
		switch (position) {
		case 0: // Home
		{
			if (!this.mActivity.getClass().isInstance(MainActivity.class))
			{
				Intent intent = new Intent(this.mActivity, MainActivity.class);
				this.startActivity(intent);
			}

			break;
		}
		case 1: // Create Form
			if (!this.mActivity.getClass().isInstance(CreateActivity.class))
			{
				Intent intent = new Intent(this.mActivity, CreateActivity.class);
				intent.putExtra(BundleKeys.CreateNew, true);
				this.startActivity(intent);
			}

			break;
		case 2: // Open Local
			if (!this.mActivity.getClass().isInstance(OpenImportFormActivity.class))
			{
				Intent intent = new Intent(this.mActivity, OpenImportFormActivity.class);
				intent.putExtra(BundleKeys.OpenFormActionID, OpenImportActionType.OpenCreate.GetIndex());
				this.startActivity(intent);
			}

			break;
		case 3: // Import
			break;
		case 4: // Export
			break;
		case 5: // Log off
			if (!this.mActivity.getClass().isInstance(MainActivity.class))
			{
				Intent intent = new Intent(this.mActivity, MainActivity.class);

				Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();

				editor.remove("pref_username");

				editor.remove("pref_password");

				editor.commit();

				this.startActivity(intent);
			}

			break;
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			DrawerItem item = mDrawerItems[position];
			selectItem(item, position);
		}
	}
}
