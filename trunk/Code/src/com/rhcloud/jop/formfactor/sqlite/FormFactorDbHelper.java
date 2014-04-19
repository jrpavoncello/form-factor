package com.rhcloud.jop.formfactor.sqlite;

import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.UserActivityType;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.BaseTable;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.FormFactorTables;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.QuestionTypeContract;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.UserActivityTypeContract;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public final class FormFactorDbHelper extends SQLiteOpenHelper
{        
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FormFactor.db";
    
    public BaseTable[] GetTables()
    {
    	return FormFactorTables.getInstance().GetAllTables();
    }
	
    private Context context;
	
    public FormFactorDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		FormFactorTables tables = FormFactorTables.getInstance();
		BaseTable[] baseTables = tables.GetAllTables();
		
		for(BaseTable table : baseTables)
		{
			String cmdCreate = table.GetCmdCreateTable();
			db.execSQL(cmdCreate);
		}
		
		for(QuestionType questionType : QuestionType.values())
		{
			ContentValues values = new ContentValues();

			values.put(QuestionTypeContract._ID, questionType.GetIndex());
			values.put(QuestionTypeContract.Name.GetName(), questionType.name());
			
			db.insert(QuestionTypeContract.TABLE_NAME, null, values);
		}

		for(UserActivityType activityType : UserActivityType.values())
		{
			ContentValues values = new ContentValues();

			values.put(tables.UserActivityTypeContract._ID, activityType.GetIndex());
			values.put(tables.UserActivityTypeContract.Action.GetName(), activityType.name());
			
			db.insert(tables.UserActivityTypeContract.TABLE_NAME, null, values);
		}
	}

	@Override
	public void onOpen(SQLiteDatabase db)
	{
        super.onOpen(db);
        context = null;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		for(BaseTable table : GetTables())
		{
			db.execSQL(table.GetCmdDeleteIfExists());
		}
		
        onCreate(db);
	}
	
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    	onUpgrade(db, oldVersion, newVersion);
    }
}
