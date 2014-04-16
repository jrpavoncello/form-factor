package com.rhcloud.jop.formfactor.domain.repositories;

import java.util.List;

import com.rhcloud.jop.formfactor.domain.ResponseChoice;

public interface IResponseChoiceRepository
{
	public void Add(ResponseChoice choice);
	
	public void DeleteByQuestionID(long questionID);

	public void DeleteByIDsNotIn(long[] IDs, long questionID);
	
	public List<ResponseChoice> GetByQuestionID(long questionID);
	
	public ResponseChoice GetByID(long ID);

	public void Update(ResponseChoice choice);

	public void UpdateSettings(long choiceID, String choice);
}
