package Game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class World implements Runnable
{
	private volatile List<GameObject> objects = new ArrayList<>();
	private GameObject player;
	private GameObject anotherShip;
	private long time_sleep;
	private Controller controller;
	private boolean isGame = false;
	public static volatile byte[][] map;
	
	World(int w, int h, int step, Controller c)
	{
		player = new GameObject(12, 1, step);
		controller = c;
		objects.add(player);
		anotherShip = new GameObject(14-1,18-2,step);
		objects.add(anotherShip);
		time_sleep = 15;
	}
	
	public List<GameObject> getObjects()
	{
		return objects;
	}
	
	public void setMap(byte[][] m)
	{
		map = new byte[m.length][m[0].length];
		Point jew = new Point(-1,-1);
		for (int i=0; i < m.length; i++)
		{
			for (int k=0; k < m[i].length; k++)
			{
				if (m[i][k] == 41)
				{
					jew.x = i;
					jew.y = k;
				}
				map[i][k] = m[i][k];
			}
		}
		map[jew.x+1][jew.y+1] = Map.JEWEL;
		map[jew.x-1][jew.y+1] = Map.JEWEL;
		map[jew.x+1][jew.y-1] = Map.JEWEL;
		map[jew.x-1][jew.y-1] = Map.JEWEL;
		int x = 1;
		int y = map[0].length-2;
		
		player.setLocation(new Point(x,y));
		player.setMap();
		anotherShip.setMap();
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
					Point p = q.ahead;
					if (map[p.x][p.y] == Map.JEWEL)
					{
						q.stop();
						controller.setWin();
					}
				}
				catch (Exception ex)
				{
					//ex.printStackTrace();
					controller.setMsg(ex.getMessage());
					q.stop();
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
