package com.rhcloud.jop.formfactor.sqlite.datacontracts;

import java.util.ArrayList;

import com.rhcloud.jop.formfactor.sqlite.datacontracts.types.BaseColumn;

import android.provider.BaseColumns;

public abstract class BaseTable implements BaseColumns
{
	private boolean isAutoIncrement = true;
	private final ArrayList<BaseColumn> columns = new ArrayList<BaseColumn>(6);
	
	public BaseTable()
	{
		this.InitTables();
		this.AddColumns();
	}
	
	public BaseTable(boolean isAutoIncrement)
	{
		this();
		this.isAutoIncrement = isAutoIncrement;
	}
	
	private final String GetIDSetup()
	{
		return _ID + " INTEGER PRIMARY KEY" + (isAutoIncrement ? " AUTOINCREMENT" : ""); 
	}
	
	public final String GetCmdCreateTable()
	{
		String setup = "CREATE TABLE " + this.GetTableName() + " (" + this.GetIDSetup() + ",";
		
		if(!columns.isEmpty())
		{
			for(BaseColumn column : columns)
			{
				if(column.Index != 0)
				{
					setup += column.GetName() + " " + column.GetType().GetDefinition() + ", ";
				}
			}
			
			setup = setup.substring(0, setup.length() - 2) + ")";
		}
		else
		{
			setup = null;
		}
		
		return setup;
	}
	
	public final String GetCmdDeleteIfExists()
	{
	    return "DROP TABLE IF EXISTS " +  this.GetTableName();
	}
	
	protected final void AddColumnCreate(BaseColumn column)
	{
		if(columns.isEmpty())
		{
			//Although SQLite defines a default _ID column, we have to add it here because of our column indexes
			BaseColumn IdColumn = new BaseColumn("_ID", BaseColumn.IntegerType);
			columns.add(IdColumn);
		}
		
		column.Index = columns.size();
		columns.add(column);
	}
	
	protected abstract void AddColumns();
	
	protected abstract void InitTables();
	
	protected abstract String GetTableName();
}
