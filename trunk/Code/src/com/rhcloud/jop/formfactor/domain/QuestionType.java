package com.rhcloud.jop.formfactor.domain;

public enum QuestionType
{
	None(1),
	BestChoice(2), 
	MultipleChoice (3),
	FreeResponse (4),
	FreeDraw (5);
	
	private long index = 0;
	
	private QuestionType(long i)
	{
		this.index = i;
	}
	
	public long GetIndex()
	{
		return index;
	}
	
	public static QuestionType GetByIndex(long i)
	{
		QuestionType[] types = QuestionType.values();
		
		for(QuestionType type : types)
		{
			if(type.GetIndex() == i)
			{
				return type;
			}
		}

		return QuestionType.None;
	}
}
