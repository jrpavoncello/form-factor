package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class LogoContract extends BaseTable
{
	public static final String TABLE_NAME = "tLogo";

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
		super.AddColumnCreate(Description);
		super.AddColumnCreate(Image);
	}
}
