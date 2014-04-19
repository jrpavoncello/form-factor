package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserOpenResponsesContract extends BaseTable
{
	public final String TABLE_NAME = "tUserOpenResponses";

	public BaseColumn UserID;
	public BaseColumn QuestionID;
	public BaseColumn Response;

	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
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
		super.AddColumnCreate(UserID);
		super.AddColumnCreate(QuestionID);
		super.AddColumnCreate(Response);
	}
}
