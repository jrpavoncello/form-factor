package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.common.SQLiteHelper;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IResponseChoiceRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.FormFactorTables;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.ResponseChoicesContract;

@SuppressWarnings("static-access")
public class ResponseChoiceRepository implements IResponseChoiceRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.ResponseChoiceRepository";
	
	public ResponseChoiceRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}
	
	@Override
	public void Add(ResponseChoice choice)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(ResponseChoicesContract.QuestionID.GetName(), choice.QuestionID);
			values.put(ResponseChoicesContract.Choice.GetName(), choice.Choice);
	
			choice.ID = SQLiteHelper.logInsert(TAG_NAME, this.liteDB.insert(ResponseChoicesContract.TABLE_NAME, null, values));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public List<ResponseChoice> GetByQuestionID(long questionID)
	{
		List<ResponseChoice> choices = new ArrayList<ResponseChoice>();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + ResponseChoicesContract.QuestionID.GetName() + " = " + questionID;
			
			String query = "SELECT * FROM " + ResponseChoicesContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				do
				{
					ResponseChoice choice = new ResponseChoice();
					
					choice.ID = cursor.getInt(0);
					choice.QuestionID = cursor.getInt(ResponseChoicesContract.QuestionID.Index);
					choice.Choice = cursor.getString(ResponseChoicesContract.Choice.Index);
					
					choices.add(choice);
				}
				while(cursor.moveToNext());
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return choices;
	}

	@Override
	public ResponseChoice GetByID(long ID)
	{
		ResponseChoice choice = new ResponseChoice();

		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = " WHERE " + ResponseChoicesContract._ID + " = " + ID;
			
			String query = "SELECT * FROM " + ResponseChoicesContract.TABLE_NAME + whereClause;
			
			Cursor cursor = this.liteDB.rawQuery(query, null);
			
			if(cursor.moveToFirst())
			{
				choice.ID = cursor.getInt(0);
				choice.QuestionID = cursor.getInt(ResponseChoicesContract.QuestionID.Index);
				choice.Choice = cursor.getString(ResponseChoicesContract.Choice.Index);
			}
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
		
		return choice;
	}

	@Override
	public void Update(ResponseChoice choice)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(ResponseChoicesContract._ID, choice.ID);
			values.put(ResponseChoicesContract.QuestionID.GetName(), choice.QuestionID);
			values.put(ResponseChoicesContract.Choice.GetName(), choice.Choice);
			
			String whereClause = ResponseChoicesContract._ID + " = ?";
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(ResponseChoicesContract.TABLE_NAME, values, whereClause, new String[] { "" + choice.ID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void UpdateSettings(long choiceID, String choice)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			ContentValues values = new ContentValues();
			
			values.put(ResponseChoicesContract._ID, choiceID);
			values.put(ResponseChoicesContract.Choice.GetName(), choice);
			
			String whereClause = ResponseChoicesContract._ID + " = ?";
			SQLiteHelper.logUpdate(TAG_NAME, this.liteDB.update(ResponseChoicesContract.TABLE_NAME, values, whereClause, new String[] { "" + choiceID }));
			
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
			
			String whereClause = ResponseChoicesContract.QuestionID.GetName() + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(ResponseChoicesContract.TABLE_NAME, whereClause, new String[] { "" + questionID }));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}

	@Override
	public void DeleteByIDsNotIn(long[] IDs, long questionID)
	{
		try
		{
			this.unitOfWork.BeginTransaction();
			
			String whereClause = ResponseChoicesContract._ID + " NOT IN (";
			
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
			
			args[IDs.length] = "" + questionID;
			
			whereClause += " AND " + ResponseChoicesContract.QuestionID + " = ?";
	
			SQLiteHelper.logDelete(TAG_NAME, this.liteDB.delete(ResponseChoicesContract.TABLE_NAME, whereClause, args));
			
			this.unitOfWork.CommitTransaction();
		}
		catch(Exception ex)
		{
			this.unitOfWork.AbortTransaction();
		}
	}
}
