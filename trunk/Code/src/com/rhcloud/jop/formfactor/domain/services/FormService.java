package com.rhcloud.jop.formfactor.domain.services;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.FreeDrawQuestion;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;
import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;
import com.rhcloud.jop.formfactor.domain.Question;
import com.rhcloud.jop.formfactor.domain.QuestionType;
import com.rhcloud.jop.formfactor.domain.ResponseChoice;
import com.rhcloud.jop.formfactor.domain.repositories.IFormRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeDrawQuestionRepository;
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
		return CreateUpdateForm(form, false);
	}

	public Result CreateUpdateForm(Form form, boolean createNew)
	{
		Result result = new Result();

		IFormRepository formRepo = DataContext.GetFormRepository();
		ILogoRepository logoRepo = DataContext.GetLogoRepository();
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		if(form != null)
		{
			try
			{
				if(form.Logo.ID == 0 || createNew)
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
				if(form.ID == 0 || createNew)
				{
					formRepo.Add(form);
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
				result.Merge(AddUpdateQuestions(form.Questions, form.ID, createNew));
			}
			else
			{
				questionRepo.DeleteByFormID(form.ID);
				
				IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
				IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
				IFreeDrawQuestionRepository freeDrawQuestionRepo = DataContext.GetFreeDrawQuestionRepository();
				
				multipleChoiceQuestionRepo.DeleteByFormID(form.ID);
				freeResponseQuestionRepo.DeleteByFormID(form.ID);
				freeDrawQuestionRepo.DeleteByFormID(form.ID);
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
		return AddUpdateQuestions(questions, formID, false);
	}
	
	public Result AddUpdateQuestions(List<Question> questions, long formID, boolean createNew)
	{
		Result result = new Result();
		
		int size = questions.size();
		
		Long[] IDs = new Long[size];
		
		for(int i = 0; i < size; i++)
		{
			Question question = questions.get(i);
			question.FormID = formID;
			result.Merge(AddUpdateQuestion(question, createNew));
			
			IDs[i] = question.ID;
		}
		
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		questionRepo.DeleteByIDsNotIn(IDs, formID);
		
		return result;
	}
	
	public Result AddUpdateQuestion(Question question)
	{
		return AddUpdateQuestion(question, false);
	}
	
	public Result AddUpdateQuestion(Question question, boolean createNew)
	{
		Result result = new Result();
		
		try
		{
			IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
			IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();
			IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
			IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
			IFreeDrawQuestionRepository freeDrawQuestionRepo = DataContext.GetFreeDrawQuestionRepository();
			
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
			ArrayList<Long> questionIDsFreeDraw = new ArrayList<Long>();
			
			if(question instanceof MultipleChoiceQuestion)
			{
				questionIDsMultipleChoice.add(question.ID);
				
				MultipleChoiceQuestion q = (MultipleChoiceQuestion)question;
				
				if(q.MultipleChoiceQuestionID == 0 || createNew)
				{
					multipleChoiceQuestionRepo.Add(q);
				}
				else
				{
					multipleChoiceQuestionRepo.Update(q);
				}
				
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
						if(choice.ID == 0 || createNew)
						{
							responseRepo.Add(choice);
						}
						else
						{
							choice.QuestionID = question.ID;

							responseRepo.Update(choice);
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
					
					if(q.FreeResponseQuestionID == 0 || createNew)
					{
						freeResponseQuestionRepo.Add(q);
					}
					else
					{
						freeResponseQuestionRepo.Update(q);
					}
				}
				else
				{
					questionIDsFreeDraw.add(question.ID);

					FreeDrawQuestion q = (FreeDrawQuestion)question;
					
					if(q.FreeDrawQuestionID == 0 || createNew)
					{
						freeDrawQuestionRepo.Add(q);
					}
					else
					{
						freeDrawQuestionRepo.Update(q);
					}
				}
			}
			
			Long[] ids = new Long[questionIDsMultipleChoice.size()];
			
			ids = questionIDsMultipleChoice.toArray(ids);

			if(ids.length > 0)
			{
				multipleChoiceQuestionRepo.DeleteByQuestionIDsNotIn(ids, question.FormID);
			}
			
			ids = new Long[questionIDsFreeResponse.size()];
			
			ids = questionIDsFreeResponse.toArray(ids);

			if(ids.length > 0)
			{
				freeResponseQuestionRepo.DeleteByQuestionIDsNotIn(ids, question.FormID);
			}
			
			ids = new Long[questionIDsFreeDraw.size()];
			
			ids = questionIDsFreeDraw.toArray(ids);

			if(ids.length > 0)
			{
				freeDrawQuestionRepo.DeleteByQuestionIDsNotIn(ids, question.FormID);
			}			
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

		question = this.FillQuestion(question);
			
		return question;
	}
	
	public List<Question> GetQuestionsByFormID(long formID)
	{
		List<Question> questions = new ArrayList<Question>();
		List<Question> modified = new ArrayList<Question>();
		
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		questions = questionRepo.GetByFormID(formID);
		
		for(Question question : questions)
		{
			modified.add(this.FillQuestion(question));
		}
		
		return modified;
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
	
	private Question FillQuestion(Question question)
	{
		IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
		IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
		IFreeDrawQuestionRepository freeDrawQuestionRepo = DataContext.GetFreeDrawQuestionRepository();
		IResponseChoiceRepository responseRepo = DataContext.GetResponseChoiceRepository();
		
		if(question instanceof MultipleChoiceQuestion)
		{
			MultipleChoiceQuestion q = (MultipleChoiceQuestion)question;
			multipleChoiceQuestionRepo.GetByQuestionID(q);
		}
		else if(question instanceof FreeResponseQuestion)
		{
			FreeResponseQuestion q = (FreeResponseQuestion)question;
			freeResponseQuestionRepo.GetByQuestionID(q);
		}
		else if(question instanceof FreeDrawQuestion)
		{
			FreeDrawQuestion q = (FreeDrawQuestion)question;
			freeDrawQuestionRepo.GetByQuestionID(q);
		}
		else if(question.Type == QuestionType.MultipleChoice)
		{
			MultipleChoiceQuestion q = new MultipleChoiceQuestion();
			
			q.ID = question.ID;
			q.FormID = question.FormID;
			q.Image = question.Image;
			q.Number = question.Number;
			q.Question = question.Question;
			q.Type = question.Type;
			q.ResponseChoices = responseRepo.GetByQuestionID(question.ID);
			
			multipleChoiceQuestionRepo.GetByQuestionID(q);
			
			question = q;
		}
		else if(question.Type == QuestionType.FreeResponse)
		{
			FreeResponseQuestion q = new FreeResponseQuestion();
			
			q.ID = question.ID;
			q.FormID = question.FormID;
			q.Image = question.Image;
			q.Number = question.Number;
			q.Question = question.Question;
			q.Type = question.Type;
			
			freeResponseQuestionRepo.GetByQuestionID(q);
			
			question = q;
		}
		else if(question.Type == QuestionType.FreeDraw)
		{
			FreeDrawQuestion q = new FreeDrawQuestion();
			
			q.ID = question.ID;
			q.FormID = question.FormID;
			q.Image = question.Image;
			q.Number = question.Number;
			q.Question = question.Question;
			q.Type = question.Type;
			
			freeDrawQuestionRepo.GetByQuestionID(q);
			
			question = q;
		}
		
		return question;
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
