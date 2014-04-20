package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserMultipleChoiceResponsesContract extends BaseTable
{
	public final String TABLE_NAME = "tUserMultipleChoiceResponses";

	public BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public BaseColumn ChoiceID = new BaseColumn("iChoiceID", BaseColumn.IntegerType);

	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
		ChoiceID = new BaseColumn("iChoiceID", BaseColumn.IntegerType);
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
		super.AddColumnCreate(ChoiceID);
	}
}
