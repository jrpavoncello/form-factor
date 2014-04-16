package com.rhcloud.jop.formfactor.domain;

public enum UserActivityType
{
	CreatedForm("Created form", 1),
	CompletedForm("Completed form", 2);
	
	private long index = 0;
	private String action = "";
	
	private UserActivityType(String action, long i)
	{
		this.index = i;
		this.action = action;
	}
	
	public long GetIndex()
	{
		return index;
	}
	
	public String GetAction()
	{
		return this.action;
	}
}
