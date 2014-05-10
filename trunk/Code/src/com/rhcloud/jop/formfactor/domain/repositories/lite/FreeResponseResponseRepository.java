package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.FreeResponseResponse;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeResponseResponseRepository implements IFreeResponseResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.FreeResponseResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public FreeResponseResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(FreeResponseResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.FreeResponseResponseContract.QuestionResponseID.GetName(), response.ID);
			values.put(tables.FreeResponseResponseContract.Response.GetName(), response.Response);
	
			response.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.FreeResponseResponseContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public FreeResponseResponse GetByQuestionResponseID(FreeResponseResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FreeResponseResponseContract.QuestionResponseID.GetName() + " = " + response.ID;
			
			String query = "SELECT * FROM " + tables.FreeResponseResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				response.FreeResponseResponseID = cursor.getInt(0);
				response.Response = cursor.getString(tables.FreeResponseResponseContract.Response.Index);
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
			
			String whereClause = tables.FreeResponseResponseContract.QuestionResponseID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeResponseResponseContract.TABLE_NAME, whereClause, new String[] { "" + ID }));
			
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
			
			String whereClause = tables.FreeResponseResponseContract.QuestionResponseID.GetName() + " NOT IN (";
			
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

			whereClause += " AND " + tables.FreeResponseResponseContract.TABLE_NAME + "." + tables.FreeResponseResponseContract.QuestionResponseID.GetName() + " IN ( SELECT " + tables.QuestionResponseContract._ID + " FROM " + tables.QuestionResponseContract.TABLE_NAME + " WHERE " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeResponseResponseContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(FreeResponseResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();

			values.put(tables.FreeResponseResponseContract.QuestionResponseID.GetName(), response.FreeResponseResponseID);
			values.put(tables.FreeResponseResponseContract.Response.GetName(), response.Response);
	
			String whereClause = tables.FreeResponseResponseContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.FreeResponseResponseContract.TABLE_NAME, values, whereClause, new String[] { "" + response.ID }));
			
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
			
			String whereClause = tables.FreeResponseResponseContract.QuestionResponseID.GetName() + " IN (";
			
			whereClause += " SELECT " + tables.QuestionResponseContract._ID + " FROM " + tables.QuestionResponseContract.TABLE_NAME + " WHERE " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ?)";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceQuestionContract.TABLE_NAME, whereClause, new String[] { "" + ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
