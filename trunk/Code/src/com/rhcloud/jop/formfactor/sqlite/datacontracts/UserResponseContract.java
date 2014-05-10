
package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserResponseContract extends BaseTable
{
	public final String TABLE_NAME = "tUserResponse";

	public BaseColumn UserID;

	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
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
	}
}