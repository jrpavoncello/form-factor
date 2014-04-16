package com.rhcloud.jop.formfactor.domain;

public interface IUnitOfWork
{
	public void BeginTransaction();
	public void CommitTransaction();
	public void AbortTransaction();
	
	void OpenConnection();
	void CloseConnection();
}
