package Game;


public class MainLoop extends Queue 
{
	
	public String toString()
	{
		String result ="MainLoop: { ";
		for (Command c : commands)
		{
			result += c.toString();
		}
		return result+" }";
	}
	
	public boolean execute(GameObject obj)
	{
		//System.out.println("execute "+current_command.toString()+" - "+current_number);
		
		/*
		 * execute возвращает bool
		 * если в if вернеться true, то данная команда завершилась и мы выполняем следующую команду.
		 * Иначе эта команда цикл/блок и в нем не закончились команды.
		 */
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			//System.out.println("execute "+current_command.toString()+" - "+current_number);
			//System.out.println("mainLoop: "+isEnd);
			
			next();
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void next()
	{
		if (++current_number != commands.size())
		{
			current_command = commands.get(current_number);
		}
		else 
		{
			//current_number = 0;
			//current_command = commands.get(current_number);
			isEnd = false;
		}
	}
}
