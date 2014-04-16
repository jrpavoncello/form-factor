package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

public class UserActivityTypeContract extends BaseTable
{
	public static final String TABLE_NAME = "tUserActivityType";

	public static final BaseColumn Action = new BaseColumn("sAction", BaseColumn.TextType);
	
	public UserActivityTypeContract()
	{
		super(false);
	}
	
	@Override
	protected String GetTableName()
	{
		return TABLE_NAME;
	}
	
	@Override
	protected void AddColumns()
	{
		super.AddColumnCreate(Action);
	}
}