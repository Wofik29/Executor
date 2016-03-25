package Game;

import java.awt.Point;

public class ifTerm extends ControlLoop 
{
	
	private boolean condition = true;
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
				int x=-1;
				int y=-1;
				
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
				
				x = p.x;
				y = p.y;
				
				int l = obj.map.length;
				if (x >= 0 && x<l && y>=0 && y<l )
				{
					 // Если условия не совпадают
					if (obj.map[x][y] != term2)
					{
						// если else есть
						if (number_else>-1)
							condition = false;
						else
							return true;
					}
					// Если условие выполняется, то else будет и концом if'a
					//else
						//number_else = commands.size();
				}
				else
					return true;
				
				// Проверяем есть ли условия, и устанавливаем начало
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
			}
			
			System.out.println(number_else);
			
			if (isEnd && current_command.execute(obj))
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
		}
		
		return true;
	}
	
	public String toString()
	{
		String result = "if:{ ";
		
		for (Command c :  commands)
		{
			result += c.toString()+", ";
		}
		
		result += " }";
		return result;
	}
}
