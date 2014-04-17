package com.rhcloud.jop.formfactor.domain.repositories;

import com.rhcloud.jop.formfactor.domain.FreeResponseQuestion;

public interface IFreeResponseQuestionRepository
{
	public void Update(FreeResponseQuestion question);

	public void DeleteByID(long ID);

	public FreeResponseQuestion GetByID();

	public void Add(FreeResponseQuestion question);
}
