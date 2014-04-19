package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class MultipleChoiceQuestionContract extends BaseTable
{
	public final String TABLE_NAME = "tMultipleChoiceQuestion";
	
	public BaseColumn QuestionID;
	public BaseColumn MinResponses;
	public BaseColumn MaxResponses;
	
	@Override
	protected void InitTables()
	{
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
		MinResponses = new BaseColumn("iMinResponses", BaseColumn.IntegerType);
		MaxResponses = new BaseColumn("iMaxResponses", BaseColumn.IntegerType);
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
		super.AddColumnCreate(MinResponses);
		super.AddColumnCreate(MaxResponses);
	}
}