package com.rhcloud.jop.formfactor.domain.services;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.repositories.IFormRepository;
import com.rhcloud.jop.formfactor.domain.repositories.ILogoRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IResponseChoiceRepository;

public class FormService
{
	IFormFactorDataContext DataContext;
	
	public FormService(IFormFactorDataContext dataContext)
	{
		this.DataContext = dataContext;
	}

	public Result CreateUpdateForm(Form form)
	{
		Result result = new Result();

		IFormRepository formRepo = DataContext.GetFormRepository();
		ILogoRepository logoRepo = DataContext.GetLogoRepository();
		
		if(form != null)
		{
			try
			{
				if(form.Logo.ID == 0)
				{
					logoRepo.Add(form.Logo);
				}
				else
				{
					logoRepo.Update(form.Logo);
				}
			}
			catch(Exception ex)
			{
				result.Success = false;
				result.Messages.add("Exception occured when trying to add/update logo:\r\n" + ex.getMessage());
			}
			
			try
			{
				if(form.ID == 0)
				{
					formRepo.Add(form);
					
					form.Title = "New Form " + form.ID;
				}
				else
				{
					formRepo.Update(form);
				}
			}
			catch(Exception ex)
			{
				result.Success = false;
				result.Messages.add("Exception occured when trying to add/update form:\r\n" + ex.getMessage());
			}
			
			int size = form.Questions.size();
			
			if(size > 0)
			{
				ArrayList<Long> questionIDsMultipleChoice = new ArrayList<Long>();
				ArrayList<Long> questionIDsFreeResponse = new ArrayList<Long>();
	
				for(int i = 0; i < size; i++)
				{
					Question question = form.Questions.get(i);
					question.FormID = form.ID;
					
					if(question instanceof MultipleChoiceQuestion)
					{
						questionIDsMultipleChoice.add(question.ID);
					}
					else
					{
						if(question instanceof FreeResponseQuestion)
						{
							questionIDsFreeResponse.add(question.ID);
						}
					}
				}
				
				IQuestionRepository questionRepo = DataContext.GetQuestionRepository(MultipleChoiceQuestion.class, );
				Long[] ids;
				questionRepo.DeleteByIDsNotIn(questionIDsMultipleChoice.toArray(ids), form.ID);
				
				for(Question question : form.Questions)
				{
					result.Merge(AddUpdateQuestion(question));
				}
			}
			else
			{
				questionRepo.DeleteByFormID(form.ID);
			}
			
			return result;
		}
		else
		{
			result.Success = false;
			result.Messages.add("No form to create.");
		}
		
		return result;
	}
	
	public Result AddUpdateQuestion(Question question)
	{
		Result result = new Result();
		
		try
		{
			IQuestionRepository questionRepo = DataContext.GetQuestionRepository(MultipleChoiceQuestion.class);
			IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();

			if(question.ID == 0)
			{
				questionRepo.Add(question);
			}
			else
			{
				questionRepo.Update(question);
			}
				
			int size = question.ResponseChoices.size();
			
			if(size > 0)
			{
				long[] choiceIDs = new long[size];
	
				for(int i = 0; i < size; i++)
				{
					ResponseChoice choice = question.ResponseChoices.get(i);
					choice.QuestionID = question.ID;
					choiceIDs[i] = choice.ID;
				}
				
				responseRepo.DeleteByIDsNotIn(choiceIDs, question.ID);
				
				for(ResponseChoice choice : question.ResponseChoices)
				{
					if(choice.ID != 0)
					{
						choice.QuestionID = question.ID;

						responseRepo.Update(choice);
					}
					else
					{
						responseRepo.Add(choice);
					}
				}
			}
			else
			{
				responseRepo.DeleteByQuestionID(question.ID);
			}
		}
		catch(Exception ex)
		{
			result.Success = false;
			result.Messages.add("Exception occured when trying to add/update questions:\r\n" + ex.getMessage());
		}
		
		return result;
	}
	
	public Form GetFormByID(long formID)
	{
		IFormRepository formRepo = DataContext.GetFormRepository();
		
		Form form = formRepo.GetByID(formID);
		
		List<Form> temp = new ArrayList<Form>();
		temp.add(form);
		
		this.FillForms(temp);
		
		return form;
	}
	
	public List<Form> GetFormsByUserID(long userID)
	{
		IFormRepository formRepo = DataContext.GetFormRepository();
		
		List<Form> forms = formRepo.GetByUserID(userID);
		
		this.FillForms(forms);
		
		return forms;
	}
	
	private void FillForms(List<Form> forms)
	{
		ILogoRepository logoRepo = DataContext.GetLogoRepository();
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();
		
		for(Form form : forms)
		{
			form.Logo = logoRepo.GetByID(form.Logo.ID);
			
			form.Questions.addAll(questionRepo.GetByFormID(form.ID));
			
			for(Question question : form.Questions)
			{
				question.ResponseChoices = responseRepo.GetByQuestionID(question.ID);
			}
		}
	}
}
