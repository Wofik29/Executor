package Game;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Queue implements Command 
{
	List<Command> commands = new LinkedList<Command>();
	Command current_command;
	int current_number = 0;
	boolean isEnd = true;
	//ListIterator<Command> it = commands.listIterator();
	
	public void add(Command c)
	{
		commands.add(c);
		if (current_command == null) 
		{
			current_command = c;
		}
	}
	
	public void StringTo()
	{
		for (Command c : commands)
		{
			System.out.println(c.toString());
		}
	}
	
	public boolean execute(GameObject obj)
	{
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			System.out.println("execute "+current_command.toString()+" - "+current_number);
			next();
			return true;
		}
		else
			return false;
		
		
	}
	
	public void next()
	{
		//StringTo();
		if (++current_number != commands.size())
		{
			current_command = commands.get(current_number);
		}
		else 
		{
			isEnd = false;
		}
	}
}
