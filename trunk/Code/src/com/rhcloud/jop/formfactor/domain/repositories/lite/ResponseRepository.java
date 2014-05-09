package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.UserResponse;
import com.rhcloud.jop.formfactor.domain.repositories.IResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDB;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class ResponseRepository implements IResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.ResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public ResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDB formFactorDB = (FormFactorDB)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}

	@Override
	public UserResponse GetByID(long ID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void Add(UserResponse question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DeleteByID(long formID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Update(UserResponse question) {
		// TODO Auto-generated method stub
		
	}
	
}
