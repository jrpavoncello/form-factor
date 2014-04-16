package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

class RealType implements IColumnType
{
	@Override
	public String GetDefinition()
	{
		return "REAL";
	}
}