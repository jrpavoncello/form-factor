package com.rhcloud.jop.formfactor.domain.repositories;

import java.util.List;

import com.rhcloud.jop.formfactor.domain.*;

public interface IQuestionRepository
{
	public void Add(Question question);
	
	public void DeleteByID(long formID);

	public void DeleteByIDsNotIn(long[] IDs, long formID);
	
	public void DeleteByFormID(long formID);
	
	public List<Question> GetAll();
	
	public List<Question> GetByFormID(long formID);
	
	public List<Long> GetByIDsNotIn(Long[] IDs, long formID);
	
	public List<Long> GetIDsByFormID(long formID);
	
	public Question GetByID(long ID);

	public void Update(Question question);

	public void UpdateSettings(long questionID, int min, int max);
}
