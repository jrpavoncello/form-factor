package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class QuestionContract extends BaseTable
{
	public final String TABLE_NAME = "tQuestion";

	public BaseColumn Number;
	public BaseColumn FormID;
	public BaseColumn Type;
	public BaseColumn Question;
	public BaseColumn Image;
	
	@Override
	protected void InitTables()
	{
		Number = new BaseColumn("iNumber", BaseColumn.IntegerType);
		FormID = new BaseColumn("iFormID", BaseColumn.IntegerType);
		Type = new BaseColumn("iType", BaseColumn.IntegerType);
		Question = new BaseColumn("sQuestion", BaseColumn.TextType);
		Image = new BaseColumn("blImage", BaseColumn.BlobType);
	}
	
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
	}
}