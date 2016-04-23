package Game;


public class Main 
{
	static boolean isDebug = false;
	
	public static void main(String[] args) 
	{
		Controller c = new Game.Controller();
		c.init();
		c.run();
	}
	
}
