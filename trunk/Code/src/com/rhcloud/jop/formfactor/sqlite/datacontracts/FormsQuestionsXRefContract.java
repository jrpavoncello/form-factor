package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FormsQuestionsXRefContract extends BaseTable
{
	public static final String TABLE_NAME = "tFormsQuestionsXRef";

	public static final BaseColumn FormID = new BaseColumn("iFormID", BaseColumn.IntegerType);
	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	
	@Override
	protected String GetTableName ()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{		
		super.AddColumnCreate(FormID);
		super.AddColumnCreate(QuestionID);
	}
}

