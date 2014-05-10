package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeResponseQuestionRepository implements IFreeResponseQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.FreeResponseQuestionRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public FreeResponseQuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(FreeResponseQuestion question)
	{
		ContentValues values = new ContentValues();
		
		values.put(tables.FreeResponseQuestionContract.QuestionID.GetName(), question.ID);
		values.put(tables.FreeResponseQuestionContract.MinLength.GetName(), question.MinLength);
		values.put(tables.FreeResponseQuestionContract.MaxLength.GetName(), question.MaxLength);
		values.put(tables.FreeResponseQuestionContract.Lines.GetName(), question.Lines);

		question.FreeResponseQuestionID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.FreeResponseQuestionContract.TABLE_NAME, null, values));
	}

	@Override
	public void Update(FreeResponseQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.FreeResponseQuestionContract.MinLength.GetName(), question.MinLength);
			values.put(tables.FreeResponseQuestionContract.MaxLength.GetName(), question.MaxLength);
			values.put(tables.FreeResponseQuestionContract.Lines.GetName(), question.Lines);
	
			String whereClause = tables.FreeResponseQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.FreeResponseQuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByQuestionID(long questionID) 
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.FreeResponseQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeResponseQuestionContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public FreeResponseQuestion GetByQuestionID(FreeResponseQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FreeResponseQuestionContract.QuestionID.GetName() + " = " + question.ID;
			
			String query = "SELECT * FROM " + tables.FreeResponseQuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.FreeResponseQuestionID = cursor.getInt(0);
				question.MinLength = cursor.getInt(tables.FreeResponseQuestionContract.MinLength.Index);
				question.MaxLength = cursor.getInt(tables.FreeResponseQuestionContract.MaxLength.Index);
				question.Lines = cursor.getInt(tables.FreeResponseQuestionContract.Lines.Index);
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
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			//Model this after
			//DELETE FROM tMultipleChoiceQuestion WHERE tMultipleChoiceQuestion.iQuestionID NOT IN (?,?,?,...) AND tMultipleChoiceQuestion.iQuestionID IN (SELECT iQuestionID FROM tQuestion WHERE iFormID = ?)
			
			String whereClause = tables.FreeResponseQuestionContract.TABLE_NAME + "." + tables.FreeResponseQuestionContract.QuestionID.GetName() + " NOT IN (";
			
			String[] args = new String[IDs.length + 1];
			
			for(int i = 0; i < IDs.length; i++)
			{
				if(i != IDs.length - 1)
				{
					whereClause += "?, ";
				}
				else
				{
					whereClause += "? )";
				}
				
				args[i] = "" + IDs[i];
			}
			
			args[IDs.length] = "" + formID;
			
			whereClause += " AND " + tables.FreeResponseQuestionContract.TABLE_NAME + "." + tables.FreeResponseQuestionContract.QuestionID.GetName() + " IN ( SELECT " + tables.QuestionContract._ID + " FROM " + tables.QuestionContract.TABLE_NAME + " WHERE " + tables.QuestionContract.FormID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeResponseQuestionContract.TABLE_NAME, whereClause, args));
			
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
			
			String whereClause = tables.FreeResponseQuestionContract.QuestionID.GetName() + " IN (";
			
			whereClause += " SELECT " + tables.QuestionContract._ID + " FROM " + tables.QuestionContract.TABLE_NAME + " WHERE " + tables.QuestionContract.FormID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeResponseQuestionContract.TABLE_NAME, whereClause, new String[] { "" + formID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
