package com.rhcloud.jop.formfactor.views.questions;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.views.BundleKeys;
import com.rhcloud.jop.formfactor.views.FormFactorFragmentActivity;
import com.rhcloud.jop.formfactor.views.MainMenuActivityFragment.DrawerListener;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

public class MultipleChoiceResponseEditActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener, OnFocusChangeListener, OnTouchListener
{
	private long mResponseChoiceID = 0;
	private ResponseChoice mResponseChoice;
	private EditText mResponse;
	
	public MultipleChoiceResponseEditActivity()
	{
		super.setData(this, R.id.activity_multiple_choice_response_edit);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		setContentView(R.layout.activity_multiple_choice_response_edit);
		super.onCreate(savedInstanceState);
        
        this.setTitle(this.getResources().getString(R.string.drawer_menu_title));
        
        if(savedInstanceState == null)
        {
            Intent intent = this.getIntent();
            if(intent != null)
        	{
                savedInstanceState = intent.getExtras();
        	}
        }
        
        if(savedInstanceState != null)
        {            
        	if(savedInstanceState.containsKey(BundleKeys.ResponseChoiceID))
        	{
            	this.mResponseChoiceID = savedInstanceState.getLong(BundleKeys.ResponseChoiceID, 0);
        	}
        }
		
		this.mResponse = (EditText)this.findViewById(R.id.activity_multiple_choice_response_edit_response);
		this.mResponse.setOnFocusChangeListener(this);

		FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.activity_multiple_choice_response_edit_container);
		
		ActivityHelper.setupForKeyboardHide(this, frameLayout);
	}
	
	@Override
	public void onResume()
	{
		if(this.mResponseChoiceID != 0)
		{
			UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
    		
    		this.mResponseChoice = dataContext.GetResponseChoiceRepository().GetByID(this.mResponseChoiceID);
    		
    		if(this.mResponseChoice != null && this.mResponse.getText().toString().equals(""))
    		{
    			this.mResponse.setText(this.mResponseChoice.Choice);
    		}
		}
		
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		
		if(bundle != null)
		{
			bundle.putLong(BundleKeys.ResponseChoiceID, this.mResponseChoiceID);
		}
	}
	
	private void saveCurrentState()
	{
		if(this.mResponseChoice != null)
		{
    		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);

    		this.mResponseChoice.Choice = this.mResponse.getText().toString();
    		
        	dataContext.GetResponseChoiceRepository().UpdateSettings(this.mResponseChoice.ID, this.mResponseChoice.Choice);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void onActionClicked(View v)
	{
		int id = v.getId();
		
		switch(id)
		{
		case R.id.activity_multiple_choice_response_edit_cancel:
			this.mResponseChoice = null;
			this.mResponseChoiceID = 0;
			
			this.finish();
			break;
		case R.id.activity_multiple_choice_response_edit_save:
			this.saveCurrentState();
			this.finish();
			break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		InputMethodManager inputMethodManager = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
	    
	    return false;
	}
	
	private void editTextChangeFocus(EditText editText, boolean hasFocus)
	{
		if(!hasFocus)
		{
			InputMethodManager mgr = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		int id = v.getId();
		
		switch(id)
		{
		case R.id.activity_multiple_choice_response_edit_response:
			
			this.editTextChangeFocus((EditText)v, hasFocus);
			break;
		}
	}

	@Override
	public void prepareDrawerLayout(Menu menu) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

}
