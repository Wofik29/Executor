package Game;

interface Command 
{
	abstract public boolean execute(GameObject obj) throws Exception;
	abstract public String toString();
}
