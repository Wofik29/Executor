package Game;

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

	// Номер элемента, с которого начинется ветка else. 
	private int number_else;
	
	private boolean entry = false;
	
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
			// Если мы в начале, то надо сделать проверку условия. 
			if (current_number == 0)
			{
				/*
				 *  TODO делаем проверку, и решаем в какую ветку идти. Или вообще на выход.
				 *  Как то надо делать проверка, на то в какую сторону смотрит кораблик и какую сторону нам надо проверять.
				 */
				int x=-1;
				int y=-1;
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
				
				int l = obj.map.length;
				if (x >= 0 && x<l && y>=0 && y<=l )
				{
					
				}
			}
		}
		
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			//System.out.println("execute "+current_command.toString()+" - "+current_number);
			
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
