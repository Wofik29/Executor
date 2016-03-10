package Game;

public class Main 
{
	public static void main(String[] args) 
	{
		//System.setProperty("org.lwjgl.librarypath", new File("Executor_lib").getAbsolutePath());
		
		//System.out.println(
		//System.getProperty("java.class.path"));
		
		// TODO не всегда проходит поворот объекта. проблема в переходе 360 в 0.
		
		Controller c = new Controller();
		c.init();
		c.start();
	}
	
}
