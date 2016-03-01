package Game;

import java.util.ArrayList;
import java.util.List;


public class World implements Runnable
{
	int width;
	int heigth;
	int step;
	
	volatile List<GameObject> objects = new ArrayList<>();
		
	int lenght_step;
	long time_sleep;
	
	boolean isGame = false;
	
	final byte[][] map;
	
	World(int w, int h, int step)
	{
		width = w;
		heigth = h;
		this.step = step;
		
		Map m = new Map();
		map = m.getMap();
		
		GameObject player = new GameObject(12, 1, step, map);
		
		MainLoop qe = new MainLoop();
		
		Queue wh = new WhileLoop();
		wh.add(new Forward());
		wh.add(new Forward());
		wh.add(new Left());
		wh.add(new Forward());
		wh.add(new Left());
		wh.add(new Forward());
		wh.add(new Left());
		wh.add(new Forward());
		qe.add(wh);
		
		player.setProgramm(qe);
		
		objects.add(player);
		
		lenght_step = 1;
		time_sleep = 10;
	}
	
	public List<GameObject> getObjects()
	{
		return objects;
	}
	
	public byte[][] getMap()
	{
		return map;
	}
	
	public void run()
	{
		isGame = true;
		mainLoop();
	}
	
	void stop()
	{
		isGame = false;
	}
	
	
	void mainLoop()
	{
		while (isGame)
		{
			
			for (GameObject q : objects)
			{
				//if (!q.isStep()) q.nextStep();
				
				q.step();

				//if (q.x < 0) q.x = 0;
		        //if (q.x > 800) q.x = 800;
		        //if (q.y < 0) q.y = 0;
		        //if (q.y > 600) q.y = 600;
			}
			
			try
			{
				Thread.sleep(time_sleep);
			}
			catch (Exception ex)
			{
				
			}
		}
	}
	

}
