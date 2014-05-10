package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserResponse implements IJSONSerializable
{
    @SerializedName("ID")
	public long ID = 0;
    
    @SerializedName("UserID")
	public long UserID = 0;
    
    @SerializedName("QuestionResponses")
    public List<QuestionResponse> QuestionResponses = new ArrayList<QuestionResponse>();
    
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
		UserResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserID = response.UserID;
		this.QuestionResponses = response.QuestionResponses;
	}
}
