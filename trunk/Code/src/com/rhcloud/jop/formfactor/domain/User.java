package com.rhcloud.jop.formfactor.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable
{
	public long ID = 0;
	public String Username = "";
	public String Email = "";
	public byte[] Password = null;
	
	public User(Parcel in)
	{
		this.ID = in.readLong();
		this.Username = in.readString();
		this.Email = in.readString();
		in.readByteArray(this.Password);
	}
	
	public User() { }

	@Override
	public int describeContents()
	{
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeLong(this.ID);
		dest.writeString(this.Username);
		dest.writeString(this.Email);
		dest.writeByteArray(Password);
	}
}
