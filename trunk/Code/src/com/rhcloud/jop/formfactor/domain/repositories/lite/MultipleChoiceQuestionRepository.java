package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class MultipleChoiceQuestionRepository implements IMultipleChoiceQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.MultipleChoiceQuestionRepository";
	
	public MultipleChoiceQuestionRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(MultipleChoiceQuestion question)
	{
		ContentValues values = new ContentValues();
		
		values.put(MultipleChoiceQuestionContract.QuestionID.GetName(), question.ID);
		values.put(MultipleChoiceQuestionContract.MinResponses.GetName(), question.MinResponses);
		values.put(MultipleChoiceQuestionContract.MaxResponses.GetName(), question.MaxResponses);

		question.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(MultipleChoiceQuestionContract.TABLE_NAME, null, values));
	}

	@Override
	public MultipleChoiceQuestion GetByQuestionID(MultipleChoiceQuestion question)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + MultipleChoiceQuestionContract.QuestionID.GetName() + " = " + question.ID;
			
			String query = "SELECT * FROM " + MultipleChoiceQuestionContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				question.MinResponses = cursor.getInt(MultipleChoiceQuestionContract.MinResponses.Index);
				question.MaxResponses = cursor.getInt(MultipleChoiceQuestionContract.MaxResponses.Index);
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
			
			String whereClause = MultipleChoiceQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(MultipleChoiceQuestionContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
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
			
			values.put(MultipleChoiceQuestionContract.QuestionID.GetName(), question.ID);
			values.put(MultipleChoiceQuestionContract.MinResponses.GetName(), question.MinResponses);
			values.put(MultipleChoiceQuestionContract.MaxResponses.GetName(), question.MaxResponses);
	
			String whereClause = MultipleChoiceQuestionContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(MultipleChoiceQuestionContract.TABLE_NAME, values, whereClause, new String[] { "" + question.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
