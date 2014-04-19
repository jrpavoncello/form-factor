package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public class UserActivityXRef extends BaseTable
{
	public final String TABLE_NAME = "tUserActivityXRef";

	public BaseColumn UserID;
	public BaseColumn ActivityID;

	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
		ActivityID = new BaseColumn("iActivityID", BaseColumn.IntegerType);
	}
	
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