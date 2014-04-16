package com.rhcloud.jop.formfactor.domain;

public enum QuestionType
{
	None(0),
	BestChoice(1), 
	MultipleChoice (2),
	OpenEnded (3),
	FreeDraw (4);
	
	private long index = 0;
	
	private QuestionType(long i)
	{
		this.index = i;
	}
	
	public long GetIndex()
	{
		return index;
	}
}
