package Game;

import java.util.ArrayList;
import java.util.List;


public class World implements Runnable
{
	private volatile List<GameObject> objects = new ArrayList<>();
	private GameObject player;
	private long time_sleep;
	
	private boolean isGame = false;
	
	private byte[][] map;
	
	World(int w, int h, int step)
	{
		player = new GameObject(12, 1, step);
		
		objects.add(player);
		time_sleep = 15;
	}
	
	public List<GameObject> getObjects()
	{
		return objects;
	}
	
	public void setMap(byte[][] m)
	{
		map = m;
		for (GameObject q : objects)
		{
			q.setMap(map);
		}
	}
	
	public byte[][] getMap()
	{
		return map;
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
				q.step();
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
