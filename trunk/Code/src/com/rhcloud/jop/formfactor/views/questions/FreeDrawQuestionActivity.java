package com.rhcloud.jop.formfactor.views.questions;

import java.util.HashSet;

import com.rhcloud.jop.formfactor.R;
import com.rhcloud.jop.formfactor.common.ActivityHelper;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.dal.lite.FormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.services.FormService;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.views.BundleKeys;
import com.rhcloud.jop.formfactor.views.FormFactorFragmentActivity;
import com.rhcloud.jop.formfactor.views.MainActivityFragment.DrawerListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class FreeDrawQuestionActivity extends FormFactorFragmentActivity implements ActionBar.TabListener, DrawerListener, OnTouchListener, OnFocusChangeListener
{
	private long mQuestionID = 0;
	private com.rhcloud.jop.formfactor.domain.FreeDrawQuestion mQuestion;
	public static FreeDrawQuestionView mFreeDrawCanvas;
	private ImageButton currPaint;
	private Bitmap mExistingBitmap;
	
	private HashSet<Integer> mHasReceivedFocus = new HashSet<Integer>();
	
	public FreeDrawQuestionActivity()
	{
		super.setData(this, R.id.activity_free_draw_question);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.activity_free_draw_question);
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
        	if(savedInstanceState.containsKey(BundleKeys.QuestionID))
        	{
            	this.mQuestionID = savedInstanceState.getLong(BundleKeys.QuestionID, 0);
        		
        		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
        		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
        		FormService formService = new FormService(dataContext);
        		
            	this.mQuestion = (com.rhcloud.jop.formfactor.domain.FreeDrawQuestion)formService.GetQuestionByID(this.mQuestionID); 
        	}
        	
        	if(savedInstanceState.containsKey(BundleKeys.FreeDrawImage))
        	{
            	this.mExistingBitmap = (Bitmap)savedInstanceState.getParcelable(BundleKeys.FreeDrawImage);
        	}
        }
        
        FreeDrawQuestionActivity.mFreeDrawCanvas = (FreeDrawQuestionView)findViewById(R.id.activity_free_draw_canvas);
        
        if(this.mExistingBitmap != null)
        {
        	FreeDrawQuestionActivity.mFreeDrawCanvas.setExistingBitmap(this.mExistingBitmap);
        }
        
        ImageButton blackPaint = (ImageButton)findViewById(R.id.activity_free_draw_color_black);
        
        currPaint = blackPaint;
        
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.palette_rectangle_pressed));
        
		FrameLayout frameLayout = (FrameLayout)this.findViewById(R.id.activity_free_draw_question_container);
		
		ActivityHelper.setupForKeyboardHide(this, frameLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}
	
	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		
		if(bundle != null)
		{
			bundle.putLong(BundleKeys.ResponseChoiceID, this.mQuestionID);
		}
	}
	
	public void clearClicked(View v)
	{
		AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
		newDialog.setTitle("Clear Drawing");
		newDialog.setMessage("Are you sure you want to clear your drawing?");
		
		newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int which)
		    {
		    	FreeDrawQuestionActivity.mFreeDrawCanvas.clearDrawing();
		        dialog.dismiss();
		    }
		});
		
		newDialog.setNegativeButton("No", new DialogInterface.OnClickListener()
		{
		    public void onClick(DialogInterface dialog, int which)
		    {
		        dialog.cancel();
		    }
		});
		
		newDialog.show();
	}
	
	public void saveDrawing()
	{
		
	}
	
	public void pencilClicked(View v)
	{
		
	}
	
	public void eraserClicked(View v)
	{
		if(!FreeDrawQuestionActivity.mFreeDrawCanvas.isErasing())
		{
			FreeDrawQuestionActivity.mFreeDrawCanvas.setErase(true);
		}
	}
	
	public void paintClicked(View v)
	{
		ImageButton pressed = (ImageButton)v;
		String color = v.getTag().toString();
		
		if(color.substring(0, 1).contains("#"))
		{
			FreeDrawQuestionActivity.mFreeDrawCanvas.setColor(color);
			pressed.setImageDrawable(getResources().getDrawable(R.drawable.palette_rectangle_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.palette_rectangle));
			
			currPaint = pressed;
		}
		
		FreeDrawQuestionActivity.mFreeDrawCanvas.setErase(false);
	}
	
	private void saveCurrentState()
	{
		if(this.mQuestion != null)
		{
    		UnitOfWork unitOfWork = new UnitOfWork(FormFactorDb.getInstance(this));
    		FormFactorDataContext dataContext = new FormFactorDataContext(unitOfWork);
    		
    		if(this.validateInput(false))
    		{
    			// TODO: Validate form
    		}

    		// TODO: Update question settings
		}
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
		case R.id.activity_free_draw_question_cancel:
			this.mQuestion = null;
			this.mQuestionID = 0;
			
			this.finish();
			break;
		case R.id.activity_free_draw_question_save:
			if(this.validateInput(true))
			{
				this.saveCurrentState();
				this.finish();
			}
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

	@Override
	public void onFocusChange(View v, boolean hasFocus)
	{
		if(!hasFocus)
		{
			this.validateInput(true);
		}
		else
		{
			if(!this.mHasReceivedFocus.contains(v.getId()))
			{
				this.mHasReceivedFocus.add(v.getId());
			}
		}
	}
	
	private boolean validateInput(boolean displayResults)
	{
		Result result = new Result();
		
		// TODO: Write validation logic
		
		return result.Success;
	}
}
