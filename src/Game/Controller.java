package Game;
import org.lwjgl.input.Keyboard;

public class Controller  
{
	private Window window;
	private World world;
	
	private Thread thread_world;
	
	private Map map;
	
	final int WIDTH = 1024;
	final int HEIGHT = 780;
	
	public void init()
	{
		int step = 10;
		world = new World(WIDTH, HEIGHT, step, this);
		
		map = new Map();
		byte[][] m = map.getMap();
		
		window = new Window(this);
		window.setMap(m);
		world.setMap(m);
		
		GameObject p =  world.getObjects().get(0);
		window.setPlayer(p);
		p =  world.getObjects().get(1);
		window.setShip(p);
		
		thread_world = new Thread(world);
		//thread_window = new Thread(window);
	}
	
	public void setProgramm(String text)
	{
		Compiller c = new Compiller();
		try
		{
			Queue prog = c.getProgramm(text);
			GameObject p = world.getPlayer();
			p.setProgramm( (MainLoop) prog);
			p.setGo();
			window.setMsg("-");
			//start();
		}
		catch (Exception ex)
		{
			window.setMsg("Error: "+ex.getMessage());
		}
	}
	
	public void setWin()
	{
		window.setMsg("Поздравляем! Вы достигли клада!");
	}
	
	public void pressedKey(int key, char c)
	{
		GameObject p = world.getObjects().get(0);
		switch (key)
        {
        case Keyboard.KEY_A: System.out.println("pressed A "); 
        	p.addCommand(new Left());
        	//window.x ++;
        	break;
        case Keyboard.KEY_D: System.out.println("pressed D"); 
    		p.addCommand(new Right());
    		//window.x --;
        	break;
        case Keyboard.KEY_W: System.out.println("pressed W"); 
    		p.addCommand(new Forward());
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
	
	public void loadMap(String name)
	{
		try
		{
			byte[][] m = map.loadMap(name);
			window.setMap(m);
			world.setMap(m);
		}
		catch (Exception ex)
		{
			window.setMsg(ex.getMessage());
		}
	}
	
	public void setMsg(String str)
	{
		window.setMsg(str);
	}
	
	public void stop()
	{
		world.getPlayer().stop();
	}
	
	public void start()
	{
		thread_world.start();
	}
	
	public void run()
	{
		window.start();
		thread_world.start();
	}
	
	
}
