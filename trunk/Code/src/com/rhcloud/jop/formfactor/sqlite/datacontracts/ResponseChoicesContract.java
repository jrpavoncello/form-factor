package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class ResponseChoicesContract extends BaseTable
{
	public static final String TABLE_NAME = "tResponseChoices";

	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public static final BaseColumn Choice = new BaseColumn("sChoice", BaseColumn.TextType);
	
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
