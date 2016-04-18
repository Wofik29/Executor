package Game;


public class MainLoop extends Queue 
{
	
	public String toString()
	{
		String result ="MainLoop: { ";
		for (int i=0; i< commands.size(); i++)
		{
			Command c = commands.get(i);
			result += c.toString()+", ";
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
		//System.out.println(current_command.toString());
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			next();
		}
		
		// как только стент не true, то закончилось
		return !isEnd;
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
	
	public int getSize()
	{
		return commands.size();
	}
}
