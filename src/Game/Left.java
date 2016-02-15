package Game;

public class Left extends Command 
{
	
	public void execute(GameObject obj) 
	{
		obj.rotation -= 90;
		if (obj.rotation == -360) obj.rotation = 0;
		
		obj.direction = obj.direct.get(obj.rotation);
	}

}
