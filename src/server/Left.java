package server;

public class Left implements Command 
{
	
	public boolean execute(Player obj) 
	{
		int r = obj.getRotation();
		r -= 90;
		obj.setRotation(r);
		return true;
	}
	
	public String toString()
	{
		return "Left Command";
	}

}
