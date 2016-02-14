package Game;

public class Right extends Command 
{
	
	public Right(GameObject o)
	{
		super(o);
	}
	
	public void execute()
	{
		obj.direction += 90;
		if (obj.direction == 360) obj.direction = 0;
	}

}
