package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.FreeDrawResponse;

public interface IFreeDrawResponseRepository
{
	public void Add(FreeDrawResponse response);

	public FreeDrawResponse GetByQuestionResponseID(FreeDrawResponse response);
	
	public void DeleteByQuestionResponseID(long ID);
	
	public void DeleteByQuestionResponseIDsNotIn(Long[] IDs, long responseID);

	public void Update(FreeDrawResponse response);

	public void DeleteByUserResponseID(long ID);
}
