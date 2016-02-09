package Game;

import java.io.*; 
import java.util.ArrayList;
import java.util.List;

public class Main 
{

	Window win;
	Quad q;
	
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

		int[] i = {1,2,1,2,3,3,0,3,0,1};
		
		q.setProgramm(i);
		
		win = new Window(800,600, quads,q );
		Thread t = new Thread(win);
		
		t.start();
		
		
		if (t.isAlive()) win.setText("Gello");
		
		try 
		{
			t.join();
		}
		catch (Exception ex)
		{
			
		}
		
		System.out.println("End main");
	}
	
}
