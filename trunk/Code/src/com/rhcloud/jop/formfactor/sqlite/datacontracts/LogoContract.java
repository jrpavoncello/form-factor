package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class LogoContract extends BaseTable
{
	public final String TABLE_NAME = "tLogo";

	public BaseColumn Description;
	public BaseColumn Image;
	
	@Override
	protected void InitTables()
	{
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
		super.AddColumnCreate(Description);
		super.AddColumnCreate(Image);
	}
}
