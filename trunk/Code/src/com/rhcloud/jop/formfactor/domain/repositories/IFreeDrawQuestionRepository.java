package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.FreeDrawQuestion;

public interface IFreeDrawQuestionRepository
{
	public void Add(FreeDrawQuestion question);

	public FreeDrawQuestion GetByQuestionID(FreeDrawQuestion question);
	
	public void DeleteByQuestionID(long questionID);
	
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID);

	public void Update(FreeDrawQuestion question);

	public void DeleteByFormID(long formID);
}
