package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.FreeResponseResponse;

public interface IFreeResponseResponseRepository
{
	public void Add(FreeResponseResponse response);

	public FreeResponseResponse GetByQuestionResponseID(FreeResponseResponse response);
	
	public void DeleteByQuestionResponseID(long ID);
	
	public void DeleteByQuestionResponseIDsNotIn(Long[] IDs, long responseID);

	public void Update(FreeResponseResponse response);

	public void DeleteByUserResponseID(long ID);
}
