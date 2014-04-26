package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.User;

public interface IUserRepository
{
	public void Add(User user);
	
	public User GetByUserNamePassword(String username, byte[] password);

	public void Update(User user);
}
