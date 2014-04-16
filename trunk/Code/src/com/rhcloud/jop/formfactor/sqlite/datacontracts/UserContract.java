
package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class UserContract extends BaseTable
{
	public static final String TABLE_NAME = "tUser";

	public static final BaseColumn Username = new BaseColumn("sUsername", BaseColumn.TextType);
	public static final BaseColumn Email = new BaseColumn("sEmail", BaseColumn.TextType);
	
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