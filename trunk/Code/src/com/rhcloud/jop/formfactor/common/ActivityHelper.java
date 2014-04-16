package com.rhcloud.jop.formfactor.common;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class ActivityHelper
{
	public static void setupForKeyboardHide(OnTouchListener listener, ViewGroup viewGroup)
	{
		for(int i = 0; i < viewGroup.getChildCount(); i++)
		{
			if(!(viewGroup.getChildAt(i) instanceof EditText))
			{
				View view = viewGroup.getChildAt(i);
				view.setOnTouchListener(listener);
			}
			else
			{
				if(viewGroup.getChildAt(i) instanceof ViewGroup)
				{
					setupForKeyboardHide(listener, (ViewGroup)viewGroup.getChildAt(i));
				}
			}
		}
	}
}
