package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FreeResponseQuestionContract extends BaseTable
{
	public static final String TABLE_NAME = "tFreeResponseQuestion";

	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public static final BaseColumn MinLength = new BaseColumn("iMinLength", BaseColumn.IntegerType);
	public static final BaseColumn MaxLength = new BaseColumn("iMaxLength", BaseColumn.IntegerType);
	public static final BaseColumn Lines = new BaseColumn("iLines", BaseColumn.IntegerType);
	
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