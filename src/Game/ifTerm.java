package Game;

import java.awt.Point;

public class ifTerm extends ControlLoop 
{
	private int sign = -1 ;
	
	@Override
	public boolean execute(GameObject obj)
	{
	
		if (current_command != null)
		{
					
			/*
			 * Если первый раз зашли, то делаем проверку на условие
			 */
			if (current_number == 0)
			{
				Point p = new Point();
				
				switch (term1)
				{
				case 1:
					p = obj.ahead;
					break;
				case 0:
					p = obj.lefty;
					break;
				case 2:
					p = obj.righty;
					break;
				}
				
				int l = obj.map.length;
				if (p.x >= 0 && p.x<l && p.y>=0 && p.y<l )
				{
					if (isCondition(obj.map[p.x][p.y]))
					{
						// Если условие совпадает, то проверяем, есть ли else.
						if (number_else == -1)
							sign = commands.size();
						else
							//Если условие выполняется и есть else, то он будет и концом if'a
							sign = number_else;
					}
					else
					{	
						// Если не совпадает, то переходим к else, или вообще выходим.
						if (number_else>-1)
						{
							sign = commands.size();
							current_number = number_else;
							current_command = commands.get(current_number);
						}
						else
							return true;
					}
				}
				else
					return true;
				
				// Проверяем есть ли условия, и устанавливаем начало
				/*
				if (condition)
				{
					sign = number_else;
				}
				else
				{
					sign = commands.size();
					current_number = number_else;
					current_command = commands.get(current_number);
				}
				*/
			}
			
			System.out.println(number_else);
			
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
