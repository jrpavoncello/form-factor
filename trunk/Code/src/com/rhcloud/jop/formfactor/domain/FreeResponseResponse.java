package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class FreeResponseResponse extends QuestionResponse implements IJSONSerializable
{
    @SerializedName("FreeResponseResponseID")
	public long FreeResponseResponseID = 0;

    @SerializedName("Response")
    public String Response = "";

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
		FreeResponseResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserResponseID = response.UserResponseID;
		this.QuestionID = response.QuestionID;
		
		this.FreeResponseResponseID = response.FreeResponseResponseID;
		this.Response = response.Response;
	}
}
