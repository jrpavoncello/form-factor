package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class ResponseChoice implements IJSONSerializable
{
    @SerializedName("ResponseChoice_ID")
	public long ID = 0;
    
    @SerializedName("ResponseChoice_QuestionID")
	public long QuestionID = 0;
    
    @SerializedName("ResponseChoice_Choice")
	public String Choice = "";

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
		ResponseChoice choice = gson.fromJson(json, this.getClass());
		
		this.ID = choice.ID;
		this.QuestionID = choice.QuestionID;
		this.Choice = choice.Choice;
	}
}
