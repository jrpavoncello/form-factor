package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class MultipleChoiceResponse extends QuestionResponse implements IJSONSerializable
{
    @SerializedName("MultipleChoiceQuestionID")
	public long MultipleChoiceResponseID = 0;

    @SerializedName("ResponseChoiceID")
    public long ResponseChoiceID = 0;

	@Override
	public String Serialize()
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public void Read(String json)
	{
		Gson gson = new GsonBuilder().serializeNulls().create();
		MultipleChoiceResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserResponseID = response.UserResponseID;
		this.QuestionID = response.QuestionID;
		
		this.MultipleChoiceResponseID = response.MultipleChoiceResponseID;
		this.ResponseChoiceID = response.ResponseChoiceID;
	}
}
