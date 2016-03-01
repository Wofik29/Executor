package Game;

import java.util.LinkedList;
import java.util.List;

abstract public class Queue implements Command 
{
	List<Command> commands = new LinkedList<Command>();
	Command current_command;
	int current_number = 0;
	boolean isEnd = true;
	
	public void add(Command c)
	{
		//System.out.println("added "+c.toString());
		commands.add(c);
		if (current_command == null) 
		{
			current_command = c;
			//System.out.println("set : "+c.toString());
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
	
	abstract public boolean execute(GameObject obj);
	
}
