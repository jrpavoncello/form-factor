package com.rhcloud.jop.formfactor.domain;

public interface IJSONSerializable
{
	public String Serialize();
	public void Read(String json);
}
