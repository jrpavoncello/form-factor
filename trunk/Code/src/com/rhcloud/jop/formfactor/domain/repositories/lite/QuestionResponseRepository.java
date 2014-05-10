package com.rhcloud.jop.formfactor.domain.repositories.lite;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.domain.QuestionResponse;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class QuestionResponseRepository implements IQuestionResponseRepository
{
	protected SQLiteDatabase liteDB;
	protected UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.repositories.lite.QuestionResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public QuestionResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public void Add(QuestionResponse response)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteByID(long questionResponseID)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteByIDsNotIn(Long[] IDs, long userResponseID)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteByUserResponseID(long userResponseID)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<QuestionResponse> GetAll()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuestionResponse> GetByUserResponseID(long userResponseID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> GetByIDsNotIn(Long[] IDs, long userResponseID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Long> GetIDsByUserResponseID(long userResponseID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QuestionResponse GetByID(long ID)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Update(QuestionResponse response)
	{
		// TODO Auto-generated method stub
		
	}
}
