package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserOpenResponsesContract extends BaseTable
{
	public static final String TABLE_NAME = "tUserOpenResponses";

	public static final BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public static final BaseColumn Response = new BaseColumn("sResponse", BaseColumn.TextType);
	
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
