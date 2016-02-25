package Game;
import org.lwjgl.input.Keyboard;

public class Controller  
{
	Window window;
	World world;
	
	Thread thread_world;
	Thread thread_window;
	
	final int WIDTH = 800;
	final int HEIGHT = 600;
	
	public void init()
	{
		int step = 10;
		world = new World(WIDTH, HEIGHT, step);
		window = new Window(WIDTH, HEIGHT, step, this);
		window.setMap(world.getMap());
		window.setObjects(world.getObjects());
		
		thread_world = new Thread(world);
		thread_window = new Thread(window);
	}
	
	public void pressedKey(int key, char c)
	{
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("pressed A "); 
        	GameObject p = world.getObjects().get(0);
        	p.isGo = true;
        	break;
        case Keyboard.KEY_D: System.out.println("pressed D"); break;
        case Keyboard.KEY_W: System.out.println("pressed W"); 
        	window.rot ++;
        	break;        
        case Keyboard.KEY_S: System.out.println("pressed S"); 
        	window.rot --;
        	break;
        }
	}

	public void relessedKey(int key, char c)
	{
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("relessed A"); break;
        case Keyboard.KEY_D: System.out.println("relessed D"); break;
        case Keyboard.KEY_W: System.out.println("relessed W"); break;
        case Keyboard.KEY_S: System.out.println("relessed S"); break;
        }
	}
	
	public void start()
	{
		thread_window.start();
		thread_world.start();
		try
		{
			thread_window.join();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		world.stop();
	}
	
	
}
