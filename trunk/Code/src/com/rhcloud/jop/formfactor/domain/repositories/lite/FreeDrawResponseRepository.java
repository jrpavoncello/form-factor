package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.FreeDrawResponse;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeDrawResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeDrawResponseRepository implements IFreeDrawResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.FreeDrawResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public FreeDrawResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(FreeDrawResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.FreeDrawResponseContract.QuestionResponseID.GetName(), response.ID);
			values.put(tables.FreeDrawResponseContract.Drawing.GetName(), response.Drawing);
	
			response.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.FreeDrawResponseContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public FreeDrawResponse GetByQuestionResponseID(FreeDrawResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FreeDrawResponseContract.QuestionResponseID.GetName() + " = " + response.ID;
			
			String query = "SELECT * FROM " + tables.FreeDrawResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				response.FreeDrawResponseID = cursor.getInt(0);
				response.Drawing = cursor.getBlob(tables.FreeDrawResponseContract.Drawing.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return response;
	}

	@Override
	public void DeleteByQuestionResponseID(long ID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.FreeDrawResponseContract.QuestionResponseID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeDrawResponseContract.TABLE_NAME, whereClause, new String[] { "" + ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByQuestionResponseIDsNotIn(Long[] IDs, long responseID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.FreeDrawResponseContract.QuestionResponseID.GetName() + " NOT IN (";
			
			String[] args = new String[IDs.length + 1];
			
			for(int i = 0; i < IDs.length; i++)
			{
				if(i != IDs.length - 1)
				{
					whereClause += "?, ";
				}
				else
				{
					whereClause += "?)";
				}
				
				args[i] = "" + IDs[i];
			}
			
			args[IDs.length] = "" + responseID;

			whereClause += " AND " + tables.FreeDrawResponseContract.TABLE_NAME + "." + tables.FreeDrawResponseContract.QuestionResponseID.GetName() + " IN ( SELECT " + tables.QuestionResponseContract._ID + " FROM " + tables.QuestionResponseContract.TABLE_NAME + " WHERE " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeDrawResponseContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(FreeDrawResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();

			values.put(tables.FreeDrawResponseContract.QuestionResponseID.GetName(), response.FreeDrawResponseID);
			values.put(tables.FreeDrawResponseContract.Drawing.GetName(), response.Drawing);
	
			String whereClause = tables.FreeDrawResponseContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.FreeDrawResponseContract.TABLE_NAME, values, whereClause, new String[] { "" + response.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByUserResponseID(long ID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.FreeDrawResponseContract.QuestionResponseID.GetName() + " IN (";
			
			whereClause += " SELECT " + tables.QuestionResponseContract._ID + " FROM " + tables.QuestionResponseContract.TABLE_NAME + " WHERE " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ?)";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeDrawResponseContract.TABLE_NAME, whereClause, new String[] { "" + ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
