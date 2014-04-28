package com.rhcloud.jop.formfactor.domain;

public enum OpenImportActionType
{
	OpenCreate(1),
	OpenComplete(2), 
	ImportComplete(3),
	ImportCreate(4);
	
	private long index = 0;
	
	private OpenImportActionType(long i)
	{
		this.index = i;
	}
	
	public long GetIndex()
	{
		return index;
	}
	
	public static OpenImportActionType GetByIndex(long i)
	{
		OpenImportActionType[] types = OpenImportActionType.values();
		
		for(OpenImportActionType type : types)
		{
			if(type.GetIndex() == i)
			{
				return type;
			}
		}

		return OpenImportActionType.OpenCreate;
	}
}
