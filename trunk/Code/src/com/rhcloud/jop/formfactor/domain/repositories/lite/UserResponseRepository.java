package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.UserResponse;
import com.rhcloud.jop.formfactor.domain.repositories.IUserResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class UserResponseRepository implements IUserResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.UserResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public UserResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public UserResponse GetByID(long ID)
	{
		UserResponse response = null;
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.UserResponseContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + tables.UserResponseContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				response = new UserResponse();
				response.ID = cursor.getInt(0);
				response.UserID = cursor.getInt(tables.UserResponseContract.UserID.Index);
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
	public void Add(UserResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.UserResponseContract.UserID.GetName(), response.UserID);
	
			response.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.UserResponseContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByID(long responseID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.UserResponseContract._ID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.UserResponseContract.TABLE_NAME, whereClause, new String[] { "" + responseID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(UserResponse response)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.UserResponseContract.UserID.GetName(), response.UserID);
	
			String whereClause = UserResponseContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.UserResponseContract.TABLE_NAME, values, whereClause, new String[] { "" + response.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
