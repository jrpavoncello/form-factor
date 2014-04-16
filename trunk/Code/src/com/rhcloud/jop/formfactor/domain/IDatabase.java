package com.rhcloud.jop.formfactor.domain;

public interface IDatabase
{
	public void BeginTransaction();
	public void CommitTransaction();
	public void AbortTransaction();
	
	public Connection OpenConnection();
	public void CloseConnection();
	
	public boolean IsInTransaction();
	public boolean IsConnectionOpen();
}
