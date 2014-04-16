package com.rhcloud.jop.formfactor.common;

import android.util.Log;

public class SQLiteHelper
{
	public static long logUpdate(String TAG_NAME, long returnedValue)
	{
		Log.d(TAG_NAME, "SQLite processed update with response of " + returnedValue);
		
		return returnedValue;
	}
	
	public static long logDelete(String TAG_NAME, long returnedValue)
	{
		Log.d(TAG_NAME, "SQLite processed delete with response of " + returnedValue);
		
		return returnedValue;
	}
	
	public static long logInsert(String TAG_NAME, long returnedValue)
	{
		Log.d(TAG_NAME, "SQLite processed insert with response of " + returnedValue);
		
		return returnedValue;
	}
}
