package com.rhcloud.jop.formfactor.views;

import android.graphics.drawable.Drawable;

public class DrawerItem
{
	Drawable mItemImage;
	String mItemText;
	
	public DrawerItem(Drawable drawable, String itemText)
	{
		this.mItemImage = drawable;
		mItemText = itemText;
	}
	
	public String getItemText()
	{
	      return mItemText;
	}
	
	public Drawable getImage()
	{
		return mItemImage;
	}
}
