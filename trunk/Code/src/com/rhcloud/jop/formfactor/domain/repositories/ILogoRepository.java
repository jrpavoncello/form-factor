package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.Logo;

public interface ILogoRepository
{
	public Logo GetByID(long ID);
	
	public void Add(Logo logo);

	public void Update(Logo logo);
}
