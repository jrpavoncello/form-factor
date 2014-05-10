package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFormRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FormRepository implements IFormRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.FormRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public FormRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}
	
	@Override
	public void Add(Form form)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.FormsContract.Title.GetName(), form.Title);
			values.put(tables.FormsContract.Description.GetName(), form.Description);
			values.put(tables.FormsContract.UserID.GetName(), form.UserID);
			values.put(tables.FormsContract.Logo.GetName(), form.Logo.ID);
			values.put(tables.FormsContract.ExternalID.GetName(), form.ExternalID);
	
			form.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.FormsContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public List<Form> GetAll()
	{
		List<Form> questions = new ArrayList<Form>();
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			Cursor cursor = this.liteDB.rawQuery("SELECT * FROM " + tables.FormsContract.TABLE_NAME, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Form form = new Form();
					
					form.ID = cursor.getInt(0);
					form.UserID = cursor.getInt(tables.FormsContract.UserID.Index);
					form.Title = cursor.getString(tables.FormsContract.Title.Index);
					form.Description = cursor.getString(tables.FormsContract.Description.Index);
					form.Logo.ID = cursor.getInt(tables.FormsContract.Logo.Index);
					form.ExternalID = cursor.getInt(tables.FormsContract.ExternalID.Index);
					
					questions.add(form);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return questions;
	}

	@Override
	public List<Form> GetByUserID(long userID)
	{
		List<Form> forms = new ArrayList<Form>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FormsContract.UserID.GetName() + " = " + userID;
			
			String query = "SELECT * FROM " + tables.FormsContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Form form = new Form();
					
					form.ID = cursor.getInt(0);
					form.UserID = cursor.getInt(tables.FormsContract.UserID.Index);
					form.Title = cursor.getString(tables.FormsContract.Title.Index);
					form.Description = cursor.getString(tables.FormsContract.Description.Index);
					form.Logo.ID = cursor.getInt(tables.FormsContract.Logo.Index);
					form.ExternalID = cursor.getInt(tables.FormsContract.ExternalID.Index);
					
					forms.add(form);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return forms;
	}

	@Override
	public Form GetByID(long ID)
	{
		Form form = null;

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FormsContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + tables.FormsContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				form = new Form();
				form.ID = cursor.getInt(0);
				form.UserID = cursor.getInt(tables.FormsContract.UserID.Index);
				form.Title = cursor.getString(tables.FormsContract.Title.Index);
				form.Description = cursor.getString(tables.FormsContract.Description.Index);
				form.Logo.ID = cursor.getInt(tables.FormsContract.Logo.Index);
				form.ExternalID = cursor.getInt(tables.FormsContract.ExternalID.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return form;
	}

	@Override
	public void Update(Form form)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.FormsContract._ID, form.ID);
			values.put(tables.FormsContract.UserID.GetName(), form.UserID);
			values.put(tables.FormsContract.Title.GetName(), form.Title);
			values.put(tables.FormsContract.Description.GetName(), form.Description);
			values.put(tables.FormsContract.Logo.GetName(), form.Logo.ID);
			values.put(tables.FormsContract.ExternalID.GetName(), form.ExternalID);
	
			String whereClause = FormsContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.FormsContract.TABLE_NAME, values, whereClause, new String[] { "" + form.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public long GetCountByUser(long userID)
	{
		long count = 0;

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FormsContract.UserID.GetName() + " = " + userID;
			
			String query = "SELECT COUNT(*) FROM " + tables.FormsContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				count = cursor.getInt(0);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return count;
	}
}
