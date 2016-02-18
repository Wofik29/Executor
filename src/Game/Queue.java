package Game;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Queue implements Command 
{
	List<Command> commands = new LinkedList<Command>();
	Command current;
	ListIterator<Command> it = commands.listIterator();
	
	public void add(Command c)
	{
		commands.add(c);
		if (current == null) current = c;
	}
	
	public boolean execute(GameObject obj)
	{
		if (current != null && current.execute(obj))
		{
			next();
			System.out.println("execute");
		}
		return true;
	}
	
	public void next()
	{
		if (it.hasNext()) current = it.next();
	}
}
