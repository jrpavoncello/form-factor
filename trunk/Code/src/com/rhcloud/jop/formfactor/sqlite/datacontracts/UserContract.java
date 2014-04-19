
package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserContract extends BaseTable
{
	public final String TABLE_NAME = "tUser";

	public BaseColumn Username = new BaseColumn("sUsername", BaseColumn.TextType);
	public BaseColumn Email = new BaseColumn("sEmail", BaseColumn.TextType);

	@Override
	protected void InitTables()
	{
		Username = new BaseColumn("sUsername", BaseColumn.TextType);
		Email = new BaseColumn("sEmail", BaseColumn.TextType);
	}
	
	@Override
	protected String GetTableName()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		super.AddColumnCreate(Username);
		super.AddColumnCreate(Email);
	}
}