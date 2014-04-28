package com.rhcloud.jop.formfactor.views;

import java.util.List;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.UserService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;

public class SettingsActivity extends PreferenceActivity implements OnPreferenceChangeListener
{
	private static ListPreference mUserPreference;
	private static SettingsActivity mSettingsActivity;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState)
	{
		mSettingsActivity = this;
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
    
    public boolean addNewUser(User user)
    {
        CharSequence[] entries = this.mUserPreference.getEntries();
        CharSequence[] values = this.mUserPreference.getEntryValues();
        
        int size = entries.length;
        
        entries = new CharSequence[size + 1];
        values = new CharSequence[size + 1];
        
        entries[size] = user.Username;
        
        String userJSON = user.Serialize();
        values[size] = userJSON;
        
        this.mUserPreference.setEntries(entries);
        this.mUserPreference.setEntryValues(values);
		
		return this.setUserPreference(user.Username, user.Password);
    }
    
    private boolean setUserPreference(String username, char[] password)
    {
    	boolean result = false;
    	
		Editor settings = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
		
		try
		{
			settings.putString("pref_username", username);
			settings.putString("pref_password", new String(password));
			
			result = true;
		}
		catch(Exception ex)
		{

		}
		
		if(result)
		{
			settings.commit();
		}
		
		return result;
    }
    
    @Override
    public boolean onPreferenceChange(Preference preference, Object value)
    {
        String selectedUserAndPassHash = value.toString();
    	String selectedPassHash = "";
    	String selectedUsername = "";

    	boolean first = true;
    	for(String part : selectedUserAndPassHash.split(" "))
    	{
    		if(first)
    		{
    			selectedUsername = part;
    			first = false;
    		}
    		else
    		{
    			selectedPassHash += part;
    		}
    	}

        ListPreference userPref = (ListPreference)preference;
        CharSequence[] values = userPref.getEntryValues();
        CharSequence[] entries = userPref.getEntries();
        
        int index = 0;
        for(; index < values.length; index++)
        {
        	String userAndPassHash = values[index].toString();
        	String passHash = "";
        	String username = "";

        	first = true;
        	for(String part : userAndPassHash.split(" "))
        	{
        		if(first)
        		{
        			username = part;
        			first = false;
        		}
        		else
        		{
        			passHash += part;
        		}
        	}
        	
        	if(passHash.equals(selectedPassHash) && username.equals(selectedUsername))
        	{
        		break;
        	}
        }

        if(index != values.length)
        {
        	String username = entries[index].toString();
        	char[] pass = selectedPassHash.toCharArray();
			this.setUserPreference(username, pass);

			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(SettingsActivity.mSettingsActivity));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			UserService userService = new UserService(dataContext);
			
			userService.SetDefaultUser(userService.GetUser(username, pass));
	
	        return true;
        }
        else
        {
        	return false;
        }
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
			
			SettingsActivity.mUserPreference = (ListPreference)findPreference("pref_user");

			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(SettingsActivity.mSettingsActivity));
			FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
			
			List<User> users = dataContext.GetUserRepository().GetAll();
			
			int size = users.size();

	        CharSequence[] entries = new CharSequence[size];
	        CharSequence[] values = new CharSequence[size];
			
			for (int i = 0; i < size; i++)
			{
				User user = users.get(i);
				
				entries[i] = user.Username;
				
				//This was done because I cannot get the entry key when the item is selected, only the value
				//which means that when there are two of the same password, the dialog will get confused
				values[i] = user.Username + " " + new String(user.Password);
			}
			
			SettingsActivity.mUserPreference.setEntries(entries);
			SettingsActivity.mUserPreference.setEntryValues(values);
			SettingsActivity.mUserPreference.setOnPreferenceChangeListener(SettingsActivity.mSettingsActivity);
		} 
	} 
}
