package com.rhcloud.jop.formfactor.domain.repositories;

import java.util.List;

import com.rhcloud.jop.formfactor.domain.User;

public interface IUserRepository
{
	public void Add(User user);
	
	public List<User> GetAll();
	
	public User GetByID(long ID);

	public void Update(User user);
}
