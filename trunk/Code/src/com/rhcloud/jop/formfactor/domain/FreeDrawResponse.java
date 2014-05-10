package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class FreeDrawResponse extends QuestionResponse implements IJSONSerializable
{
    @SerializedName("FreeDrawResponseID")
	public long FreeDrawResponseID = 0;

    @SerializedName("Drawing")
    public byte[] Drawing = null;

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
		FreeDrawResponse response = gson.fromJson(json, this.getClass());
		
		this.ID = response.ID;
		this.UserResponseID = response.UserResponseID;
		this.QuestionID = response.QuestionID;
		
		this.FreeDrawResponseID = response.FreeDrawResponseID;
		this.Drawing = response.Drawing;
	}
}
