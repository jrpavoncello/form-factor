package com.rhcloud.jop.formfactor.domain.services;
import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.User;
import com.rhcloud.jop.formfactor.domain.repositories.IUserRepository;

public class UserService
{
	IFormFactorDataContext DataContext;
	
	public UserService(IFormFactorDataContext dataContext)
	{
		this.DataContext = dataContext;
	}
	
	public Result CreateUpdateUser(User user)
	{
		Result result = new Result();
		
		IUserRepository userRepo = this.DataContext.GetUserRepository();

		if(user != null)
		{
			try
			{
				if(user.ID == 0)
				{
					userRepo.Add(user);
				}
				else
				{
					userRepo.Update(user);
				}
			}
			catch(Exception ex)
			{
				result.Success = false;
				result.Messages.add("Exception occured when trying to add/update user:\r\n" + ex.getMessage());
			}
		}
		
		return result;
	}
	
	public boolean IsValidAuthentication(String username, byte[] passwordHash)
	{
		User user = null;
		
		IUserRepository userRepo = this.DataContext.GetUserRepository();
		
		user = userRepo.GetByUserNamePassword(username, passwordHash);
		
		return user != null;
	}
	
	public User GetUser(String username, byte[] passwordHash)
	{
		User user = null;
		
		IUserRepository userRepo = this.DataContext.GetUserRepository();
		
		user = userRepo.GetByUserNamePassword(username, passwordHash);
		
		return user;
	}
}
