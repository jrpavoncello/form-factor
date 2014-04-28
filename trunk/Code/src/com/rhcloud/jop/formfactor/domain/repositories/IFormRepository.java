package com.rhcloud.jop.formfactor.domain.repositories;

import java.util.List;

import com.rhcloud.jop.formfactor.domain.*;

public interface IFormRepository
{
	public void Add(Form form);
	
	public List<Form> GetAll();
	
	public List<Form> GetByUserID(long ID);
	
	public Form GetByID(long ID);

	public void Update(Form form);
	
	public long GetCountByUser(long userID);
}
