package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FreeDrawResponseContract extends BaseTable
{
	public final String TABLE_NAME = "tFreeDrawResponse";

	public BaseColumn QuestionResponseID;
	public BaseColumn Drawing;
	
	@Override
	protected void InitTables()
	{
		QuestionResponseID = new BaseColumn("iQuestionResponseID", BaseColumn.IntegerType);
		Drawing = new BaseColumn("blDrawing", BaseColumn.BlobType);
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
		super.AddColumnCreate(Drawing);
	}
}