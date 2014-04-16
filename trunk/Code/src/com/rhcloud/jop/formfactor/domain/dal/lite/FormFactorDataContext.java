package com.rhcloud.jop.formfactor.domain.dal.lite;

import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.IUnitOfWork;
import com.rhcloud.jop.formfactor.domain.UnitOfWork;
import com.rhcloud.jop.formfactor.domain.repositories.*;
import com.rhcloud.jop.formfactor.domain.repositories.lite.*;

public class FormFactorDataContext implements IFormFactorDataContext
{
	private IUnitOfWork UnitOfWork = null;
	
	private IFormRepository FormRepo;
	private IQuestionRepository QuestionRepo;
	private IResponseRepository ResponseRepo;
	private IResponseChoiceRepository ResponseChoiceRepo;
	private ILogoRepository LogoRepo;
	private IUserRepository UserRepo;
	
	public FormFactorDataContext(IUnitOfWork unitOfWork)
	{
		this.UnitOfWork = unitOfWork;
		UnitOfWork unit = (UnitOfWork)unitOfWork;
		
		this.FormRepo = new FormRepository(unit);
		this.QuestionRepo = new QuestionRepository(unit);
		this.ResponseRepo = new ResponseRepository(unit);
		this.ResponseChoiceRepo = new ResponseChoiceRepository(unit);
		this.LogoRepo = new LogoRepository(unit);
		this.UserRepo = new UserRepository(unit);
	}

	@Override
	public IUnitOfWork GetUnitOfWork()
	{
		return this.UnitOfWork;
	}

	@Override
	public IFormRepository GetFormRepository()
	{
		return this.FormRepo;
	}

	@Override
	public IQuestionRepository GetQuestionRepository()
	{
		return this.QuestionRepo;
	}

	@Override
	public IResponseRepository GetResponseRepository()
	{
		return this.ResponseRepo;
	}
	
	@Override
	public IResponseChoiceRepository GetResponseChoiceRepository()
	{
		return this.ResponseChoiceRepo;
	}

	@Override
	public ILogoRepository GetLogoRepository()
	{
		return this.LogoRepo;
	}

	@Override
	public IUserRepository GetUserRepository()
	{
		return this.UserRepo;
	}
}
