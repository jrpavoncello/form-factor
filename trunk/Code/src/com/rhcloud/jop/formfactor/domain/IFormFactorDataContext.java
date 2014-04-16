package com.rhcloud.jop.formfactor.domain;

import com.rhcloud.jop.formfactor.domain.repositories.*;

public interface IFormFactorDataContext
{
	public IFormRepository GetFormRepository();
	public IQuestionRepository GetQuestionRepository();
	public IResponseRepository GetResponseRepository();
	public IUnitOfWork GetUnitOfWork();
	public ILogoRepository GetLogoRepository();
	public IUserRepository GetUserRepository();
	public IResponseChoiceRepository GetResponseChoiceRepository();
}
