package Game;

import java.awt.Point;

public class WhileLoop extends ControlLoop
{
	public WhileLoop()
	{
		
	}
	
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
				if ((x < 0 && x>=l && y<0 && y>l) || (obj.map[x][y] != term2)  )
				{
					return true;
				}
			}
			
			if (isEnd)
			{
				if (current_command.execute(obj))
				{
					//System.out.println(current_number);
					current_number = ++current_number >= commands.size() ? 0 : current_number;
					//System.out.println(current_number);
				}
				current_command = commands.get(current_number);
				return false;
			}
		}
		return true;
	}
}
