package Game;

public class WhileLoop extends Queue 
{
	int condition;
	
	class Condition	
	{
		int condtion;
		int direction;
		
		public Condition(int c, int d)
		{
			condtion = c;
			direction = d;
		}
		
		public boolean isCondition(GameObject obj)
		{
			switch (obj.direction)
			{
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			}
			
			return true;
		}
	}
	
	public WhileLoop() 
	{
		condition = 3;
	};
	
	public WhileLoop(int c)
	{
		condition = c;
	}
	
	@Override
	public String toString() 
	{
		return "WhileLoop";
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
		if (isEnd && current_command != null && current_command.execute(obj))
		{
			//System.out.println("execute "+current_command.toString()+" - "+current_number);
			obj.checkDirection();
			next(obj);
			return false;
		}
		else
		{
			//System.out.println("Main : return false");	
			return true;
		}
	}
	
	
	public void next(GameObject obj)
	{
		
		if (++current_number != commands.size())
		{
			current_command = commands.get(current_number);
		}
		else 
		{
			//System.out.println("WhileLoop : number == size - "+condition +" ==  " +obj.current_front + " -> "+(condition == obj.current_front));	
			//System.out.println(current_command.toString());
			if (condition == obj.current_front)	
			{
				isEnd = false;
			}
			else 
			{
				current_number = 0;
				current_command = commands.get(current_number);
			}
		}
		
	}
}
