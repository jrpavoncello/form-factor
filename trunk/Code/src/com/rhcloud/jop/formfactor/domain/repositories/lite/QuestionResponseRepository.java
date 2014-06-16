package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.QuestionResponse;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class QuestionResponseRepository implements IQuestionResponseRepository
{
	protected SQLiteDatabase liteDB;
	protected UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.QuestionResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public QuestionResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(QuestionResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.QuestionResponseContract.QuestionID.GetName(), response.QuestionID);
			values.put(tables.QuestionResponseContract.UserResponseID.GetName(), response.UserResponseID);
	
			response.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.QuestionResponseContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByID(long questionResponseID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionResponseContract._ID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionResponseContract.TABLE_NAME, whereClause, new String[] { "" + questionResponseID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByIDsNotIn(Long[] IDs, long userResponseID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionResponseContract._ID + " NOT IN (";
			
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
			
			args[IDs.length] = "" + userResponseID;
			
			whereClause += " AND " + tables.QuestionResponseContract.UserResponseID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionResponseContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByUserResponseID(long userResponseID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionResponseContract.UserResponseID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionResponseContract.TABLE_NAME, whereClause, new String[] { "" + userResponseID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public List<QuestionResponse> GetAll()
	{
		List<QuestionResponse> responses = new ArrayList<QuestionResponse>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String query = "SELECT * FROM " + tables.QuestionResponseContract.TABLE_NAME;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);

			if(cursor.moveToFirst())
			{
				do
				{
					QuestionResponse response = new QuestionResponse();

					response.ID = cursor.getInt(0);
					response.QuestionID = cursor.getInt(tables.QuestionResponseContract.QuestionID.Index);
					response.UserResponseID = cursor.getInt(tables.QuestionResponseContract.UserResponseID.Index);
					
					responses.add(response);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return responses;
	}

	@Override
	public List<QuestionResponse> GetByUserResponseID(long userResponseID)
	{
		List<QuestionResponse> responses = new ArrayList<QuestionResponse>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.QuestionResponseContract.UserResponseID + " = " + userResponseID;
			
			String query = "SELECT * FROM " + tables.QuestionResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);

			if(cursor.moveToFirst())
			{
				do
				{
					QuestionResponse response = new QuestionResponse();

					response.ID = cursor.getInt(0);
					response.QuestionID = cursor.getInt(tables.QuestionResponseContract.QuestionID.Index);
					response.UserResponseID = cursor.getInt(tables.QuestionResponseContract.UserResponseID.Index);
					
					responses.add(response);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return responses;
	}

	@Override
	public List<Long> GetByIDsNotIn(Long[] IDs, long userResponseID)
	{
		List<Long> questionResponses = new ArrayList<Long>();
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = "WHERE " + tables.QuestionResponseContract._ID + " NOT IN (";
			
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
			
			args[IDs.length] = "" + userResponseID;
			
			whereClause += " AND " + tables.QuestionResponseContract.UserResponseID.GetName() + " = ?";
			
			String query = "SELECT _ID FROM " + tables.QuestionResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					questionResponses.add(Long.valueOf(cursor.getInt(0)));
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return questionResponses;
	}

	@Override
	public List<Long> GetIDsByUserResponseID(long userResponseID)
	{
		List<Long> questionResponses = new ArrayList<Long>();
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = "WHERE " + tables.QuestionResponseContract.UserResponseID + " = " + userResponseID;
			
			String query = "SELECT _ID FROM " + tables.QuestionResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					questionResponses.add(Long.valueOf(cursor.getInt(0)));
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return questionResponses;
	}

	@Override
	public QuestionResponse GetByID(long ID)
	{
		QuestionResponse response = null;

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.QuestionResponseContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + tables.QuestionResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);

			if(cursor.moveToFirst())
			{
				response = new QuestionResponse();

				response.ID = cursor.getInt(0);
				response.QuestionID = cursor.getInt(tables.QuestionResponseContract.QuestionID.Index);
				response.UserResponseID = cursor.getInt(tables.QuestionResponseContract.UserResponseID.Index);
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
	public void Update(QuestionResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.QuestionResponseContract.QuestionID.GetName(), response.QuestionID);
			values.put(tables.QuestionResponseContract.UserResponseID.GetName(), response.UserResponseID);
			
			String whereClause = tables.QuestionResponseContract._ID + " = ?";
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.QuestionResponseContract.TABLE_NAME, values, whereClause, new String[] { "" + response.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
