package com.rhcloud.jop.formfactor.domain.services;

import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.Form;
import com.rhcloud.jop.formfactor.domain.FormResponse;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.repositories.IFormRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeDrawQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.ILogoRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IUserResponseRepository;

public class ResponseService
{
	IFormFactorDataContext DataContext;
	
	public ResponseService(IFormFactorDataContext dataContext)
	{
		this.DataContext = dataContext;
	}

	public Result CreateUpdateFormResponse(FormResponse formResponse)
	{
		Result result = new Result();

		IUserResponseRepository formRepo = DataContext.GetResponseRepository();
		
		
		
		/*
		if(formResponse != null)
		{
			try
			{
				if(formResponse.ID == 0)
				{
					formRepo.Add(formResponse);
				}
				else
				{
					formRepo.Update(formResponse);
				}
			}
			catch(Exception ex)
			{
				result.Success = false;
				result.Messages.add("Exception occured when trying to add/update logo:\r\n" + ex.getMessage());
			}
			
			int size = formResponse.UserResponses.size();
			
			if(size > 0)
			{
				result.Merge(AddUpdateQuestions(formResponse.UserResponses, formResponse.ID));
			}
			else
			{
				questionRepo.DeleteByFormID(formResponse.ID);
				
				IMultipleChoiceQuestionRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceQuestionRepository();
				IFreeResponseQuestionRepository freeResponseQuestionRepo = DataContext.GetFreeResponseQuestionRepository();
				IFreeDrawQuestionRepository freeDrawQuestionRepo = DataContext.GetFreeDrawQuestionRepository();
				
				multipleChoiceQuestionRepo.DeleteByFormID(formResponse.ID);
				freeResponseQuestionRepo.DeleteByFormID(formResponse.ID);
				freeDrawQuestionRepo.DeleteByFormID(formResponse.ID);
			}
			
			return result;
		}
		else
		{
			result.Success = false;
			result.Messages.add("No form to create.");
		}*/
		
		return result;
	}
}
