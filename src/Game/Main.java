package Game;

import java.io.*; 
import java.util.ArrayList;
import java.util.List;

public class Main 
{

	Window win;
	World w;
	Quad q;
	
	final int WIDTH = 800;
	final int HEIGHT = 600;
	
	List<Quad> quads = new ArrayList<>();
	
	public static void main(String[] args) 
	{
		Main m = new Main();
		m.start();
	}
	
	void start()
	{
		q = new Quad(300,100,50,50);
		quads.add(q);

		int[] i = {0,1,2,3};
		
		q.setProgramm(i);
		
		
		w = new World(WIDTH, HEIGHT, quads);
		win = new Window(WIDTH, HEIGHT, quads,q );
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
