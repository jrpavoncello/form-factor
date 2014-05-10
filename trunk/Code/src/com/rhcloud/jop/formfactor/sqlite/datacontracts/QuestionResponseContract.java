package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class QuestionResponseContract extends BaseTable
{
	public final String TABLE_NAME = "tQuestionResponse";

	public BaseColumn UserResponseID;
	public BaseColumn QuestionID;
	
	@Override
	protected void InitTables()
	{
		UserResponseID = new BaseColumn("iUserResponseID", BaseColumn.IntegerType);
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
		super.AddColumnCreate(UserResponseID);
		super.AddColumnCreate(QuestionID);
	}
}