package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public class UserActivityXRef extends BaseTable
{
	public static final String TABLE_NAME = "tUserActivityXRef";

	public static final BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public static final BaseColumn ActivityID = new BaseColumn("iActivityID", BaseColumn.IntegerType);
	
	@Override
	protected String GetTableName()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		super.AddColumnCreate(UserID);
		super.AddColumnCreate(ActivityID);
	}
}