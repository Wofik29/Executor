package Game;

public abstract class Command 
{
	protected GameObject obj;
	protected boolean end = false;
	
	Command(GameObject o)
	{
		obj = o;
	}
	
	abstract public void execute();
	
	public boolean isEnd()
	{
		return end;
	}
}
