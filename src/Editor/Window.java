package Editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window implements Runnable 
{
	Image image;
	Image select_terrain; 
	JFrame frame;
	JButton save;
	
	boolean isPressed = false;
	
	public final int width_canvas = 600;
	public final int height_canvas = 600;
	private final int size = 30;
	
	int x = 0;
	
	byte select;
	int cell = width_canvas/size;
	
	byte[][] map;
	byte[][] terrain;
	
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
	
	public Window()
	{
		select = SHALLOW;
		
		map = new byte[size][size];
		for (int i=0;i<size;i++)
			for (int j=0;j<size;j++)
			{
				map[i][j] = -1;
			}
		
		terrain = new byte[2][20];
		byte m = 0;
		for (int i=0; i<2; i++)
			for (int j=0; j<20; j++)
			{
				terrain[i][j] = m;
				m++;
				
			}
		
		frame = new JFrame("Editor");
		frame.setSize(800, 800);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		save = new JButton("Save");
		save.setBounds(10,620, 80, 20);
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveMap();
			}
		});
		frame.add(save);
		 
		/*
		frame.addMouseMotionListener(new MouseAdapter() 
			{	
				@Override
				public void mouseMoved(MouseEvent e) 
				{
					System.out.println("isPre");
					if (isPressed)
					{
						System.out.println("isPre");
						Point p = new Point( e.getX(), e.getY());
						
						p.x -= 10;
						p.y -=25;
						
						if (p.x>0 && p.x<width_canvas &&
								p.y>0 && p.y<height_canvas)
						{
							map[p.x/cell][p.y/cell] = select;
							
						}
						
						p.x -= 610;
						
						if (p.x > 0 && p.x<40 &&
								p.y>0 && p.y<400)
						{
							select = terrain[p.x/cell][p.y/cell];
						}
					}
				}
		});
		*/
		
		frame.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0) 
				{
					 isPressed = true;
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0)
				{
					Point p = new Point( arg0.getX(), arg0.getY());
					
					p.x -= 10;
					p.y -=25;
					
					if (p.x>0 && p.x<width_canvas &&
							p.y>0 && p.y<height_canvas)
					{
						map[p.x/cell][p.y/cell] = select;
						
					}
					
					p.x -= 610;
					
					if (p.x > 0 && p.x<40 &&
							p.y>0 && p.y<400)
					{
						select = terrain[p.x/cell][p.y/cell];
					}
				}
				
				
				@Override
				public void mouseReleased(MouseEvent arg0) 
				{
					isPressed = false;
				}
			});
		
		frame.setBackground(Color.white);
		
		
		
		System.out.println("end constructor");
	}
	
	public void run()
	{
		frame.setVisible(true);
		image = frame.createImage(width_canvas, height_canvas);
		select_terrain = frame.createImage(40, 400);
		
		Graphics g = select_terrain.getGraphics();
		
		
		int ter_cell = 20;
		int w = 40;
		int h = 400;
		int i = 0;
		while (i<w)
		{
			
			for (int j=0;j<h;j+=20)
			{
				g.setColor(Color.black);
				g.drawRect(i, j, cell, cell);
				
				drawTerrain(g, i, j, terrain[i/20][j/20], ter_cell); 
			}
			i+=20;
		}
		
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
			
			frame.getGraphics().drawImage(image, 10, 25, null);
			frame.getGraphics().drawImage(select_terrain, 620, 25, null);
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
		
		for (int i=0; i<width_canvas; i+=cell)
		{
			for (int j=0; j<height_canvas; j+=cell)
			{
				g.setColor(Color.black);
				g.drawRect(i, j, cell, cell);
				drawTerrain(g, i, j, map[i/cell][j/cell], cell);
			}
		}
	}
	
	private void saveMap()
	{
		int startPosX = 0;
		int startPosY = 0;
		/*for (int j=0;j<size;j++)
			for (int i=0;i<size;i++)
				if (map[i][j] > 0)
				{
					startPosX = i;
					startPosY = j;
				}
				*/
		File f = new File("Executor.map");
		
		try
		{
			f.createNewFile();
			
			//DataOutputStream out = new DataOutputStream(new BufferedOutputStream()));
			FileWriter out = new FileWriter(f);
			//PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f)));
			
			for (int i=startPosX; i<size; i++)
			{
				for (int j=startPosY; j<size; j++)
				{
					if (map[i][j]==-1)
					{
						out.write("0:");
					}
					else if (j==size-1)
					{
						out.write( Integer.toString(map[i][j]));
					}
					else
					{
						out.write( Integer.toString(map[i][j])+":" );
					}
				}
				out.write("\n");
			}
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		loadMap();
	}
	
	private void loadMap()
	{
		 try
		 {
			 BufferedReader in = new BufferedReader(new FileReader("Executor.map"));
			 
			 while (in.ready())
			 {
				 
				 System.out.println(in.read());
			 }
			 
			 in.close();
		 }
		 catch (Exception e)
		 {
			 e.printStackTrace();
		 }
		 
	}
	
	private void drawTerrain(Graphics g, int i, int j, byte type, int ter_cell)
	{
		// TODO оптимизировать
		switch (type)
		{
		case GRASS:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			break;
		case BEACH:
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			break;
		case DEEP:
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			break;
		case SHALLOW:
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			break;
		case GRASS_BEACH_IN_4:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case GRASS_BEACH_IN_3:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case GRASS_BEACH_IN_2:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case GRASS_BEACH_IN_1:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case GRASS_BEACH_OUT_4:
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(Color.green);
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case GRASS_BEACH_OUT_3:
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell);
			g.setColor(Color.green);
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case GRASS_BEACH_OUT_2:
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.green);
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case GRASS_BEACH_OUT_1:
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.green);
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case GRASS_BEACH_3:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+ter_cell/2, ter_cell, ter_cell/2);
			break;
		case GRASS_BEACH_2:
			g.setColor(Color.green);
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			break;
		case GRASS_BEACH_4:
			g.setColor(Color.green);
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			g.setColor(new Color(255,217,00));
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			break;
		case GRASS_BEACH_1 :
			g.setColor(Color.green);
			g.fillRect(i+1, j+ter_cell/2+1, ter_cell, ter_cell/2);
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			break;
		case BEACH_SHALLOW_IN_4 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case BEACH_SHALLOW_IN_3 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case BEACH_SHALLOW_IN_2 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case BEACH_SHALLOW_IN_1 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case BEACH_SHALLOW_OUT_4 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(255,217,00));
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case BEACH_SHALLOW_OUT_3 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(255,217,00));
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case BEACH_SHALLOW_OUT_2 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(255,217,00));
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case BEACH_SHALLOW_OUT_1 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(255,217,00));
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case SHALLOW_DEEP_IN_4 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.BLUE);
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case SHALLOW_DEEP_IN_3 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.BLUE);
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case SHALLOW_DEEP_IN_2 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.BLUE);
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case SHALLOW_DEEP_IN_1 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(Color.BLUE);
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case SHALLOW_DEEP_OUT_4 :
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i-ter_cell/2+1, j+ter_cell/2, ter_cell, ter_cell, 0, 90);
			break;
		case SHALLOW_DEEP_OUT_3 :
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i-ter_cell/2+1, j-ter_cell/2+1, ter_cell, ter_cell, 0, -90);
			break;
		case SHALLOW_DEEP_OUT_2 :
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i+ter_cell/2, j-ter_cell/2+1, ter_cell, ter_cell, 180, 90);
			break;
		case SHALLOW_DEEP_OUT_1 :
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell-1);
			g.setColor(new Color(0,192,255));
			g.fillArc(i+ter_cell/2, j+ter_cell/2, ter_cell, ter_cell, 180, -90);
			break;
		case SHALLOW_DEEP_3 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+ter_cell/2, ter_cell, ter_cell/2);
			break;
		case SHALLOW_DEEP_2 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			break;
		case SHALLOW_DEEP_4 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			g.setColor(Color.BLUE);
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			break;
		case SHALLOW_DEEP_1 :
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+ter_cell/2+1, ter_cell, ter_cell/2);
			g.setColor(Color.BLUE);
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			break;
		case BEACH_SHALLOW_3 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+ter_cell/2, ter_cell, ter_cell/2);
			break;
		case BEACH_SHALLOW_2 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			break;
		case BEACH_SHALLOW_4 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+1, ter_cell/2, ter_cell);
			g.setColor(new Color(0,192,255));
			g.fillRect(i+ter_cell/2, j+1, ter_cell/2, ter_cell);
			break;
		case BEACH_SHALLOW_1 :
			g.setColor(new Color(255,217,00));
			g.fillRect(i+1, j+ter_cell/2+1, ter_cell, ter_cell/2);
			g.setColor(new Color(0,192,255));
			g.fillRect(i+1, j+1, ter_cell, ter_cell/2);
			break;
		}
	}
	
	public boolean isVisible()
	{
		return frame.isVisible();
	}
}
