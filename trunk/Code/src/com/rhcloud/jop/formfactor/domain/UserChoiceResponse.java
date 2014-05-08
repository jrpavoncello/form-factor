package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserChoiceResponse extends UserResponse implements IJSONSerializable
{
    @SerializedName("UserOpenResponse_ResponseChoiceID")
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
		UserChoiceResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserID = response.UserID;
		this.QuestionID = response.QuestionID;
		this.ResponseChoiceID = response.ResponseChoiceID;
	}
}
