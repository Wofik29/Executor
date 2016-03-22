package Game;

interface Command 
{
	abstract public boolean execute(GameObject obj);
	abstract public String toString();
}
