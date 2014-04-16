package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

class IntegerType implements IColumnType
{
	@Override
	public String GetDefinition()
	{
		return "INTEGER";
	}
}