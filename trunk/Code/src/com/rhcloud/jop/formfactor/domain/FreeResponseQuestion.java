package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class FreeResponseQuestion extends Question implements IJSONSerializable
{
    @SerializedName("FreeResponseQuestion_FreeResponseQuestionID")
	public long FreeResponseQuestionID = 0;
    
    @SerializedName("FreeResponseQuestion_MaxLength")
	public int MaxLength = 0;
    
    @SerializedName("FreeResponseQuestion_MinLength")
	public int MinLength = 0;
    
    @SerializedName("FreeResponseQuestion_Lines")
	public int Lines = 0;

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
		FreeResponseQuestion question = gson.fromJson(json, this.getClass());
		
		this.ID = question.ID;
		this.FormID = question.FormID;
		this.Number = question.Number;
		this.Type = question.Type;
		this.Question = question.Question;
		this.Image = question.Image;
		
		this.FreeResponseQuestionID = question.FreeResponseQuestionID;
		this.MaxLength = question.MaxLength;
		this.MinLength = question.MinLength;
		this.Lines = question.Lines;
	}
}
