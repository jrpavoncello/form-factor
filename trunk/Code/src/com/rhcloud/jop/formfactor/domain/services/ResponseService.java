package com.rhcloud.jop.formfactor.domain.services;

import java.util.ArrayList;
import java.util.List;

import com.rhcloud.jop.formfactor.common.Result;
import com.rhcloud.jop.formfactor.domain.FreeDrawResponse;
import com.rhcloud.jop.formfactor.domain.FreeResponseResponse;
import com.rhcloud.jop.formfactor.domain.IFormFactorDataContext;
import com.rhcloud.jop.formfactor.domain.MultipleChoiceResponse;
import com.rhcloud.jop.formfactor.domain.QuestionResponse;
import com.rhcloud.jop.formfactor.domain.UserResponse;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeDrawResponseRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IFreeResponseResponseRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IMultipleChoiceResponseRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IQuestionResponseRepository;
import com.rhcloud.jop.formfactor.domain.repositories.IUserResponseRepository;

public class ResponseService
{
	IFormFactorDataContext DataContext;
	
	public ResponseService(IFormFactorDataContext dataContext)
	{
		this.DataContext = dataContext;
	}

	public Result CreateUpdateUserResponse(UserResponse response)
	{
		Result result = new Result();

		IUserResponseRepository userResponseRepo = DataContext.GetUserResponseRepository();
		IQuestionResponseRepository questionResponseRepo = DataContext.GetQuestionResponseRepository();
		
		if(response != null)
		{
			try
			{
				if(response.ID == 0)
				{
					userResponseRepo.Add(response);
				}
				else
				{
					userResponseRepo.Update(response);
				}
			}
			catch(Exception ex)
			{
				result.Success = false;
				result.Messages.add("Exception occured when trying to add/update logo:\r\n" + ex.getMessage());
			}
			
			int size = response.QuestionResponses.size();
			
			if(size > 0)
			{
				result.Merge(AddUpdateQuestionResponses(response.QuestionResponses, response.ID));
			}
			else
			{
				questionResponseRepo.DeleteByUserResponseID(response.ID);
				
				IMultipleChoiceResponseRepository multipleChoiceQuestionRepo = DataContext.GetMultipleChoiceResponseRepository();
				IFreeResponseResponseRepository freeResponseQuestionRepo = DataContext.GetFreeResponseResponseRepository();
				IFreeDrawResponseRepository freeDrawQuestionRepo = DataContext.GetFreeDrawResponseRepository();
				
				multipleChoiceQuestionRepo.DeleteByUserResponseID(response.ID);
				freeResponseQuestionRepo.DeleteByUserResponseID(response.ID);
				freeDrawQuestionRepo.DeleteByUserResponseID(response.ID);
			}
			
			return result;
		}
		else
		{
			result.Success = false;
			result.Messages.add("No form response to create.");
		}
		
		return result;
	}

	private Result AddUpdateQuestionResponses(List<QuestionResponse> questionResponses, long userResponseID)
	{
		Result result = new Result();
		
		int size = questionResponses.size();
		
		Long[] IDs = new Long[size];
		
		for(int i = 0; i < size; i++)
		{
			QuestionResponse questionResponse = questionResponses.get(i);
			questionResponse.UserResponseID = userResponseID;
			result.Merge(AddUpdateQuestionResponse(questionResponse));
			
			IDs[i] = questionResponse.ID;
		}
		
		IQuestionRepository questionRepo = DataContext.GetQuestionRepository();
		
		questionRepo.DeleteByIDsNotIn(IDs, userResponseID);
		
		return result;
	}

	private Result AddUpdateQuestionResponse(QuestionResponse questionResponse)
	{
		Result result = new Result();
		
		try
		{
			IQuestionResponseRepository questionResponseRepo = DataContext.GetQuestionResponseRepository();
			IMultipleChoiceResponseRepository multipleChoiceResponseRepo = DataContext.GetMultipleChoiceResponseRepository();
			IFreeResponseResponseRepository freeResponseResponseRepo = DataContext.GetFreeResponseResponseRepository();
			IFreeDrawResponseRepository freeDrawResponseRepo = DataContext.GetFreeDrawResponseRepository();
			
			if(questionResponse.ID == 0)
			{
				questionResponseRepo.Add(questionResponse);
			}
			else
			{
				questionResponseRepo.Update(questionResponse);
			}

			ArrayList<Long> responseIDsMultipleChoice = new ArrayList<Long>();
			ArrayList<Long> responseIDsFreeResponse = new ArrayList<Long>();
			ArrayList<Long> responseIDsFreeDraw = new ArrayList<Long>();
			
			if(questionResponse instanceof MultipleChoiceResponse)
			{
				responseIDsMultipleChoice.add(questionResponse.ID);
				
				MultipleChoiceResponse q = (MultipleChoiceResponse)questionResponse;
				
				if(q.MultipleChoiceResponseID == 0)
				{
					multipleChoiceResponseRepo.Add(q);
				}
				else
				{
					multipleChoiceResponseRepo.Update(q);
				}
			}
			else
			{
				if(questionResponse instanceof FreeResponseResponse)
				{
					responseIDsFreeResponse.add(questionResponse.ID);

					FreeResponseResponse q = (FreeResponseResponse)questionResponse;
					
					if(q.FreeResponseResponseID == 0)
					{
						freeResponseResponseRepo.Add(q);
					}
					else
					{
						freeResponseResponseRepo.Update(q);
					}
				}
				else
				{
					responseIDsFreeDraw.add(questionResponse.ID);

					FreeDrawResponse q = (FreeDrawResponse)questionResponse;
					
					if(q.FreeDrawResponseID == 0)
					{
						freeDrawResponseRepo.Add(q);
					}
					else
					{
						freeDrawResponseRepo.Update(q);
					}
				}
			}
			
			Long[] ids = new Long[responseIDsMultipleChoice.size()];
			
			ids = responseIDsMultipleChoice.toArray(ids);

			if(ids.length > 0)
			{
				multipleChoiceResponseRepo.DeleteByQuestionResponseIDsNotIn(ids, questionResponse.UserResponseID);
			}
			
			ids = new Long[responseIDsFreeResponse.size()];
			
			ids = responseIDsFreeResponse.toArray(ids);

			if(ids.length > 0)
			{
				freeResponseResponseRepo.DeleteByQuestionResponseIDsNotIn(ids, questionResponse.UserResponseID);
			}
			
			ids = new Long[responseIDsFreeDraw.size()];
			
			ids = responseIDsFreeDraw.toArray(ids);

			if(ids.length > 0)
			{
				freeDrawResponseRepo.DeleteByQuestionResponseIDsNotIn(ids, questionResponse.UserResponseID);
			}			
		}
		catch(Exception ex)
		{
			result.Success = false;
			result.Messages.add("Exception occured when trying to add/update question responses:\r\n" + ex.getMessage());
		}
		
		return result;
	}
}
