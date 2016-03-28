package Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class Window implements Runnable 
{

	Editor editor;
	Image image;
	JFrame frame;
	int x = 0;
	
	byte select;
	
	byte[][] map;
	
	public static final byte  GRASS = 0;
	public static final byte  DEEP = 1;
	public static final byte  BEACH = 2;
	public static final byte  SHALLOW = 3;
	public static final byte  GRASS_BEACH_IN_1 = 4;
	public static final byte  GRASS_BEACH_IN_2 = 5;
	public static final byte  GRASS_BEACH_IN_3 = 6;
	public static final byte  GRASS_BEACH_IN_4 = 7;
	public static final byte  GRASS_BEACH_OUT_1 = 8; 
	public static final byte  GRASS_BEACH_OUT_2 = 9;
	public static final byte  GRASS_BEACH_OUT_3 = 10;
	public static final byte  GRASS_BEACH_OUT_4 = 11;
	public static final byte  GRASS_BEACH_1 = 12;
	public static final byte  GRASS_BEACH_2 = 13;
	public static final byte  GRASS_BEACH_3 = 14;
	public static final byte  GRASS_BEACH_4 = 15;
	public static final byte  BEACH_SHALLOW_IN_1 = 16;
	public static final byte  BEACH_SHALLOW_IN_2 = 17;
	public static final byte  BEACH_SHALLOW_IN_3 = 18;
	public static final byte  BEACH_SHALLOW_IN_4 = 19;
	public static final byte  BEACH_SHALLOW_OUT_1 = 20;
	public static final byte  BEACH_SHALLOW_OUT_2 = 21;
	public static final byte  BEACH_SHALLOW_OUT_3 = 22;
	public static final byte  BEACH_SHALLOW_OUT_4 = 23;
	public static final byte  SHALLOW_DEEP_IN_1 = 24;
	public static final byte  SHALLOW_DEEP_IN_2 = 25;
	public static final byte  SHALLOW_DEEP_IN_3 = 26;
	public static final byte  SHALLOW_DEEP_IN_4 = 27;
	public static final byte  SHALLOW_DEEP_OUT_1 = 28;
	public static final byte  SHALLOW_DEEP_OUT_2 = 29;
	public static final byte  SHALLOW_DEEP_OUT_3 = 30;
	public static final byte  SHALLOW_DEEP_OUT_4 = 31;
	public static final byte  SHALLOW_DEEP_1 = 32;
	public static final byte  SHALLOW_DEEP_2 = 33;
	public static final byte  SHALLOW_DEEP_3 = 34;
	public static final byte  SHALLOW_DEEP_4 = 35;
	public static final byte  BEACH_SHALLOW_1 = 36;
	public static final byte  BEACH_SHALLOW_2 = 37;
	public static final byte  BEACH_SHALLOW_3 = 38;
	public static final byte  BEACH_SHALLOW_4 = 39;
	
	public Window(Editor ed)
	{
		editor = ed;
		
		select = BEACH;
		
		map = new byte[50][50];
		
		for (int i=0;i<50;i++)
			for (int j=0;j<50;j++)
			{
				map[i][j] = -1;
			}
		
		frame = new JFrame("Editor");
		frame.setSize(800, 800);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
			@Override
			public void windowIconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			
			@Override
			public void windowClosing(WindowEvent arg0)	{
				System.out.println("Closed");
				editor.setStop();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {}
			
			@Override
			public void windowActivated(WindowEvent arg0) {}
		});
		frame.setBackground(Color.white);
		System.out.println("end constructor");
	}
	
	public void run()
	{
		frame.setVisible(true);
		image = frame.createImage(600, 600);
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				draw();
			}
		});
		
		t.start();
		//draw();
	}
	
	public void draw()
	{
		Graphics g = image.getGraphics();
		
		while (frame.isVisible())
		{
			g.clearRect(0, 0, 600, 600);
			
			drawGrid(g);
			
			g.setColor(Color.green);
			g.drawRect(x, 50, 10, 10);
			
			frame.getGraphics().drawImage(image, 20, 40, null);
			if  (x<600-10)
				x++;
			try 
			{
				Thread.sleep(20);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void drawGrid(Graphics g)
	{
		int cell = 20;
		
		int width = 600;//g.getClipBounds().width;
		int height = 600;//g.getClipBounds().height;
		
		g.setColor(Color.black);
		
		for (int i=0; i<width; i+=cell)
		{
			g.drawLine(i, 0, i, height);
		}
		for (int j=0; j<height; j+=cell)
		{
			g.drawLine(0, j, height, j);
		}
	}
	
	public boolean isVisible()
	{
		return frame.isVisible();
	}
}
