package Game;

import java.io.*; 

public class Main 
{

	Window win;
	Quad q;
	
	public static void main(String[] args) 
	{
		Main m = new Main();
		m.start();
	}
	
	void start()
	{
		
		
		/*Thread t = new Thread( new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				win = new Window(800,600);
				win.start();
			}
		});
		*/
		win = new Window(800,600);
		Thread t = new Thread(win);
		
		t.start();
		
		q = new Quad(100,100,50,50);
		
		//while (win == null) System.out.println("End main");;
		win.setText("Gello");
		
		System.out.println("End main");
	}
	
}
