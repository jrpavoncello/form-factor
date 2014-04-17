package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class MultipleChoiceQuestionContract extends BaseTable
{
	public static final String TABLE_NAME = "tMultipleChoiceQuestion";
	
	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public static final BaseColumn MinResponses = new BaseColumn("iMinResponses", BaseColumn.IntegerType);
	public static final BaseColumn MaxResponses = new BaseColumn("iMaxResponses", BaseColumn.IntegerType);
	
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