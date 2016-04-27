package Game;

public class ifTerm extends ControlLoop 
{
	private int sign = -1 ;
	
	@Override
	public boolean execute(GameObject obj) throws Exception
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
							sign = commands.size();
						else
							//Если условие выполняется и есть else, то он будет и концом if'a
							sign = number_else;
					break;
				case 1:	// Если не совпадает, то переходим к else, или вообще выходим.
					if (number_else>-1)
					{
						sign = commands.size();
						current_number = number_else;
						current_command = commands.get(current_number);
					}
					else
						return true;
					
					break;
				case -1:
					System.out.println("return -1");
					return true;
					
				}
				
			}
			
			System.out.println("ifTerm: "+current_command.toString());
			
			if (current_command.execute(obj))
			{
				if (++current_number != sign)
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
