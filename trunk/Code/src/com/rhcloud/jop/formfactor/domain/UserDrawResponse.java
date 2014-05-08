package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class UserDrawResponse extends UserResponse implements IJSONSerializable
{    
    @SerializedName("UserDrawResponse_Description")
	public String Description = "";
    
    @SerializedName("UserDrawResponse_Image")
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
		UserDrawResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserID = response.UserID;
		this.QuestionID = response.QuestionID;
		this.Description = response.Description;
		this.Image = response.Image;
	}
}
