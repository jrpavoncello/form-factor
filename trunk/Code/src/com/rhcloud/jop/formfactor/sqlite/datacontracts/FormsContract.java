package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public final class FormsContract extends BaseTable
{
	public static final String TABLE_NAME = "tForms";
	
	public static final BaseColumn UserID = new BaseColumn("iUserID", BaseColumn.IntegerType);
	public static final BaseColumn Title = new BaseColumn("sTitle", BaseColumn.TextType);
	public static final BaseColumn Description = new BaseColumn("sDescription", BaseColumn.TextType);
	public static final BaseColumn Logo = new BaseColumn("iLogoID", BaseColumn.IntegerType);

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
	}
}