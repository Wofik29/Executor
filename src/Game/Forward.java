package Game;

public class Forward implements Command 
{
	public boolean execute(GameObject obj)
	{
		
		
		int x = obj.ahead.x;
		int y = obj.ahead.y;
		
		if (obj.map[x][y] == Map.SHALLOW || obj.map[x][y] == Map.DEEP)
		{
			obj.location.setLocation( obj.ahead);
		}
		
		
		/*
		int[] value =
				{
				obj.map[obj.x+1][obj.y],
				obj.map[obj.x][obj.y+1],
				obj.map[obj.x-1][obj.y],
				obj.map[obj.x][obj.y-1]
				};
		
		
		switch (obj.direction)
		{
		case 0: // down-left. 
			if (obj.y != obj.map[obj.x].length-1 && (value[0] == Map.SHALLOW || value[0] == Map.DEEP)) // Checking end map and wall or void sector.
			{
				obj.x++;
			}
			break;
		case 1: // down-rigth
			if ( obj.x != obj.map.length-1 && (value[1] == Map.SHALLOW || value[1] == Map.DEEP))
			{
				obj.y++;
			}
			break;
		case 2: // up-right
			if (obj.y != 0 && (value[2] == Map.SHALLOW || value[2] == Map.DEEP)) // Checking end map and wall or void sector.
			{
				obj.x--;
			}
			break;
		case 3: // up-left
			if ( obj.x != 0 && (value[3] == Map.SHALLOW || value[3] == Map.DEEP))
			{
				obj.y--;
			}
			break;
		}
		*/
		
		return true;
	}
	
	public String toString()
	{
		return "Forward Command";
	}

}
