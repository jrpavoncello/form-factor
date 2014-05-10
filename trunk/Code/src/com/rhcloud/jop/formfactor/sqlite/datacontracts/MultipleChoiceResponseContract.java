package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class MultipleChoiceResponseContract extends BaseTable
{
	public final String TABLE_NAME = "tMultipleChoiceResponse";
	
	public BaseColumn QuestionResponseID;
	public BaseColumn ResponseChoiceID;
	
	@Override
	protected void InitTables()
	{
		QuestionResponseID = new BaseColumn("iQuestionResponseID", BaseColumn.IntegerType);
		ResponseChoiceID = new BaseColumn("iResponseChoiceID", BaseColumn.IntegerType);
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
		super.AddColumnCreate(ResponseChoiceID);
	}
}