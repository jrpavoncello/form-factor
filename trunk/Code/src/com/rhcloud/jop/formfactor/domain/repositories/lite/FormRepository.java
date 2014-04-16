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
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

@SuppressWarnings("static-access")
public class FormRepository implements IFormRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private static FormsContract table = FormFactorTables.getInstance().FormsContract;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.FormRepository";
	
	public FormRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
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
			
			values.put(table.Title.GetName(), form.Title);
			values.put(table.Description.GetName(), form.Description);
			values.put(table.UserID.GetName(), form.UserID);
			values.put(table.Logo.GetName(), form.Logo.ID);
	
			form.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(table.TABLE_NAME, null, values));
			
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
			
			Cursor cursor = this.liteDB.rawQuery("SELECT * FROM " + table.TABLE_NAME, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Form form = new Form();
					
					form.ID = cursor.getInt(0);
					form.UserID = cursor.getInt(table.UserID.Index);
					form.Title = cursor.getString(table.Title.Index);
					form.Description = cursor.getString(table.Description.Index);
					form.Logo.ID = cursor.getInt(table.Logo.Index);
					
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
			
			String whereClause = " WHERE " + table.UserID.GetName() + " = " + userID;
			
			String query = "SELECT * FROM " + table.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Form form = new Form();
					
					form.ID = cursor.getInt(0);
					form.UserID = cursor.getInt(table.UserID.Index);
					form.Title = cursor.getString(table.Title.Index);
					form.Description = cursor.getString(table.Description.Index);
					form.Logo.ID = cursor.getInt(table.Logo.Index);
					
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
		Form form = new Form();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + table._ID + " = " + ID;
			
			String query = "SELECT * FROM " + table.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				form.ID = cursor.getInt(0);
				form.UserID = cursor.getInt(table.UserID.Index);
				form.Title = cursor.getString(table.Title.Index);
				form.Description = cursor.getString(table.Description.Index);
				form.Logo.ID = cursor.getInt(table.Logo.Index);
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
			
			values.put(table._ID, form.ID);
			values.put(table.UserID.GetName(), form.UserID);
			values.put(table.Title.GetName(), form.Title);
			values.put(table.Description.GetName(), form.Description);
			values.put(table.Logo.GetName(), form.Logo.ID);
	
			String whereClause = table._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(table.TABLE_NAME, values, whereClause, new String[] { "" + form.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
