package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class QuestionContract extends BaseTable
{
	public static final String TABLE_NAME = "tQuestion";

	public static final BaseColumn Number = new BaseColumn("iNumber", BaseColumn.IntegerType);
	public static final BaseColumn FormID = new BaseColumn("iFormID", BaseColumn.IntegerType);
	public static final BaseColumn Type = new BaseColumn("iType", BaseColumn.IntegerType);
	public static final BaseColumn Question = new BaseColumn("sQuestion", BaseColumn.TextType);
	public static final BaseColumn Image = new BaseColumn("blImage", BaseColumn.BlobType);
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
		super.AddColumnCreate(Number);
		super.AddColumnCreate(FormID);
		super.AddColumnCreate(Type);
		super.AddColumnCreate(Question);
		super.AddColumnCreate(Image);
		super.AddColumnCreate(MinResponses);
		super.AddColumnCreate(MaxResponses);
	}
}