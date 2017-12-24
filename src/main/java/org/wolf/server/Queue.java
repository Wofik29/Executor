package org.wolf.server;

import java.util.LinkedList;
import java.util.List;

abstract public class Queue implements Command 
{
	protected List<Command> commands = new LinkedList<Command>();
	protected Command current_command;
	protected int current_number = 0;
	protected boolean isEnd = true;
	
	public void add(Command c)
	{
		commands.add(c);
		if (current_command == null) 
		{
			current_command = c;
		}
	}
	
	public String toString()
	{
		String result = "";
		
		for (Command c : commands)
		{
			result += " { " + c.toString() + " }, ";
		}
		
		return result;
	}
	
	abstract public boolean execute(Player obj) throws Exception;
}
