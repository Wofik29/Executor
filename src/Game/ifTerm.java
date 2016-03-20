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
				/*
				 *  TODO делаем проверку, и решаем в какую ветку идти. Или вообще на выход.
				 *  Как то надо делать проверка, на то в какую сторону смотрит кораблик и какую сторону нам надо проверять.
				 */
				int x=-1;
				int y=-1;
				
				Point p = dir[obj.direction][term1];
				
				x = obj.x+p.x;
				y = obj.y+p.y;
				/*
				switch (obj.direction)
				{
				case 0: // down-left.
					switch (term1)
					{
					
					}
					break;
				case 1: // down-rigth
					
					break;
				case 2: // up-right
					
					break;
				case 3: // up-left
					
					break;
				}
				*/
				int l = obj.map.length;
				if (x >= 0 && x<l && y>=0 && y<=l )
				{
					if (obj.map[x][y] != term2) // Если не совпало, но идем в ветку else
					{
						if (number_else>-1)
						{
							current_number = number_else;
							number_else = commands.size();
						}	
						else
							isEnd = false;
					}
					else
					{
						number_else = number_else > -1 ? number_else : commands.size();
					}
				}
			}
			
			if (isEnd && current_command.execute(obj))
			{
				// TODO Тут возможно ошибка, когда последний элемент в списке, тогда один ход будет пропущен
				next();
				return false;
			}
		}
		
		return true;
	}
	
	private void next()
	{
		//StringTo();
		if (++current_number != number_else)
		{
			current_command = commands.get(current_number);
		}
		else 
		{
			current_number = 0;
			isEnd = false;
		}
	}
	
	public String toString()
	{
		String result = "";
		return result;
	}
}
