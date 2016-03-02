package Game;
import java.awt.EventQueue;

import org.lwjgl.input.Keyboard;

public class Controller  
{
	Window window;
	World world;
	
	Thread thread_world;
	Thread thread_window;
	
	final int WIDTH = 1024;
	final int HEIGHT = 780;
	
	public void init()
	{
		int step = 10;
		world = new World(WIDTH, HEIGHT, step);
		window = new Window(this);
		//window.setMap(world.getMap());
		//window.setObjects(world.getObjects());
		
		GameObject p =  world.getObjects().get(0);
		//window.setPlayer(p);
		
		thread_world = new Thread(world);
		//thread_window = new Thread(window);
	}
	
	public void pressedKey(int key, char c)
	{
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("pressed A "); 
        	GameObject p = world.getObjects().get(0);
        	p.isGo = true;
        	//window.x ++;
        	break;
        case Keyboard.KEY_D: System.out.println("pressed D"); 
        	//window.x --;
        	break;
        case Keyboard.KEY_W: System.out.println("pressed W"); 
        	//window.y ++;
        	break;        
        case Keyboard.KEY_S: System.out.println("pressed S"); 
        	//window.y --;
        	break;
        }
	}

	public void relessedKey(int key, char c)
	{
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("relessed A"); 
        	//window.x_speed = 0;
        	break;
        case Keyboard.KEY_D: System.out.println("relessed D"); 
        	//window.x_speed = 0;
        	break;
        case Keyboard.KEY_W: System.out.println("relessed W"); 
        	//window.y_speed = 0;
        	break;
        case Keyboard.KEY_S: System.out.println("relessed S"); 
        	//window.y_speed = 0;
        	break;	
        }
	}
	
	public void stop()
	{
		world.stop();
	}
	
	public void start()
	{
		window.start();
		//thread_window.start();
		//EventQueue.invokeLater(window);
		//thread_world.start();
		try
		{
			//thread_window.join();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		//world.stop();
	}
	
	
}
