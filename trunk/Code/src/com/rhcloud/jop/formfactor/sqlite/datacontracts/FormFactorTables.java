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
				QuestionTypeContract, 
				ResponseChoicesContract, 
				UserChoiceResponsesContract, 
				UserDrawResponsesContract, 
				UserOpenResponsesContract, 
				UserContract, 
				UserActivityTypeContract  };
	}
	
    public final FormsContract FormsContract = new FormsContract();
    public final FormsQuestionsXRefContract FormsQuestionsXRefContract = new FormsQuestionsXRefContract();
    public final LogoContract LogoContract = new LogoContract();
    public final QuestionContract QuestionContract = new QuestionContract();
    public final QuestionTypeContract QuestionTypeContract = new QuestionTypeContract();
    public final ResponseChoicesContract ResponseChoicesContract = new ResponseChoicesContract();
    public final UserChoiceResponsesContract UserChoiceResponsesContract = new UserChoiceResponsesContract();
    public final UserDrawResponsesContract UserDrawResponsesContract = new UserDrawResponsesContract();
    public final UserOpenResponsesContract UserOpenResponsesContract = new UserOpenResponsesContract();
    public final UserContract UserContract = new UserContract();
    public final UserActivityTypeContract UserActivityTypeContract = new UserActivityTypeContract();
}
