package com.rhcloud.jop.formfactor.sqlite.datacontracts;

public final class FormFactorTables
{
	private FormFactorTables(){ }
	
	private static FormFactorTables instance;
	
	public static FormFactorTables getInstance()
	{
		if(instance == null)
		{
			instance = new FormFactorTables();
		}
		
		return instance;
	}
	
	public final BaseTable[] GetAllTables()
	{
		return new BaseTable[]{ 
				FormsContract,
				FormsQuestionsXRefContract,
				LogoContract,
				QuestionContract,
				FreeResponseQuestionContract,
				FreeDrawQuestionContract,
				MultipleChoiceQuestionContract,
				QuestionTypeContract,
				QuestionResponseContract,
				ResponseChoicesContract,
				FreeDrawResponseContract,
				FreeResponseResponseContract,
				MultipleChoiceResponseContract,
				UserContract,
				UserActivityTypeContract,
				UserResponseContract };
	}
	
    public final FormsContract FormsContract = new FormsContract();
    public final FormsQuestionsXRefContract FormsQuestionsXRefContract = new FormsQuestionsXRefContract();
    public final LogoContract LogoContract = new LogoContract();
    public final QuestionContract QuestionContract = new QuestionContract();
    public final FreeResponseQuestionContract FreeResponseQuestionContract = new FreeResponseQuestionContract();
    public final FreeDrawQuestionContract FreeDrawQuestionContract = new FreeDrawQuestionContract();
    public final MultipleChoiceQuestionContract MultipleChoiceQuestionContract = new MultipleChoiceQuestionContract();
    public final QuestionTypeContract QuestionTypeContract = new QuestionTypeContract();
    public final QuestionResponseContract QuestionResponseContract = new QuestionResponseContract();
    public final ResponseChoicesContract ResponseChoicesContract = new ResponseChoicesContract();
    public final FreeDrawResponseContract FreeDrawResponseContract = new FreeDrawResponseContract();
    public final FreeResponseResponseContract FreeResponseResponseContract = new FreeResponseResponseContract();
    public final MultipleChoiceResponseContract MultipleChoiceResponseContract = new MultipleChoiceResponseContract();
    public final UserContract UserContract = new UserContract();
    public final UserActivityTypeContract UserActivityTypeContract = new UserActivityTypeContract();
    public final UserResponseContract UserResponseContract = new UserResponseContract();
}
