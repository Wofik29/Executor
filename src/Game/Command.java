package Game;

public abstract class Command 
{
	protected boolean end = false;
	
	abstract public void execute(GameObject obj);
	
	public boolean isEnd()
	{
		return end;
	}
}
