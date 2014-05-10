package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.MultipleChoiceResponse;

public interface IMultipleChoiceResponseRepository
{
	public void Add(MultipleChoiceResponse response);

	public MultipleChoiceResponse GetByQuestionResponseID(MultipleChoiceResponse response);

	public void DeleteByQuestionResponseID(long ID);
	
	public void DeleteByQuestionResponseIDsNotIn(Long[] IDs, long responseID);
	
	public void Update(MultipleChoiceResponse response);

	public void DeleteByUserResponseID(long ID);
}
