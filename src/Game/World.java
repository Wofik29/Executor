package Game;

import java.util.List;


public class World implements Runnable
{
	int width;
	int heigth;
	
	List<GameObject> quads;
		
	int lenght_step;
	long time_sleep;
	
	boolean isGame = false;
	
	World(int w, int h, List<GameObject> q)
	{
		width = w;
		heigth = h;
		quads = q;
		lenght_step = 1;
		time_sleep = 10;
	}
	
	public void run()
	{
		isGame = true;
		start();
	}
	
	void stop()
	{
		isGame = false;
	}	
	
	void start()
	{
		while (isGame)
		{
			for (GameObject q : quads)
			{
				//if (!q.isStep()) q.nextStep();
				q.step();

				if (q.x < 0) q.x = 0;
		        if (q.x > 800) q.x = 800;
		        if (q.y < 0) q.y = 0;
		        if (q.y > 600) q.y = 600;
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
