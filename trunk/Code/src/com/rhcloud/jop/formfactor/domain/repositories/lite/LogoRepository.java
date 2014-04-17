package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.IUnitOfWork;
import com.rhcloud.jop.formfactor.domain.Logo;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.ILogoRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class LogoRepository implements ILogoRepository
{
	private SQLiteDatabase liteDB;
	private IUnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.LogoRepository";
	
	public LogoRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.liteDB = formFactorDB.getDB();
		this.unitOfWork = unitOfWork;
	}

	@Override
	public void Add(Logo logo)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(LogoContract.Description.GetName(), logo.Description);
			values.put(LogoContract.Image.GetName(), logo.Image);
	
			logo.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(LogoContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
	
	@Override
	public Logo GetByID(long ID) 
	{
		Logo logo = new Logo();
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + LogoContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + LogoContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				logo.ID = cursor.getInt(0);
				logo.Description = cursor.getString(LogoContract.Description.Index);
				logo.Image = cursor.getBlob(LogoContract.Image.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return logo;
	}

	@Override
	public void Update(Logo logo)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(LogoContract._ID, logo.ID);
			values.put(LogoContract.Description.GetName(), logo.Description);
			values.put(LogoContract.Image.GetName(), logo.Image);
			
			String whereClause = LogoContract._ID + " = ?";
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(LogoContract.TABLE_NAME, values, whereClause, new String[] { "" + logo.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
