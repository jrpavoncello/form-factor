package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class QuestionResponse implements IJSONSerializable
{
    @SerializedName("ID")
	public long ID = 0;
    
    @SerializedName("UserResponseID")
    public long UserResponseID = 0;
    
    @SerializedName("QuestionID")
	public long QuestionID = 0;
    
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
		QuestionResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserResponseID = response.UserResponseID;
		this.QuestionID = response.QuestionID;
	}
}
