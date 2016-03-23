package Game;

import java.awt.Point;

public class ifTerm extends Queue 
{
	/* 
	 * Без разницы как написано в условии
	 * term1 - сторона кораблика
	 * term2 - вид клетки
	 * перед тем, как отправлять сюда, нужно разобрать.
	 */
	private int term1 = -1; 
	private int term2 = -1;
	
	
	private boolean condition = true;
	private int sign = -1 ;
	// Номер элемента, с которого начинется ветка else. 
	private int number_else = -1;
	
	public void setTerm1(int t1)
	{
		term1 = t1;
	}
	
	public void setTerm2(int t2)
	{
		term2 = t2;
	}
	
	public void setElse()
	{
		number_else = commands.size();
	}
	
	public boolean isAllTerm()
	{
		if (term1 == -1 || term2 == -1 )
			return false;
		return true;
	}
	
	@Override
	public boolean execute(GameObject obj)
	{
		
		
		if (current_command != null)
		{
			Point[][] dir = new Point[4][3];
			dir[0][0] = new Point(0,1);
			dir[0][1] = new Point(1,0);
			dir[0][2] = new Point(0,-1);
			dir[1][0] = new Point(-1,0);
			dir[1][1] = dir[0][0];
			dir[1][2] = dir[0][1];
			dir[2][0] = dir[0][2];
			dir[2][1] = dir[1][0];
			dir[2][2] = dir[0][0];
			dir[3][0] = dir[0][1];
			dir[3][1] = dir[0][2];
			dir[3][2] = dir[1][0];
					
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
				if (x >= 0 && x<l && y>=0 && y<=l )
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
