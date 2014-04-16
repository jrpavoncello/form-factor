package com.rhcloud.jop.formfactor.views.questions;

import android.text.TextWatcher;

public class QuestionEditItem
{
	private long mID;
	private String mItem;
	private String mItemName;
	
	private TextWatcher textWatcher;
	
	public QuestionEditItem(long ID, String item, String itemName)
	{
		this.mID = ID;
		this.mItem = item;
		this.mItemName = itemName;
	}
	
	public long getID()
	{
	      return this.mID;
	}
	
	public String getItem()
	{
	      return this.mItem;
	}
	
	public String getItemName()
	{
	      return this.mItemName;
	}
	
	public void setTextWatcher(TextWatcher textWatcher)
	{
		this.textWatcher = textWatcher;
	}
	
	TextWatcher getTextWatcher()
	{
		return this.textWatcher;
	}
}
