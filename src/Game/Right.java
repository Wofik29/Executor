package Game;

public class Right implements Command 
{
	public boolean execute(GameObject obj)
	{
		obj.rotation += 90;
		obj.checkRotation();
		obj.direction = obj.directs.get(obj.rotation);
		
		return true;
	}
	
	public String toString()
	{
		return "Right Command";
	}

}
