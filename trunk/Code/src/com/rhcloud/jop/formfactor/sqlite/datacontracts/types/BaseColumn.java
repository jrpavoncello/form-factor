package com.rhcloud.jop.formfactor.sqlite.datacontracts.types;

public final class BaseColumn
{
	private IColumnType Type;
	
	private String Name = "";
	
	public int Index = 0;
	
	public BaseColumn(String name, IColumnType type)
	{
		this.Name = name;
		this.Type = type;
	}
	
	public IColumnType GetType()
	{
		return this.Type;
	}
	
	public String GetName()
	{
		return this.Name;
	}
	
	@Override
	public String toString()
	{
		return this.Name;
	}
	
	public static BlobType BlobType = new BlobType();

	public static IntegerType IntegerType = new IntegerType();

	public static RealType RealType = new RealType();

	public static TextType TextType = new TextType();

	public static NullType NullType = new NullType();
}