package Game;

import java.awt.Point;

public class WhileLoop extends ControlLoop
{
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
	
	@Override
	public boolean execute(GameObject obj)
	{
		/*
		System.out.println("WhileLoop : "+current_command.toString());
		System.out.println("WhileLoop : number == size - "+condition +" ==  " +obj.current_front + " -> "+(condition == obj.current_front));
		System.out.println(current_number + " != " + commands.size());
		System.out.println("Player -  x: "+obj.x+", y: "+obj.y);
		System.out.println();
		*/
		
		if (current_command != null)
		{
			/*
			 * Если мы в начале, то проверяем на условие
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
				if ((p.x < 0 && p.x>=l && p.y<0 && p.y>l) || (!isCondition(obj.map[p.x][p.y])) )
				{
					return true;
				}
			}
			
			if (current_command.execute(obj))
			{
				//System.out.println(current_number);
				current_number = ++current_number >= commands.size() ? 0 : current_number;
				//System.out.println(current_number);
			}
			
			current_command = commands.get(current_number);
			return false;
		}
		return true;
	}
}
