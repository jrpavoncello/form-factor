package com.rhcloud.jop.formfactor.domain;

import com.rhcloud.jop.formfactor.domain.repositories.*;

public interface IFormFactorDataContext
{
	public IFormRepository GetFormRepository();
	public IResponseRepository GetResponseRepository();
	public IUnitOfWork GetUnitOfWork();
	public ILogoRepository GetLogoRepository();
	public IUserRepository GetUserRepository();
	public IQuestionRepository GetQuestionRepository();
	public IMultipleChoiceQuestionRepository GetMultipleChoiceQuestionRepository();
	public IFreeResponseQuestionRepository GetFreeResponseQuestionRepository();
	public IResponseChoiceRepository GetResponseChoiceRepository();
	public IFreeDrawQuestionRepository GetFreeDrawQuestionRepository();
}
