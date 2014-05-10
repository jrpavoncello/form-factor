package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class FormResponse implements IJSONSerializable
{
    @SerializedName("ID")
	public long ID = 0;
    
    @SerializedName("UserID")
	public long UserID = 0;
    
    @SerializedName("UserResponses")
	public List<UserResponse> UserResponses = new ArrayList<UserResponse>();
    
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
		FormResponse form = gson.fromJson(json, this.getClass());
		
		this.ID = form.ID;
		this.UserID = form.UserID;
		this.UserResponses = form.UserResponses;
	}
}
