package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeResponseQuestionRepository implements IFreeResponseQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.FreeResponseQuestionRepository";
	
	public FreeResponseQuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Update(FreeResponseQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(FreeResponseQuestionContract.QuestionID.GetName(), question.ID);
			values.put(FreeResponseQuestionContract.MinLength.GetName(), question.MinLength);
			values.put(FreeResponseQuestionContract.MaxLength.GetName(), question.MaxLength);
			values.put(FreeResponseQuestionContract.Lines.GetName(), question.Lines);
	
			String whereClause = FreeResponseQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(FreeResponseQuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
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
			
			String whereClause = FreeResponseQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(FreeResponseQuestionContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
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
			
			String whereClause = " WHERE " + FreeResponseQuestionContract.QuestionID.GetName() + " = " + question.ID;
			
			String query = "SELECT * FROM " + FreeResponseQuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.MinLength = cursor.getInt(FreeResponseQuestionContract.MinLength.Index);
				question.MaxLength = cursor.getInt(FreeResponseQuestionContract.MaxLength.Index);
				question.Lines = cursor.getInt(FreeResponseQuestionContract.Lines.Index);
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
	public void Add(FreeResponseQuestion question)
	{
		ContentValues values = new ContentValues();
		
		values.put(FreeResponseQuestionContract.QuestionID.GetName(), question.ID);
		values.put(FreeResponseQuestionContract.MinLength.GetName(), question.MinLength);
		values.put(FreeResponseQuestionContract.MaxLength.GetName(), question.MaxLength);
		values.put(FreeResponseQuestionContract.Lines.GetName(), question.Lines);

		question.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(FreeResponseQuestionContract.TABLE_NAME, null, values));
	}

	@Override
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			//Model this after
			//DELETE FROM tMultipleChoiceQuestion WHERE tMultipleChoiceQuestion.iQuestionID NOT IN (?,?,?,...) AND tMultipleChoiceQuestion.iQuestionID IN (SELECT iQuestionID FROM tQuestion WHERE iFormID = ?)
			
			String whereClause = FreeResponseQuestionContract.TABLE_NAME + "." + FreeResponseQuestionContract.QuestionID.GetName() + " NOT IN (";
			
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
			
			whereClause += " AND " + FreeResponseQuestionContract.QuestionID.GetName() + " IN ( SELECT " + QuestionContract._ID + " FROM " + QuestionContract.TABLE_NAME + " WHERE " + QuestionContract.FormID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(FreeResponseQuestionContract.TABLE_NAME, whereClause, args));
			
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
		
	}
}
