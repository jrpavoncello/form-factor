package com.rhcloud.jop.formfactor.views;

import com.rhcloud.jop.formfactor.domain.Form;

import android.graphics.drawable.Drawable;

public class FormListItem
{
	Form mForm;
	
	public FormListItem(Form form)
	{
		mForm = form;
	}
	
	public String getItemText()
	{
	      return mForm.Title;
	}
}
