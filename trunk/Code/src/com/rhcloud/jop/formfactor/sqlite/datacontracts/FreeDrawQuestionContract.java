package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FreeDrawQuestionContract extends BaseTable
{
	public final String TABLE_NAME = "tFreeDrawQuestion";

	public BaseColumn QuestionID;
	
	@Override
	protected void InitTables()
	{
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	}
	
	@Override
	protected String GetTableName ()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		super.AddColumnCreate(QuestionID);
	}
}