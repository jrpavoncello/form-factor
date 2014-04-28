package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FormsContract extends BaseTable
{
	public final String TABLE_NAME = "tForms";
	
	public BaseColumn UserID;
	public BaseColumn Title;
	public BaseColumn Description;
	public BaseColumn Logo;
	public BaseColumn ExternalID;
	
	@Override
	protected void InitTables()
	{
		UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
		Title = new BaseColumn("sTitle", BaseColumn.TextType);
		Description = new BaseColumn("sDescription", BaseColumn.TextType);
		Logo = new BaseColumn("iLogoID", BaseColumn.IntegerType);
		ExternalID = new BaseColumn("iExternalID", BaseColumn.IntegerType);
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
		super.AddColumnCreate(Title);
		super.AddColumnCreate(Description);
		super.AddColumnCreate(Logo);
		super.AddColumnCreate(ExternalID);
	}
}