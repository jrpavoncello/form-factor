package com.rhcloud.jop.formfactor.domain;

import java.util.ArrayList;
import java.util.List;

public class Question
{
	public long ID = 0;
	public long FormID = 0;
	public int Number = 0;
	public QuestionType Type = QuestionType.None;
	public String Question = "";
	public byte[] Image = null;
}
