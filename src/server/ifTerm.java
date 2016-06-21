package server;

public class ifTerm extends ControlLoop 
{
	private int end_number = -1 ;
	
	@Override
	public boolean execute(Player obj) throws Exception
	{
		if (current_command != null)
		{
			/*
			 * Если первый раз зашли, то делаем проверку на условие
			 */
			if (current_number == 0)
			{
				switch (isCondition(obj))
				{
				case 0:	// Если условие совпадает, то проверяем, есть ли else.
						if (number_else == -1)
							end_number = commands.size();
						else
							//Если условие выполняется и есть else, то он будет и концом if'a
							end_number = number_else;
					break;
				case 1:	// Если не совпадает, то переходим к else, или вообще выходим.
					if (number_else>-1)
					{
						end_number = commands.size();
						current_number = number_else;
						current_command = commands.get(current_number);
					}
					else
						return true;
					
					break;
				case -1:
					System.out.println("return -1");
					throw new Exception("Непредвиденная ошибка в условии");
					//return true;
				}
			}
			
			if (current_command.execute(obj))
			{
				if (++current_number != end_number)
				{
					current_command = commands.get(current_number);
					return false;
				}
				else
				{
					current_number = 0;
					current_command = commands.get(current_number);
					return true;
				}
			}
			else
			{
				return false;
			}
		}
		
		return true;
	}
	
	public String toString()
	{
		String result = "if:{ condition:{ "+term1+" "+condition+" "+term2+" }, command:{";
		
		for (Command c :  commands)
		{
			result += c.toString()+", ";
		}
		
		result += " }";
		return result;
	}
}
