package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class QuestionTypeContract extends BaseTable
{
	public static String TABLE_NAME = "tQuestionType";

	public static BaseColumn Name;
	
	@Override
	protected void InitTables()
	{
		Name = new BaseColumn("sName", BaseColumn.TextType);
	}
	
	public QuestionTypeContract()
	{
		super(false);
	}
	
	@Override
	protected String GetTableName ()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		
		super.AddColumnCreate(Name);
	}
}
