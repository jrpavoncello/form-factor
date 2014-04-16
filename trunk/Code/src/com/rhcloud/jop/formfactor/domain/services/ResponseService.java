package com.rhcloud.jop.formfactor.domain.services;

import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;

public class ResponseService
{
	IFormFactorDataContext DataContext;
	
	public ResponseService(IFormFactorDataContext dataContext)
	{
		this.DataContext = dataContext;
	}

}
