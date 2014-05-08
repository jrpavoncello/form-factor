package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.UserResponse;

public interface IResponseRepository
{
	public UserResponse GetByID(long ID);
	
	public void Add(UserResponse question);
	
	public void DeleteByID(long formID);

	public void Update(UserResponse question);
}
