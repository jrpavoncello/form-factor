package com.rhcloud.jop.formfactor.domain;

import java.util.HashSet;
import java.util.Set;

public class Connection
{
	private Connection(int connectionID)
	{
		ConnectionID = connectionID;
	}
	
	public static Connection newInstance()
	{
		Connection connection = new Connection(NextConnectionID++);
		
		return connection;
	}
	
	private static int NextConnectionID = 0;
	private static Set<Integer> ConnectionsOpen = new HashSet<Integer>();
	
	private int ConnectionID = 0;
	
	public void CloseConnection()
	{
		ConnectionsOpen.remove(Integer.valueOf(ConnectionID));
	}
}
