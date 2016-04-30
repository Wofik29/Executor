package Game;

public class Forward implements Command 
{
	public boolean execute(GameObject obj) throws Exception
	{
		int x = obj.ahead.x;
		int y = obj.ahead.y;
		
		if (World.map[x][y] == Map.SHALLOW || World.map[x][y] == Map.DEEP)
		{
			obj.setLocation( obj.ahead);
		}
		else if (World.map[x][y] == Map.SHIP)
		{
			throw new Exception("Корабль не может проплыть сквозь другой");
		}
		else
		{
			throw new Exception("Корабль не может плыть по суши");
		}
		
		return true;
	}
	
	public String toString()
	{
		return "Forward Command";
	}

}
