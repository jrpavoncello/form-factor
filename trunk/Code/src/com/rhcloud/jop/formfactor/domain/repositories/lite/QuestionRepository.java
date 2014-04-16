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
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

@SuppressWarnings("static-access")
public class QuestionRepository implements IQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private static QuestionContract table = FormFactorTables.getInstance().QuestionContract;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.QuestionRepository";
	
	public QuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
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
			
			values.put(table.Question.GetName(), question.Question);
			values.put(table.Number.GetName(), question.Number);
			values.put(table.FormID.GetName(), question.FormID);
			values.put(table.Type.GetName(), question.Type.GetIndex());
			values.put(table.Image.GetName(), question.Image);
			values.put(table.MaxResponses.GetName(), question.MaxResponses);
			values.put(table.MinResponses.GetName(), question.MinResponses);
	
			question.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(table.TABLE_NAME, null, values));
			
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
			
			Cursor cursor = this.liteDB.rawQuery("SELECT * FROM " + table.TABLE_NAME + " ORDER BY " + table.Number.GetName(), null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Question question = new Question();
					
					question.ID = cursor.getInt(0);
					question.Question = cursor.getString(table.Question.Index);
					question.Number = cursor.getInt(table.Number.Index);
					question.FormID = cursor.getInt(table.FormID.Index);
					question.Type = QuestionType.values()[(cursor.getInt(table.Type.Index))];
					question.Image = cursor.getBlob(table.Image.Index);
					question.MaxResponses = cursor.getInt(table.MaxResponses.Index);
					question.MinResponses = cursor.getInt(table.MinResponses.Index);
					
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
			
			String whereClause = " WHERE " + table.FormID.GetName() + " = " + formID;
			
			String query = "SELECT * FROM " + table.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					Question question = new Question();
					
					question.ID = cursor.getInt(0);
					question.Question = cursor.getString(table.Question.Index);
					question.Number = cursor.getInt(table.Number.Index);
					question.FormID = cursor.getInt(table.FormID.Index);
					question.Type = QuestionType.values()[cursor.getInt(table.Type.Index)];
					question.Image = cursor.getBlob(table.Image.Index);
					question.MaxResponses = cursor.getInt(table.MaxResponses.Index);
					question.MinResponses = cursor.getInt(table.MinResponses.Index);
					
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
	public Question GetByID(long ID)
	{
		Question question = new Question();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + table._ID + " = " + ID;
			
			String query = "SELECT * FROM " + table.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.ID = cursor.getInt(0);
				question.Question = cursor.getString(table.Question.Index);
				question.Number = cursor.getInt(table.Number.Index);
				question.FormID = cursor.getInt(table.FormID.Index);
				question.Type = QuestionType.values()[cursor.getInt(table.Type.Index)];
				question.Image = cursor.getBlob(table.Image.Index);
				question.MaxResponses = cursor.getInt(table.MaxResponses.Index);
				question.MinResponses = cursor.getInt(table.MinResponses.Index);
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
	public void Update(Question question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(table._ID, question.ID);
			values.put(table.Question.GetName(), question.Question);
			values.put(table.Number.GetName(), question.Number);
			values.put(table.FormID.GetName(), question.FormID);
			values.put(table.Type.GetName(), question.Type.GetIndex());
			values.put(table.Image.GetName(), question.Image);
			values.put(table.MaxResponses.GetName(), question.MaxResponses);
			values.put(table.MinResponses.GetName(), question.MinResponses);
	
			String whereClause = table._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(table.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void UpdateSettings(long questionID, int minResponses, int maxResponses)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(table._ID, questionID);
			values.put(table.MaxResponses.GetName(), maxResponses);
			values.put(table.MinResponses.GetName(), minResponses);
	
			String whereClause = table._ID + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(table.TABLE_NAME, values, whereClause, new String[] { "" + questionID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
	
	@Override
	public void DeleteByIDsNotIn(long[] IDs, long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = table._ID + " NOT IN (";
			
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
			
			whereClause += " AND " + table.FormID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(table.TABLE_NAME, whereClause, args));
			
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
			
			String whereClause = table.FormID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(table.TABLE_NAME, whereClause, new String[] { "" + formID }));
			
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
			
			String whereClause = table._ID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(table.TABLE_NAME, whereClause, new String[] { "" + id }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
