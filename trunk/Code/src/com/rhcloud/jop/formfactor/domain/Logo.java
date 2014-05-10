package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

public class Logo implements IJSONSerializable
{
    @SerializedName("ID")
	public long ID = 0;
    
    @SerializedName("Image")
	public byte[] Image = null;
    
    @SerializedName("Description")
	public String Description = "";

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
		Logo logo = gson.fromJson(json, this.getClass());
		
		this.ID = logo.ID;
		this.Image = logo.Image;
		this.Description = logo.Description;
	}
}
