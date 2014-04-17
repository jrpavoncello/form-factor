package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class FreeResponseQuestionRepository implements IFreeResponseQuestionRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private static FreeResponseQuestionContract table = FormFactorTables.getInstance().FreeResponseQuestionExtendContract;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteByID(long ID) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public FreeResponseQuestion GetByID()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Add(FreeResponseQuestion question)
	{
		// TODO Auto-generated method stub
		
	}
}
