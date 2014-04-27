package com.rhcloud.jop.formfactor.domain;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements IJSONSerializable
{
    @SerializedName("User_ID")
	public long ID = 0;
    
    @SerializedName("User_Username")
	public String Username = "";
    
    @SerializedName("User_Email")
	public String Email = "";
    
    @SerializedName("User_Password")
	public char[] Password = null;

	@Override
	public String Serialize()
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	@Override
	public void Read(String json)
	{
		Gson gson = new Gson();
		User newUser = gson.fromJson(json, this.getClass());
		
		this.ID = newUser.ID;
		this.Username = newUser.Username;
		this.Email = newUser.Email;
		this.Password = newUser.Password;
	}
}
