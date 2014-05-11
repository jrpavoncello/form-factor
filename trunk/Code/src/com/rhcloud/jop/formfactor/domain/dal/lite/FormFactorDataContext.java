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
	private IMultipleChoiceQuestionRepository MultipleChoiceQuestionRepository;
	private IFreeResponseQuestionRepository FreeResponseQuestionRepository;
	private IFreeDrawQuestionRepository FreeDrawQuestionRepository;
	private IUserResponseRepository ResponseRepo;
	private IResponseChoiceRepository ResponseChoiceRepo;
	private ILogoRepository LogoRepo;
	private IUserRepository UserRepo;
	private IQuestionResponseRepository QuestionResponseRepository;
	private IMultipleChoiceResponseRepository MultipleChoiceResponseRepository;
	private IFreeResponseResponseRepository FreeResponseResponseRepository;
	private IFreeDrawResponseRepository FreeDrawResponseRepository;
	
	public FormFactorDataContext(IUnitOfWork unitOfWork)
	{
		this.UnitOfWork = unitOfWork;
		UnitOfWork unit = (UnitOfWork)unitOfWork;
		
		this.QuestionRepo = new QuestionRepository(unit);
		this.MultipleChoiceQuestionRepository = new MultipleChoiceQuestionRepository(unit);
		this.FreeResponseQuestionRepository = new FreeResponseQuestionRepository(unit);
		this.FreeDrawQuestionRepository = new FreeDrawQuestionRepository(unit);
		this.FormRepo = new FormRepository(unit);
		this.ResponseRepo = new UserResponseRepository(unit);
		this.ResponseChoiceRepo = new ResponseChoiceRepository(unit);
		this.LogoRepo = new LogoRepository(unit);
		this.UserRepo = new UserRepository(unit);
		this.QuestionResponseRepository = new QuestionResponseRepository(unit);
		this.MultipleChoiceResponseRepository = new MultipleChoiceResponseRepository(unit);
		this.FreeResponseResponseRepository = new FreeResponseResponseRepository(unit);
		this.FreeDrawResponseRepository = new FreeDrawResponseRepository(unit);
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
	public IUserResponseRepository GetUserResponseRepository()
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

	@Override
	public IQuestionRepository GetQuestionRepository()
	{
		return this.QuestionRepo;
	}

	@Override
	public IMultipleChoiceQuestionRepository GetMultipleChoiceQuestionRepository()
	{
		return this.MultipleChoiceQuestionRepository;
	}

	@Override
	public IFreeResponseQuestionRepository GetFreeResponseQuestionRepository()
	{
		return this.FreeResponseQuestionRepository;
	}

	@Override
	public IFreeDrawQuestionRepository GetFreeDrawQuestionRepository()
	{
		return this.FreeDrawQuestionRepository;
	}

	@Override
	public IQuestionResponseRepository GetQuestionResponseRepository()
	{
		return this.QuestionResponseRepository;
	}

	@Override
	public IMultipleChoiceResponseRepository GetMultipleChoiceResponseRepository()
	{
		return this.MultipleChoiceResponseRepository;
	}

	@Override
	public IFreeResponseResponseRepository GetFreeResponseResponseRepository()
	{
		return this.FreeResponseResponseRepository;
	}

	@Override
	public IFreeDrawResponseRepository GetFreeDrawResponseRepository()
	{
		return this.FreeDrawResponseRepository;
	}
}
