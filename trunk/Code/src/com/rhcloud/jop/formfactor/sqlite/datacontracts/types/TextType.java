package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

public class TextType implements IColumnType
{
	@Override
	public String GetDefinition()
	{
		return "TEXT";
	}
}