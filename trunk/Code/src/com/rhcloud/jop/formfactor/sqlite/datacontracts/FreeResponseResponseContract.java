package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FreeResponseResponseContract extends BaseTable
{
	public final String TABLE_NAME = "tFreeResponseResponse";

	public BaseColumn QuestionResponseID;
	public BaseColumn Response;
	
	@Override
	protected void InitTables()
	{
		QuestionResponseID = new BaseColumn("iQuestionResponseID", BaseColumn.IntegerType);
		Response = new BaseColumn("sResponse", BaseColumn.TextType);
	}
	
	@Override
	protected String GetTableName ()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		super.AddColumnCreate(QuestionResponseID);
		super.AddColumnCreate(Response);
	}
}