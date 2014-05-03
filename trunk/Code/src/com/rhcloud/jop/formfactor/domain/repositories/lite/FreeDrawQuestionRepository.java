package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.FreeDrawQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeDrawQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeDrawQuestionRepository implements IFreeDrawQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.FreeDrawQuestionRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public FreeDrawQuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(FreeDrawQuestion question)
	{
		ContentValues values = new ContentValues();
		
		values.put(tables.FreeResponseQuestionContract.QuestionID.GetName(), question.ID);

		question.FreeDrawQuestionID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(tables.FreeDrawQuestionContract.TABLE_NAME, null, values));
	}

	@Override
	public void Update(FreeDrawQuestion question)
	{/*
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
	
			String whereClause = tables.FreeResponseQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(tables.FreeResponseQuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}*/
	}

	@Override
	public void DeleteByQuestionID(long questionID) 
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = tables.FreeDrawQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeDrawQuestionContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public FreeDrawQuestion GetByQuestionID(FreeDrawQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + tables.FreeDrawQuestionContract.QuestionID.GetName() + " = " + question.ID;
			
			String query = "SELECT * FROM " + tables.FreeDrawQuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.FreeDrawQuestionID = cursor.getInt(0);
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
			
			String whereClause = tables.FreeDrawQuestionContract.TABLE_NAME + "." + tables.FreeDrawQuestionContract.QuestionID.GetName() + " NOT IN (";
			
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
			
			whereClause += " AND " + tables.FreeDrawQuestionContract.TABLE_NAME + "." + tables.FreeDrawQuestionContract.QuestionID.GetName() + " IN ( SELECT " + tables.QuestionContract._ID + " FROM " + tables.QuestionContract.TABLE_NAME + " WHERE " + tables.QuestionContract.FormID.GetName() + " = ? )";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(tables.FreeDrawQuestionContract.TABLE_NAME, whereClause, args));
			
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
