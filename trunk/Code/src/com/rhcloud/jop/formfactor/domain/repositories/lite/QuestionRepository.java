package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class QuestionRepository implements IQuestionRepository
{
	protected SQLiteDatabase liteDB;
	protected UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.QuestionRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public QuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}
	
	@Override
	public void Add(Question question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.QuestionContract.Question.GetName(), question.Question);
			values.put(tables.QuestionContract.Number.GetName(), question.Number);
			values.put(tables.QuestionContract.FormID.GetName(), question.FormID);			
			values.put(tables.QuestionContract.Type.GetName(), question.Type.GetIndex());
			values.put(tables.QuestionContract.Image.GetName(), question.Image);
	
			question.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.QuestionContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public List<Question> GetAll()
	{
		List<Question> questions = new ArrayList<Question>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			Cursor cursor = this.liteDB.rawQuery("SELECT * FROM " + tables.QuestionContract.TABLE_NAME + " ORDER BY " + tables.QuestionContract.Number.GetName(), null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Question question = new Question();
					
					question.ID = cursor.getInt(0);
					question.Question = cursor.getString(tables.QuestionContract.Question.Index);
					question.Number = cursor.getInt(tables.QuestionContract.Number.Index);
					question.FormID = cursor.getInt(tables.QuestionContract.FormID.Index);
					question.Type = QuestionType.GetByIndex(cursor.getInt(tables.QuestionContract.Type.Index));
					question.Image = cursor.getBlob(tables.QuestionContract.Image.Index);
					
					questions.add(question);
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
	public List<Question> GetByFormID(long formID)
	{
		List<Question> questions = new ArrayList<Question>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.QuestionContract.FormID.GetName() + " = " + formID;
			
			String query = "SELECT * FROM " + tables.QuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Question question = new Question();
					
					question.ID = cursor.getInt(0);
					question.Question = cursor.getString(tables.QuestionContract.Question.Index);
					question.Number = cursor.getInt(tables.QuestionContract.Number.Index);
					question.FormID = cursor.getInt(tables.QuestionContract.FormID.Index);
					question.Type = QuestionType.GetByIndex(cursor.getInt(tables.QuestionContract.Type.Index));
					question.Image = cursor.getBlob(tables.QuestionContract.Image.Index);
					
					questions.add(question);
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
	public List<Long> GetIDsByFormID(long formID)
	{
		List<Long> questions = new ArrayList<Long>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.QuestionContract.FormID.GetName() + " = " + formID;
			
			String query = "SELECT _ID FROM " + tables.QuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					long id = cursor.getInt(0);
					
					questions.add(id);
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
	public Question GetByID(long ID)
	{
		Question question = null;
		
		try
		{			
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.QuestionContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + tables.QuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question = new Question();
				question.ID = cursor.getInt(0);
				question.Question = cursor.getString(tables.QuestionContract.Question.Index);
				question.Number = cursor.getInt(tables.QuestionContract.Number.Index);
				question.FormID = cursor.getInt(tables.QuestionContract.FormID.Index);
				question.Type = QuestionType.GetByIndex(cursor.getInt(tables.QuestionContract.Type.Index));
				question.Image = cursor.getBlob(tables.QuestionContract.Image.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return question;
	}
	
	@Override
	public List<Long> GetByIDsNotIn(Long[] IDs, long formID)
	{
		List<Long> questions = new ArrayList<Long>();
		
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = "WHERE " + tables.QuestionContract._ID + " NOT IN (";
			
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
			
			args[IDs.length] = "" + formID;
			
			whereClause += " AND " + tables.QuestionContract.FormID.GetName() + " = ?";
			
			String query = "SELECT _ID FROM " + tables.QuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					questions.add(Long.valueOf(cursor.getInt(0)));
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
	public void Update(Question question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.QuestionContract._ID, question.ID);
			values.put(tables.QuestionContract.Question.GetName(), question.Question);
			values.put(tables.QuestionContract.Number.GetName(), question.Number);
			values.put(tables.QuestionContract.FormID.GetName(), question.FormID);
			values.put(tables.QuestionContract.Type.GetName(), question.Type.GetIndex());
			values.put(tables.QuestionContract.Image.GetName(), question.Image);
	
			String whereClause = tables.QuestionContract._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.QuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
	
	@Override
	public void DeleteByIDsNotIn(Long[] IDs, long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionContract._ID + " NOT IN (";
			
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
			
			args[IDs.length] = "" + formID;
			
			whereClause += " AND " + tables.QuestionContract.FormID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByFormID(long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionContract.FormID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionContract.TABLE_NAME, whereClause, new String[] { "" + formID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByID(long id)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.QuestionContract._ID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.QuestionContract.TABLE_NAME, whereClause, new String[] { "" + id }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
