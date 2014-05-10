package com.rhcloud.jop.formfactor.domain.repositories;

import java.util.List;

import com.rhcloud.jop.formfactor.domain.*;

public interface IQuestionResponseRepository
{
	public void Add(QuestionResponse response);
	
	public void DeleteByID(long questionResponseID);

	public void DeleteByIDsNotIn(Long[] IDs, long userResponseID);
	
	public void DeleteByUserResponseID(long userResponseID);
	
	public List<QuestionResponse> GetAll();
	
	public List<QuestionResponse> GetByUserResponseID(long userResponseID);
	
	public List<Long> GetByIDsNotIn(Long[] IDs, long userResponseID);
	
	public List<Long> GetIDsByUserResponseID(long userResponseID);
	
	public QuestionResponse GetByID(long ID);

	public void Update(QuestionResponse response);
}
