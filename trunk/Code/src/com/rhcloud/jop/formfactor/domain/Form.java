package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

public class Form
{
	public long ID = 0;
	public long UserID = 0;
	public String Title = "";
	public String Description = "";
	public Logo Logo = new Logo();
	
	public List<Question> Questions = new ArrayList<Question>();
}
