package Game;

import java.util.ArrayList;
import java.util.List;


public class World implements Runnable
{
	private int width;
	private int heigth;
	private int step;
	
	private volatile List<GameObject> objects = new ArrayList<>();
	private GameObject player;
	
	private int lenght_step;
	private long time_sleep;
	
	boolean isGame = false;
	
	private byte[][] map;
	
	World(int w, int h, int step)
	{
		width = w;
		heigth = h;
		this.step = step;
		
		player = new GameObject(12, 1, step);
		
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
		
		//player.setProgramm(qe);
		
		objects.add(player);
		
		lenght_step = 1;
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
