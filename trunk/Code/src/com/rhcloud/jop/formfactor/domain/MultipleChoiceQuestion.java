package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question
{
	public int MinResponses = 0;
	public int MaxResponses = 0;
	
	public List<ResponseChoice> ResponseChoices = new ArrayList<ResponseChoice>();
}
