package com.rhcloud.jop.formfactor.views;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.rhcloud.jop.formfactor.R;

public class SettingsActivity extends PreferenceActivity
{
	
	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_settings); 
	} 

    /**
     * Populate the activity with the top-level headers.
     */
    @Override
    public void onBuildHeaders(List<Header> target)
    {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }
		 
	public static class SettingsFragment extends PreferenceFragment
	{ 
		@Override 
		public void onCreate(Bundle savedInstanceState)
		{ 
			super.onCreate(savedInstanceState); 
		 
			// Load the preferences from an XML resource 
			addPreferencesFromResource(R.xml.pref_general); 
		} 
	}

	public static class SettingsFragmentInner extends PreferenceFragment
	{ 
		@Override 
		public void onCreate(Bundle savedInstanceState)
		{ 
			super.onCreate(savedInstanceState); 
		
			// Load the preferences from an XML resource 
			addPreferencesFromResource(R.xml.pref_general); 
		} 
	} 
}
