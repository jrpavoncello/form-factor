package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class ResponseChoicesContract extends BaseTable
{
	public final String TABLE_NAME = "tResponseChoices";

	public BaseColumn QuestionID;
	public BaseColumn Choice;
	
	@Override
	protected void InitTables()
	{
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
		Choice = new BaseColumn("sChoice", BaseColumn.TextType);
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
		super.AddColumnCreate(Choice);
	}
}
