package Game;

public class Main 
{
	public static void main(String[] args) 
	{
		//System.setProperty("org.lwjgl.librarypath", new File("Executor_lib").getAbsolutePath());
		
		//System.out.println(
		//System.getProperty("java.class.path"));
		
		Controller c = new Controller();
		c.init();
		c.start();
	}
	
}
