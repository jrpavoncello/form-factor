package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class MainActivityFragment extends Fragment
{
	private DrawerListener mDrawerListener;
	
	public static MainActivityFragment newInstance()
	{
		MainActivityFragment newFragment = new MainActivityFragment();
		
        return newFragment;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_activity_main, container, false);
        
        return rootView;
    }
    
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        
        // Verify that the host activity implements the callback interface
        try
        {
        	mDrawerListener = (DrawerListener) activity;
        }
        catch(ClassCastException e)
        {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement DrawerListener");
        }
    }
	
	public static interface DrawerListener
	{
		public void prepareDrawerLayout(Menu menu);
	}
}
