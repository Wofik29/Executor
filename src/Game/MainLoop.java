package Game;


public class MainLoop extends Queue 
{
	
	public void StringTo()
	{
		for (Command c : commands)
		{
			System.out.println(c.toString());
		}
	}
	
	public boolean execute(GameObject obj)
	{
		//System.out.println("execute "+current_command.toString()+" - "+current_number);
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			System.out.println("execute "+current_command.toString()+" - "+current_number);
			
			next();
			return true;
		}
		else
		{
				
			return false;
		}
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
