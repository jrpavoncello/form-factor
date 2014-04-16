package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

class BlobType implements IColumnType
{
	@Override
	public String GetDefinition()
	{
		return "BLOB";
	}
}
