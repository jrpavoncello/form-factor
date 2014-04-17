package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.MultipleChoiceQuestion;

public interface IMultipleChoiceQuestionRepository
{
	public void Add(MultipleChoiceQuestion question);

	public void DeleteByQuestionID(long ID);
	
	public void Update(MultipleChoiceQuestion question);

	public MultipleChoiceQuestion GetByQuestionID(MultipleChoiceQuestion question);
}
