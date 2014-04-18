package com.rhcloud.jop.formfactor.domain.services;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.repositories.IFormRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.ILogoRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceQuestionRepository;
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
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
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
				result.Merge(AddUpdateQuestions(form.Questions, form.ID));
			}
			else
			{
				questionRepo.DeleteByFormID(form.ID);
				
				IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
				IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
				
				multipleChoiceQuestionRepo.DeleteByFormID(form.ID);
				freeResponseQuestionRepo.DeleteByFormID(form.ID);
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
	
	public Result AddUpdateQuestions(List<Question> questions, long formID)
	{
		Result result = new Result();
		
		for(Question question : questions)
		{
			question.FormID = formID;
			result.Merge(AddUpdateQuestion(question));
		}
		
		return result;
	}
	
	public Result AddUpdateQuestion(Question question)
	{
		Result result = new Result();
		
		try
		{
			IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
			IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();
			IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
			IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
			
			if(question.ID == 0)
			{
				questionRepo.Add(question);
			}
			else
			{
				questionRepo.Update(question);
			}

			ArrayList<Long> questionIDsMultipleChoice = new ArrayList<Long>();
			ArrayList<Long> questionIDsFreeResponse = new ArrayList<Long>();
			
			if(question instanceof MultipleChoiceQuestion)
			{
				questionIDsMultipleChoice.add(question.ID);
				
				MultipleChoiceQuestion q = (MultipleChoiceQuestion)question;
				multipleChoiceQuestionRepo.Add(q);
				
				int size = q.ResponseChoices.size();
				
				if(size > 0)
				{
					long[] choiceIDs = new long[size];
		
					for(int i = 0; i < size; i++)
					{
						ResponseChoice choice = q.ResponseChoices.get(i);
						choice.QuestionID = question.ID;
						choiceIDs[i] = choice.ID;
					}
					
					responseRepo.DeleteByIDsNotIn(choiceIDs, question.ID);
					
					for(ResponseChoice choice : q.ResponseChoices)
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
			else
			{
				if(question instanceof FreeResponseQuestion)
				{
					questionIDsFreeResponse.add(question.ID);

					FreeResponseQuestion q = (FreeResponseQuestion)question;
					freeResponseQuestionRepo.Add(q);
				}
			}
			
			Long[] ids = new Long[questionIDsMultipleChoice.size()];
			
			ids = questionIDsMultipleChoice.toArray(ids);
			
			questionRepo.DeleteByIDsNotIn(ids, question.FormID);
			multipleChoiceQuestionRepo.DeleteByQuestionIDsNotIn(ids, question.FormID);
			
			ids = new Long[questionIDsFreeResponse.size()];
			
			ids = questionIDsFreeResponse.toArray(ids);
			
			questionRepo.DeleteByIDsNotIn(ids, question.FormID);
			freeResponseQuestionRepo.DeleteByQuestionIDsNotIn(ids, question.FormID);
			
		}
		catch(Exception ex)
		{
			result.Success = false;
			result.Messages.add("Exception occured when trying to add/update questions:\r\n" + ex.getMessage());
		}
		
		return result;
	}
	
	public Question GetQuestionByID(long questionID)
	{
		Question question = null;
		
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		question = questionRepo.GetByID(questionID);

		this.FillQuestion(question);
			
		return question;
	}
	
	public List<Question> GetQuestionsByFormID(long formID)
	{
		List<Question> questions = new ArrayList<Question>();
		
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		questions = questionRepo.GetByFormID(formID);
		
		for(Question question : questions)
		{
			this.FillQuestion(question);
		}
		
		return questions;
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
	
	private void FillQuestion(Question question)
	{
		IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
		IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
		IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();
		
		if(question instanceof MultipleChoiceQuestion)
		{
			MultipleChoiceQuestion q = (MultipleChoiceQuestion)question;
			multipleChoiceQuestionRepo.GetByQuestionID(q);
		}
		else
		{
			if(question instanceof FreeResponseQuestion)
			{
				FreeResponseQuestion q = (FreeResponseQuestion)question;
				freeResponseQuestionRepo.GetByQuestionID(q);
			}
			else
			{
				if(question.Type == QuestionType.MultipleChoice)
				{
					MultipleChoiceQuestion q = new MultipleChoiceQuestion();
					
					q.ID = question.ID;
					q.FormID = question.FormID;
					q.Image = question.Image;
					q.Number = question.Number;
					q.Question = question.Question;
					q.Type = question.Type;
					q.ResponseChoices = responseRepo.GetByQuestionID(question.ID);
					
					question = q;
				}
				else
				{
					if(question.Type == QuestionType.FreeResponse)
					{
						FreeResponseQuestion q = new FreeResponseQuestion();
						
						q.ID = question.ID;
						q.FormID = question.FormID;
						q.Image = question.Image;
						q.Number = question.Number;
						q.Question = question.Question;
						q.Type = question.Type;
						
						question = q;
					}
				}
			}
		}
	}
	
	private void FillForms(List<Form> forms)
	{
		ILogoRepository logoRepo = DataContext.GetLogoRepository();
		
		for(Form form : forms)
		{
			form.Logo = logoRepo.GetByID(form.Logo.ID);
			
			form.Questions.addAll(this.GetQuestionsByFormID(form.ID));
		}
	}
}
