package com.rhcloud.jop.formfactor.sqlite;

import com.rhcloud.jop.formfactor.domain.Connection;
import com.rhcloud.jop.formfactor.domain.IDatabase;
import com.rhcloud.jop.formfactor.views.MainMenuActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class FormFactorDb implements IDatabase
{
	private static FormFactorDb formFactorDb;
	private SQLiteDatabase DB;
	private boolean isReady;
	
	private FormFactorDb() { }
	
	public static FormFactorDb getInstance(Context context)
	{
		if (formFactorDb == null)
		{		
			formFactorDb = new FormFactorDb();
			formFactorDb.isReady = false;
			
			// Open DB in an AsyncTask, since it may take a while
			formFactorDb.new OpenDbAsyncTask().execute(context);
		}
		
		return formFactorDb;
	}
	
	public boolean initialized()
	{
		return isReady;
	}
	
    private class OpenDbAsyncTask extends AsyncTask<Context, Void, Void>
    {
		@Override
		protected Void doInBackground(Context... params) 
		{
			FormFactorDbHelper dbHelper = new FormFactorDbHelper(params[0]);
			DB = dbHelper.getWritableDatabase();
			isReady = true;

			try
			{
				MainMenuActivity activity = (MainMenuActivity)params[0];
				
				if(activity != null)
				{
					MainMenuActivity.setData();
				}
			}
			catch(Exception ex)
			{
				
			}
			
			return null;
		}
    }
    
    public SQLiteDatabase getDB()
    {
    	if(!isReady)
    	{
    		return null;
    	}
    	
    	return DB;
    }

	@Override
	public void BeginTransaction()
	{
    	if(isReady)
    	{
    		DB.beginTransaction();
    	}
    	else
    	{
    		// TODO: Report error to log
    	}
	}

	@Override
	public void CommitTransaction()
	{
    	if(isReady)
    	{
    		DB.setTransactionSuccessful();
    		DB.endTransaction();
    	}
    	else
    	{
    		// TODO: Report error to log
    	}
	}

	@Override
	public void AbortTransaction()
	{
		if(isReady)
		{
			DB.endTransaction();
		}
		else
		{
			//TODO: Report in error log
		}
	}

	@Override
	public Connection OpenConnection()
	{
		//SQLite has no connections
		return null;
	}

	@Override
	public void CloseConnection()
	{
		//SQLite has no connections
	}

	@Override
	public boolean IsInTransaction()
	{
		return this.DB.inTransaction();
	}

	@Override
	public boolean IsConnectionOpen()
	{
		//SQLite has no connections - we'll say it's always open
		return true;
	}
}
