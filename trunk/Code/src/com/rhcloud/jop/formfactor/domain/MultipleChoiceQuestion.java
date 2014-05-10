package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class MultipleChoiceQuestion extends Question implements IJSONSerializable
{
    @SerializedName("MultipleChoiceQuestionID")
	public long MultipleChoiceQuestionID = 0;
    
    @SerializedName("MinResponses")
	public int MinResponses = 0;
    
    @SerializedName("MaxResponses")
	public int MaxResponses = 0;

    @SerializedName("ResponseChoices")
	public List<ResponseChoice> ResponseChoices = new ArrayList<ResponseChoice>();

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
		MultipleChoiceQuestion question = gson.fromJson(json, this.getClass());
		
		this.ID = question.ID;
		this.FormID = question.FormID;
		this.Number = question.Number;
		this.Type = question.Type;
		this.Question = question.Question;
		this.Image = question.Image;
		
		this.MultipleChoiceQuestionID = question.MultipleChoiceQuestionID;
		this.MinResponses = question.MinResponses;
		this.MaxResponses = question.MaxResponses;
	}
}
