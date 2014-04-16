package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserDrawResponsesContract extends BaseTable
{
	public static final String TABLE_NAME = "tUserDrawResponses";

	public static final BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public static final BaseColumn QuestionID = new BaseColumn("iQuestionID", BaseColumn.IntegerType);
	public static final BaseColumn Description = new BaseColumn("sDescription", BaseColumn.TextType);
	public static final BaseColumn Image = new BaseColumn("blImage", BaseColumn.BlobType);
	
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
