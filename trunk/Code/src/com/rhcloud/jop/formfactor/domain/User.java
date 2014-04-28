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
    
    @SerializedName("User_IsDefault")
	public boolean IsDefault = false;

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
		User user = gson.fromJson(json, this.getClass());
		
		this.ID = user.ID;
		this.Username = user.Username;
		this.Email = user.Email;
		this.Password = user.Password;
	}
}
