package Game;

import java.util.List;

public class World implements Runnable
{
	int width;
	int heigth;
	
	List<Quad> quads;	
	
	int lenght_step;
	
	boolean isGame = false;
	
	World(int w, int h, List<Quad> q)
	{
		width = w;
		heigth = h;
		quads = q;
		lenght_step = 1;
	}
	
	void stop()
	{
		isGame = false;
	}	
	
	public void run()
	{
		isGame = true;
		start();
	}
	
	void start()
	{
		while (isGame)
		{
			for (Quad q : quads)
			{
				if (!q.isStep()) q.nextStep();
				q.step(lenght_step);
			}
			try
			{
				Thread.sleep(10);
			}
			catch (Exception ex)
			{
				
			}
		}
	}
	
}
