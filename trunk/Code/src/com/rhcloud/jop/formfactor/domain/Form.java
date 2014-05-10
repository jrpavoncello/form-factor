package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Form implements IJSONSerializable
{
    @SerializedName("ID")
	public long ID = 0;
    
    @SerializedName("UserID")
	public long UserID = 0;
    
    @SerializedName("Title")
	public String Title = "";
    
    @SerializedName("Description")
	public String Description = "";
    
    @SerializedName("Logo")
	public Logo Logo = new Logo();
    
    @SerializedName("ExternalID")
	public long ExternalID = 0;

    @SerializedName("Questions")
	public List<Question> Questions = new ArrayList<Question>();

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
		Form form = gson.fromJson(json, this.getClass());
		
		this.ID = form.ID;
		this.UserID = form.UserID;
		this.Title = form.Title;
		this.Description = form.Description;
		this.Logo = form.Logo;
		this.ExternalID = form.ExternalID;
		this.Questions = form.Questions;
	}
}
