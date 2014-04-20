package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserFreeDrawResponsesContract extends BaseTable
{
	public final String TABLE_NAME = "tUserFreeDrawResponses";

	public BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public BaseColumn Description = new BaseColumn("sDescription", BaseColumn.TextType);
	public BaseColumn Image = new BaseColumn("blImage", BaseColumn.BlobType);

	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
		QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
		Description = new BaseColumn("sDescription", BaseColumn.TextType);
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
		super.AddColumnCreate(UserID);
		super.AddColumnCreate(QuestionID);
		super.AddColumnCreate(Description);
		super.AddColumnCreate(Image);
	}
}
