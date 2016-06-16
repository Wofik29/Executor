package server;

public class WhileLoop extends ControlLoop
{
	@Override
	public boolean execute(Player obj) throws Exception
	{
		if (current_command != null)
		{
			/*
			 * Если мы в начале, то проверяем на условие
			 */
			if (current_number == 0)
			{
				if  (isCondition(obj) != 0)
				{
					return true;
				}
			}
			
			if (current_command.execute(obj))
			{
				current_number = ++current_number >= commands.size() ? 0 : current_number;
			}
			
			current_command = commands.get(current_number);
			return false;
		}
		return true;
	}

	@Override
	public String toString() 
	{
		String result = "WhileLoop command : { ";
		
		for (Command c : commands)
		{
			result += c.toString()+", ";
		}
				
		return result+" }";
	};

}
