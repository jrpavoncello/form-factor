package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FreeResponseQuestionContract extends BaseTable
{
	public final String TABLE_NAME = "tFreeResponseQuestion";

	public BaseColumn QuestionID;
	public BaseColumn MinLength;
	public BaseColumn MaxLength;
	public BaseColumn Lines;
	
	@Override
	protected void InitTables()
	{
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
		MinLength = new BaseColumn("iMinLength", BaseColumn.IntegerType);
		MaxLength = new BaseColumn("iMaxLength", BaseColumn.IntegerType);
		Lines = new BaseColumn("iLines", BaseColumn.IntegerType);
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
		super.AddColumnCreate(MinLength);
		super.AddColumnCreate(MaxLength);
		super.AddColumnCreate(Lines);
	}
}