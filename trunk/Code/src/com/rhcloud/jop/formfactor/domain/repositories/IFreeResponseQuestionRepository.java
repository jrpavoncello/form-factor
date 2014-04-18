package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;

public interface IFreeResponseQuestionRepository
{
	public void Add(FreeResponseQuestion question);

	public FreeResponseQuestion GetByQuestionID(FreeResponseQuestion question);
	
	public void DeleteByQuestionID(long questionID);
	
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID);

	public void Update(FreeResponseQuestion question);

	public void DeleteByFormID(long formID);
}
