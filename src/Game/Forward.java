package Game;

public class Forward extends Command 
{
	public void execute(GameObject obj)
	{
		
		switch (obj.direction)
		{
		case 0: // up. 
			if (obj.y != obj.map[obj.x].length-1 && obj.map[obj.x][obj.y+1] == 0) // Checking end map and wall or void sector.
			{
				obj.y++;
			}
			break;
		case 1: // rigth
			if ( obj.x != obj.map.length-1 && obj.map[obj.x+1][obj.y] == 0)
			{
				obj.x++;
			}
			break;
		case 2: //down
			if (obj.y != 0 && obj.map[obj.x][obj.y-1] == 0) // Checking end map and wall or void sector.
			{
				obj.y--;
			}
			break;
		case 3: // left
			if ( obj.x != 0 && obj.map[obj.x-1][obj.y] == 0)
			{
				obj.x--;
			}
			break;
		}
	}

}
