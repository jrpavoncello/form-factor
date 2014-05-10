package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceResponse;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class MultipleChoiceResponseRepository implements IMultipleChoiceResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.MultipleChoiceResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public MultipleChoiceResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(MultipleChoiceResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.MultipleChoiceResponseContract.QuestionResponseID.GetName(), response.ID);
			values.put(tables.MultipleChoiceResponseContract.ResponseChoiceID.GetName(), response.ResponseChoiceID);
	
			response.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.MultipleChoiceResponseContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public MultipleChoiceResponse GetByQuestionResponseID(MultipleChoiceResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.MultipleChoiceResponseContract.QuestionResponseID.GetName() + " = " + response.ID;
			
			String query = "SELECT * FROM " + tables.MultipleChoiceResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				response.MultipleChoiceResponseID = cursor.getInt(0);
				response.ResponseChoiceID = cursor.getInt(tables.MultipleChoiceResponseContract.ResponseChoiceID.Index);
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
			
			String whereClause = tables.MultipleChoiceResponseContract.QuestionResponseID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceResponseContract.TABLE_NAME, whereClause, new String[] { "" + ID }));
			
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
			
			String whereClause = tables.MultipleChoiceResponseContract.QuestionResponseID.GetName() + " NOT IN (";
			
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

			whereClause += " AND " + tables.MultipleChoiceResponseContract.TABLE_NAME + "." + tables.MultipleChoiceResponseContract.QuestionResponseID.GetName() + " IN ( SELECT " + tables.QuestionResponseContract._ID + " FROM " + tables.QuestionResponseContract.TABLE_NAME + " WHERE " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceResponseContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(MultipleChoiceResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();

			values.put(tables.MultipleChoiceResponseContract.QuestionResponseID.GetName(), response.MultipleChoiceResponseID);
			values.put(tables.MultipleChoiceResponseContract.ResponseChoiceID.GetName(), response.ResponseChoiceID);
	
			String whereClause = tables.MultipleChoiceResponseContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.MultipleChoiceResponseContract.TABLE_NAME, values, whereClause, new String[] { "" + response.ID }));
			
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
			
			String whereClause = tables.MultipleChoiceResponseContract.QuestionResponseID.GetName() + " IN (";
			
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
