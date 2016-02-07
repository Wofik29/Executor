package Game;

import java.io.*; 

public class Main 
{

	public static void main(String[] args) 
	{
		System.out.println(new File("executor.log").getAbsolutePath());
		
		Logger log = Logger.getInstance();
		
		log.print("Hello World1");
		
	}
	
	
	
}
