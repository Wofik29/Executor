package Game;

import java.util.ArrayList;
import java.util.List;


public class World implements Runnable
{
	private volatile List<GameObject> objects = new ArrayList<>();
	private GameObject player;
	private long time_sleep;
	private Controller controller;
	private boolean isGame = false;
	public static volatile byte[][] map;
	
	World(int w, int h, int step, Controller c)
	{
		player = new GameObject(12, 1, step);
		controller = c;
		objects.add(player);
		time_sleep = 15;
	}
	
	public List<GameObject> getObjects()
	{
		return objects;
	}
	
	public void setMap(byte[][] m)
	{
		map = m.clone();
	}
	
	public GameObject getPlayer()
	{
		return player;
	}
	
	public void run()
	{
		isGame = true;
		mainLoop();
	}
	
	public void stop()
	{
		isGame = false;
	}
	
	
	private void mainLoop()
	{
		while (isGame)
		{
			
			for (GameObject q : objects)
			{
				try
				{
					q.step();
				}
				catch (Exception ex)
				{
					controller.setMsg(ex.getMessage());
					stop();
				}
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
