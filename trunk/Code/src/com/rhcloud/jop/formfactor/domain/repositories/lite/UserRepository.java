package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.repositories.IUserRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class UserRepository implements IUserRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.UserRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public UserRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(User user)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.UserContract.Email.GetName(), user.Email);
			values.put(tables.UserContract.Username.GetName(), user.Username);
			values.put(tables.UserContract.Password.GetName(), user.Password);
	
			user.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.UserContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(User user)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.UserContract._ID, user.ID);
			values.put(tables.UserContract.Email.GetName(), user.Email);
			values.put(tables.UserContract.Username.GetName(), user.Username);
			values.put(tables.UserContract.Password.GetName(), user.Password);
	
			String whereClause = UserContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.UserContract.TABLE_NAME, values, whereClause, new String[] { "" + user.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public User GetByUserNamePassword(String username, byte[] password)
	{
		User user = null;

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.UserContract.Username.GetName() + " = ? AND " + tables.UserContract.Password.GetName() + " = ?";
			
			String query = "SELECT * FROM " + tables.UserContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				user = new User();
				user.ID = cursor.getInt(0);
				user.Email = cursor.getString(tables.UserContract.Email.Index);
				user.Username = cursor.getString(tables.UserContract.Username.Index);
				user.Password = cursor.getBlob(tables.UserContract.Email.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return user;
	}
}
