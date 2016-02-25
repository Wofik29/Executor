package Game;

public class Left implements Command 
{
	
	public boolean execute(GameObject obj) 
	{
		obj.rotation -= 90;
		obj.direction = obj.direct.get(obj.rotation);
		
		return true;
	}
	
	public String toString()
	{
		return "Left Command";
	}

}
