package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

public class NullType implements IColumnType
{
	@Override
	public String GetDefinition()
	{
		return "NULL";
	}
}