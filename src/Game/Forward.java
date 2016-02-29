package Game;

public class Forward implements Command 
{
	public boolean execute(GameObject obj)
	{
		int[] value =
				{
				obj.map[obj.x][obj.y+1],
				obj.map[obj.x+1][obj.y],
				obj.map[obj.x][obj.y-1],
				obj.map[obj.x-1][obj.y]
				};
		switch (obj.direction)
		{
		case 0: // up. 
			if (obj.y != obj.map[obj.x].length-1 && (value[0] == Map.SHALLOW || value[0] == Map.DEEP)) // Checking end map and wall or void sector.
			{
				obj.y++;
			}
			break;
		case 1: // rigth
			if ( obj.x != obj.map.length-1 && (value[1] == Map.SHALLOW || value[1] == Map.DEEP))
			{
				obj.x++;
			}
			break;
		case 2: //down
			if (obj.y != 0 && (value[2] == Map.SHALLOW || value[2] == Map.DEEP)) // Checking end map and wall or void sector.
			{
				obj.y--;
			}
			break;
		case 3: // left
			if ( obj.x != 0 && (value[3] == Map.SHALLOW || value[3] == Map.DEEP))
			{
				obj.x--;
			}
			break;
		}
		
		return true;
	}
	
	public String toString()
	{
		return "Forward Command";
	}

}
