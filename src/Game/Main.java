package Game;

import java.io.*; 
import java.util.ArrayList;
import java.util.List;

public class Main 
{

	Window win;
	World w;
	
	
	final int WIDTH = 800;
	final int HEIGHT = 600;
	
	final int[][] map = new int[10][10];
	
	volatile List<GameObject> quads = new ArrayList<>();
	
	public static void main(String[] args) 
	{
		Main m = new Main();
		m.start();
	}
	
	void start()
	{
		for (int i=0; i<map.length; i++)
		{
			for (int k=0; k<map[i].length; k++)
			{
				map[i][k] = (int) Math.round( Math.random());
				System.out.print(map[i][k]+" ");
			}
			System.out.println();
		}
		
		int[] p = {0};
		
		GameObject player = new GameObject(0, 0, p, map);
		
		quads.add(player);
		
		w = new World(WIDTH, HEIGHT, quads);
		win = new Window(WIDTH, HEIGHT, quads, player, map );
		Thread t = new Thread(win);
		Thread t1 = new Thread(w);
		
		t.start();
		t1.start();
		win.w = w;
		
		if (t.isAlive()) win.setText("Gello");
		
		try 
		{
			t.join();
		}
		catch (Exception ex)
		{
			
		}
		
		w.stop();
		
		System.out.println("End main");
	}
	
}
