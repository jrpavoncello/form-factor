package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserOpenResponse implements IJSONSerializable
{
    @SerializedName("UserOpenResponse_ID")
	public long ID = 0;
    
    @SerializedName("UserOpenResponse_UserID")
	public long UserID = 0;
    
    @SerializedName("UserOpenResponse_QuestionID")
	public long QuestionID = 0;
    
    @SerializedName("UserOpenResponse_Response")
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
		UserOpenResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserID = response.UserID;
		this.QuestionID = response.QuestionID;
		this.Response = response.Response;
	}
}
