package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.UserResponse;

public interface IUserResponseRepository
{
	public UserResponse GetByID(long ID);
	
	public void Add(UserResponse response);
	
	public void DeleteByID(long ID);

	public void Update(UserResponse response);
}
