package com.rhcloud.jop.formfactor.domain;


public class UnitOfWork implements IUnitOfWork
{
	private IDatabase DB = null;
	
	int beginCounter = 0;

	public UnitOfWork(IDatabase db)
	{
		this.DB = db;
	}
	
	public IDatabase GetDB()
	{
		return this.DB;
	}
	
	public void BeginTransaction()
	{
		if(DB != null)
		{
			if(!DB.IsInTransaction())
			{
				if(beginCounter > 0)
				{
					//TODO: REPORT ERROR IN LOG - SOMETHING ODD HAPPENED WITH LAST TRANSACTION
					beginCounter = 0;
				}
			}
			
			DB.BeginTransaction();
			
			beginCounter++;
		}
		else
		{
			//TODO: REPORT ERROR IN LOG
		}
	}
	
	public void CommitTransaction()
	{
		if(DB != null)
		{
			if(DB.IsInTransaction())
			{
				if(--beginCounter == 0)
				{
					DB.CommitTransaction();
				}
			}
			else
			{
				//TODO: REPORT ERROR IN LOG
			}
		}
		else
		{
			//TODO: REPORT ERROR IN LOG
		}
	}
	
	@Override
	public void AbortTransaction()
	{
		if(DB != null)
		{
			if(DB.IsInTransaction())
			{
				DB.AbortTransaction();
			}
		}
	}

	@Override
	public void OpenConnection()
	{
		if(DB != null)
		{
			DB.OpenConnection();
		}
	}

	@Override
	public void CloseConnection() 
	{
		if(DB != null)
		{
			if(DB.IsInTransaction())
			{
				DB.AbortTransaction();
			}
			
			if(DB.IsConnectionOpen())
			{
				DB.CloseConnection();
			}
			else
			{
				//TODO: REPORT ERROR IN LOG
			}
		}
	}
}
