package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

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
	
			user.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.UserContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public List<User> GetAll()
	{
		List<User> users = new ArrayList<User>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			Cursor cursor = this.liteDB.rawQuery("SELECT * FROM " + tables.UserContract.TABLE_NAME, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					User user = new User();
					
					user.ID = cursor.getInt(0);
					user.Email = cursor.getString(tables.UserContract.Email.Index);
					user.Username = cursor.getString(tables.UserContract.Username.Index);
					
					users.add(user);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return users;
	}

	@Override
	public User GetByID(long ID)
	{
		User user = new User();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.UserContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + tables.UserContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				user.ID = cursor.getInt(0);
				user.Email = cursor.getString(tables.UserContract.Email.Index);
				user.Username = cursor.getString(tables.UserContract.Username.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return user;
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
	
			String whereClause = UserContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.UserContract.TABLE_NAME, values, whereClause, new String[] { "" + user.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
