package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Question implements IJSONSerializable
{
    @SerializedName("Question_ID")
	public long ID = 0;
    
    @SerializedName("Question_FormID")
	public long FormID = 0;
    
    @SerializedName("Question_Number")
	public int Number = 0;
    
    @SerializedName("Question_Type")
	public QuestionType Type = QuestionType.None;
    
    @SerializedName("Question_Question")
	public String Question = "";
    
    @SerializedName("Question_Image")
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
		Question question = gson.fromJson(json, this.getClass());
		
		this.ID = question.ID;
		this.FormID = question.FormID;
		this.Number = question.Number;
		this.Type = question.Type;
		this.Question = question.Question;
		this.Image = question.Image;
	}
}
