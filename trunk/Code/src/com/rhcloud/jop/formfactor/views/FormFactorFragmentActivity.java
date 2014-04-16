package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;

import android.app.Fragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * An activity representing a single Item detail screen. This activity is only
 * used on handset devices. On tablet-size devices, item details are presented
 * side-by-side with a list of items in a {@link ItemListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ItemDetailFragment}.
 */
public class FormFactorFragmentActivity extends FragmentActivity
{    
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private FragmentActivity mActivity;
    
    private int mDrawerResourceID;

	protected FormFactorDb formFactorDb;

	private DrawerItem[] gatherDrawerItems()
	{
        String[] textRefs = getResources().getStringArray(R.array.drawer_items_text);
        
        TypedArray imageRefs = getResources().obtainTypedArray(R.array.drawer_items_image);

		DrawerItem[] items = new DrawerItem[textRefs.length];
        for(int i = 0; i < textRefs.length; i++)
        {
        	DrawerItem item = new DrawerItem(imageRefs.getDrawable(i), textRefs[i]);
        	items[i] = item;
        }
        
        imageRefs.recycle();
        
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

        mDrawerLayout = (DrawerLayout) findViewById(mDrawerResourceID);
        mDrawerToggle = new ActionBarDrawerToggle(
			mActivity,
	        mDrawerLayout,
            R.drawable.ic_navigation_drawer,
            R.string.action_open,
            R.string.action_close)
        {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        
        mDrawerList = (ListView)this.mActivity.findViewById((R.id.fragment_menu));
        
        mDrawerList.setAdapter(new DrawerItemArrayAdapter(this, R.layout.drawer_list_item, gatherDrawerItems()));
        
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

	private void selectItem(int position)
	{
	    mDrawerList.setItemChecked(position, true);
	    mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	public void setData(FragmentActivity activity, int drawerResourceID)
	{
		this.mActivity = activity;
		mDrawerResourceID = drawerResourceID;
	}
	
    private class DrawerItemClickListener implements ListView.OnItemClickListener
	{
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	    {
	        selectItem(position);
	    }
	}
}
