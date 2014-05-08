package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserResponse implements IJSONSerializable
{
    @SerializedName("UserResponse_ID")
	public long ID = 0;
    
    @SerializedName("UserResponse_UserID")
	public long UserID = 0;
    
    @SerializedName("UserResponse_QuestionID")
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
		UserResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserID = response.UserID;
		this.QuestionID = response.QuestionID;
	}
}
