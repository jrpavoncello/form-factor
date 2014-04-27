package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class MultipleChoiceQuestionRepository implements IMultipleChoiceQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.MultipleChoiceQuestionRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public MultipleChoiceQuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(MultipleChoiceQuestion question)
	{
		ContentValues values = new ContentValues();
		
		values.put(tables.MultipleChoiceQuestionContract.QuestionID.GetName(), question.ID);
		values.put(tables.MultipleChoiceQuestionContract.MinResponses.GetName(), question.MinResponses);
		values.put(tables.MultipleChoiceQuestionContract.MaxResponses.GetName(), question.MaxResponses);

		question.MultipleChoiceQuestionID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.MultipleChoiceQuestionContract.TABLE_NAME, null, values));
	}

	@Override
	public MultipleChoiceQuestion GetByQuestionID(MultipleChoiceQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " = " + question.ID;
			
			String query = "SELECT * FROM " + tables.MultipleChoiceQuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.MultipleChoiceQuestionID = cursor.getInt(0);
				question.MinResponses = cursor.getInt(tables.MultipleChoiceQuestionContract.MinResponses.Index);
				question.MaxResponses = cursor.getInt(tables.MultipleChoiceQuestionContract.MaxResponses.Index);
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
	public void DeleteByQuestionID(long questionID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceQuestionContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void Update(MultipleChoiceQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(tables.MultipleChoiceQuestionContract.MinResponses.GetName(), question.MinResponses);
			values.put(tables.MultipleChoiceQuestionContract.MaxResponses.GetName(), question.MaxResponses);
	
			String whereClause = tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.MultipleChoiceQuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			//Model this after
			//DELETE FROM tMultipleChoiceQuestion WHERE tMultipleChoiceQuestion.iQuestionID NOT IN (?,?,?,...) AND tMultipleChoiceQuestion.iQuestionID IN (SELECT iQuestionID FROM tQuestion WHERE iFormID = ?)
			
			String whereClause = tables.MultipleChoiceQuestionContract.TABLE_NAME + "." + tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " NOT IN (";
			
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
			
			whereClause += " AND " + tables.MultipleChoiceQuestionContract.TABLE_NAME + "." + tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " IN ( SELECT " + tables.QuestionContract._ID + " FROM " + tables.QuestionContract.TABLE_NAME + " WHERE " + tables.QuestionContract.FormID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceQuestionContract.TABLE_NAME, whereClause, args));
			
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
			
			String whereClause = tables.MultipleChoiceQuestionContract.QuestionID.GetName() + " IN (";
			
			whereClause += " SELECT " + tables.QuestionContract._ID + " FROM " + tables.QuestionContract.TABLE_NAME + " WHERE " + tables.QuestionContract.FormID.GetName() + " = ?)";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.MultipleChoiceQuestionContract.TABLE_NAME, whereClause, new String[] { "" + formID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
