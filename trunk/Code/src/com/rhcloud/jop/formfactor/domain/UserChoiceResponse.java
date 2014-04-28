package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserChoiceResponse implements IJSONSerializable
{
    @SerializedName("UserDrawResponses_ID")
	public long ID = 0;
    
    @SerializedName("UserDrawResponses_UserID")
	public long UserID = 0;
    
    @SerializedName("UserOpenResponse_QuestionID")
	public long QuestionID = 0;
    
    @SerializedName("UserDrawResponses_Description")
	public String Description = "";
    
    @SerializedName("UserDrawResponses_Image")
	public byte[] Image = null;

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
		this.Description = response.Description;
		this.Image = response.Image;
	}
}
