package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;

public interface IMultipleChoiceQuestionRepository
{
	public void Add(MultipleChoiceQuestion question);

	public MultipleChoiceQuestion GetByQuestionID(MultipleChoiceQuestion question);

	public void DeleteByQuestionID(long ID);
	
	public void DeleteByQuestionIDsNotIn(Long[] IDs, long formID);
	
	public void Update(MultipleChoiceQuestion question);

	public void DeleteByFormID(long formID);
}
