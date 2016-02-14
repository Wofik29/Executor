package Game;

public class Left extends Command 
{
	public Left(GameObject o)
	{
		super(o);
	}
	
	public void execute() 
	{
		obj.direction -= 90;
		if (obj.direction == -360) obj.direction = 0;
	}

}
