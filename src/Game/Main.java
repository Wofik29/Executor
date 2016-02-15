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
	
	final int[][] map = new int[30][30];
	
	volatile List<GameObject> quads = new ArrayList<>();
	
	public static void main(String[] args) 
	{
		//System.setProperty("org.lwjgl.librarypath", new File("Executor_lib").getAbsolutePath());
		
		System.out.println(
		System.getProperty("java.class.path"));
		
		Main m = new Main();
		m.start();
	}
	
	void start()
	{
		for (int i=0; i<map[0].length; i++)
		{
			map[0][i] = 0;
			map[map.length-1][i] = 0;
		}
		
		for (int i=1; i<map.length-1; i++)
			for (int k=1; k<map[i].length-1; k++)
			{
				map[i][k] =  Math.round( (float) Math.random() );
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
		
		//if (t.isAlive()) win.setText("Gello");
		
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
