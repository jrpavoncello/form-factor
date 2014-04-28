
package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserContract extends BaseTable
{
	public final String TABLE_NAME = "tUser";

	public BaseColumn Username;
	public BaseColumn Email;
	public BaseColumn Password;
	public BaseColumn IsDefault;

	@Override
	protected void InitTables()
	{
		Username = new BaseColumn("sUsername", BaseColumn.TextType);
		Email = new BaseColumn("sEmail", BaseColumn.TextType);
		Password = new BaseColumn("sPassword", BaseColumn.TextType);
		IsDefault = new BaseColumn("bIsDefault", BaseColumn.IntegerType);
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
		super.AddColumnCreate(Password);
		super.AddColumnCreate(IsDefault);
	}
}