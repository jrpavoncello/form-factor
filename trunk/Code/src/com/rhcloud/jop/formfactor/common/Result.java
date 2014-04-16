package com.rhcloud.jop.formfactor.common;

import java.util.ArrayList;
import java.util.List;

public class Result
{
	public boolean Success = true;
	public List<String> Messages = new ArrayList<String>();
	
	public void Merge(Result result)
	{
		this.Messages.addAll(result.Messages);
		this.Success &= result.Success;
	}
	
	public String Join(String seperator)
	{
		String join = "";
		
		int size = this.Messages.size();
		for(int i = 0; i < size; i++)
		{
			String message = this.Messages.get(i);
			if(i != size - 1)
			{
				join += message += seperator;
			}
			else
			{
				join += message;
			}
		}
		
		return join;
	}
}
