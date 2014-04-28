package com.rhcloud.jop.formfactor.domain.repositories.lite;

import android.database.sqlite.SQLiteDatabase;

import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.IResponseRepository;
import com.rhcloud.jop.formfactor.sqlite.FormFactorDb;
import com.rhcloud.jop.formfactor.sqlite.datacontracts.*;

public class ResponseRepository implements IResponseRepository
{
	private SQLiteDatabase liteDB;
	private UnitOfWork unitOfWork;
	private final String TAG_NAME = "com.rhcloud.jop.formfactor.domain.dal.repositories.ResponseRepository";
	private FormFactorTables tables = FormFactorTables.getInstance();
	
	public ResponseRepository(UnitOfWork unitOfWork)
	{
		FormFactorDb formFactorDB = (FormFactorDb)unitOfWork.GetDB();
		this.unitOfWork = unitOfWork;
		this.liteDB = formFactorDB.getDB();
	}
	
}
