package Game;

import java.awt.Canvas;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.Timer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Window2 implements Runnable
{
	
	long lastFPS;
	long lastFrame; // Время последнего
	int fps;
	int width, height;
	
	float tex_width;
	float tex_height;
	
	float x_s = 0;
	float y_s = 0;
	float x_speed = 0;
	float y_speed = 0;
	
	GameObject player;	
	
	byte[][] map;
	TrueTypeFont font;
	int step;
	int rot = 0;
	volatile List<GameObject> objects;

	HashMap<Integer , int[]> coordTex = new HashMap<Integer, int[]>(50);
	
	
	Texture sprites;
	Texture ship;
	
	Controller controller;
	Timer timer;
	int trans = -50;
	float scal = 1.4f;
	int i_w =0 ;
	
	boolean isClose = false;
	
	//JFrame frame;
	//Canvas canvas;
	
	
	Window2(int w, int h, int step, Controller c)
	{
		width = w;
		height = h;
		tex_height = 1f/8f;
		tex_width = 1f/8f;
		this.step = step;
		controller = c;
		timer = new Timer(200, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				i_w = (i_w==0) ? (i_w=1) : (i_w=0);
			
			}
		});
		
	/*	setSize(w+10, h+10);
		canvas = new Canvas();
		canvas.setSize(w, h);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	*/
		
		coordTex.put(0, new int[]{5,0}); // grass
		coordTex.put(1, new int[]{0,0}); // deep
		coordTex.put(2, new int[]{1,4}); // beach
		coordTex.put(3, new int[]{2,3}); // shallow
		coordTex.put(4, new int[]{0,3}); // grass-beach-in-1
		coordTex.put(5, new int[]{4,3}); // grass-beach-in-2
		coordTex.put(6, new int[]{1,3}); // grass-beach-in-3
		coordTex.put(7, new int[]{5,2}); // grass-beach-in-4
		coordTex.put(8, new int[]{5,4}); // grass-beach-out-1
		coordTex.put(9, new int[]{5,3}); // grass-beach-out-2
		coordTex.put(10, new int[]{7,2}); // grass-beach-out-3
		coordTex.put(11, new int[]{3,4}); // grass-beach-out-4
		coordTex.put(12, new int[]{6,2}); // grass-beach-1
		coordTex.put(13, new int[]{6,3}); // grass-beach-2
		coordTex.put(14, new int[]{7,4}); // grass-beach-3
		coordTex.put(15, new int[]{3,3}); // grass-beach-4
		coordTex.put(16, new int[]{2,0}); // beach-shallow-in-1
		coordTex.put(17, new int[]{2,4}); // beach-shallow-in-2
		coordTex.put(18, new int[]{6,0}); // beach-shallow-in-3
		coordTex.put(19, new int[]{7,0}); // beach-shallow-in-4
		coordTex.put(20, new int[]{0,4}); // beach-shallow-out-1
		coordTex.put(21, new int[]{4,0}); // beach-shallow-out-2
		coordTex.put(22, new int[]{6,4}); // beach-shallow-out-3
		coordTex.put(23, new int[]{0,1}); // beach-shallow-out-4
		coordTex.put(24, new int[]{1,0}); // shallow-deep-in-1
		coordTex.put(25, new int[]{0,2}); // shallow-deep-in-2
		coordTex.put(26, new int[]{2,1}); // shallow-deep-in-3
		coordTex.put(27, new int[]{1,1}); // shallow-deep-in-4
		coordTex.put(28, new int[]{7,1}); // shallow-deep-out-1
		coordTex.put(29, new int[]{1,2}); // shallow-deep-out-2
		coordTex.put(30, new int[]{2,2}); // shallow-deep-out-3
		coordTex.put(31, new int[]{6,1}); // shallow-deep-out-4
		coordTex.put(32, new int[]{4,1}); // shallow-deep-1
		coordTex.put(33, new int[]{5,1}); // shallow-deep-2
		coordTex.put(34, new int[]{3,1}); // shallow-deep-3
		coordTex.put(35, new int[]{3,0}); // shallow-deep-4
		coordTex.put(36, new int[]{3,2}); // beach-shallow-1
		coordTex.put(37, new int[]{7,3}); // beach-shallow-2
		coordTex.put(38, new int[]{4,4}); // beach-shallow-3
		coordTex.put(39, new int[]{4,2}); // beach-shallow-4
		
	}
	
	public void setPlayer(GameObject p)
	{
		player = p;
	}
	
	public void setObjects(List<GameObject> o)
	{
		objects = o;
	}
	
	public void setMap(byte[][] m)
	{
		map = m;
	}
	private void initGL()
	{
		Font awtFont = new Font("Times new Roman", Font.BOLD, 25);
		font = new TrueTypeFont(awtFont, false);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		//GL11.glEnable(GL11.GL_DEPTH_TEST);
		//GL11.glDepthFunc(GL11.GL_LEQUAL);
		
		GL11.glShadeModel(GL11.GL_SMOOTH);
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glAlphaFunc(GL11.GL_GEQUAL, 0.8f);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
 
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glClearDepth(1);
        
        //GL11.glAlphaFunc(GL11.GL_GEQUAL, 1);
 
        GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
 
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		//GL11.glRotatef(-35, 0, 0, 1);
		

		try
		{
			
			sprites = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/Spritesheet.png"));
			ship = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/ship.png"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void drawShip(float x, float y)
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		float tex_width = 1f/4f;
		float tex_height = 1f/4f;
		float tx = 0;
		float ty = 1;

		GL11.glTranslatef(x,y, 0);
		//GL11.glScalef(scal,scal, 0);
		ship.bind();
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(tx*tex_width, ty*tex_height);
			GL11.glVertex2f(-32,-32);
			
			GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height);
			GL11.glVertex2f(32, -32);
			
			GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height+tex_height);
			GL11.glVertex2f(32, 32);
			
			GL11.glTexCoord2f(tx*tex_width, ty*tex_height+tex_height);
			GL11.glVertex2f(-32, 32);
		GL11.glEnd();
		GL11.glPopMatrix();
		sprites.bind();
	}
	
	private void drawMap()
	{
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(trans, 0, 0);
		GL11.glScalef(scal,scal, 0);
		sprites.bind();
		float Xo = 0;
	    float Yo = 0;
	    int height = 32;
	    int width = 64;
	    float C =(float) Math.floor(Display.getWidth()/ 2);
	    int [] t = {0,0};
	    float Xc = 0;
	   // System.out.println();
	   // System.out.println("=====");
		
		for (int y=0; y<map.length; y++)
		{
			// Здесь высчитывается, на какой высоте должна начинаться отрисовка 
            Yo = (height / 2) * y;

            // Про эту переменную я уже рассказал чуть ранее.
            Xc = C - (width / 2 * y);
			
			for (int x=0; x<map[y].length; x++)
			{
				Xo = Xc + (x * (width / 2));
				Yo += height / 2;
				
				t = coordTex.get((int)map[y][x]);
				//System.out.println(map[y][x]);
				//System.out.print(" "+t[0]+" , ");
				
				drawTexture(Xo, Yo, 0, t[0], t[1]);
				if (player != null)
					if (y==player.x && x==player.y)
					{
						font.drawString(0, 0, "X: "+x+" Y: "+y);
						drawShip(Xo,Yo);
					}
			}
			//System.out.println();
			
		}
		//drawShip();
		GL11.glPopMatrix(); 
	}
	
	private void inputLoop()
	{
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) 
		    {
		    	controller.pressedKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
		    }
		    else
		    {
		    	controller.relessedKey(Keyboard.getEventKey(), Keyboard.getEventCharacter());
		    }
		}
	}
	
	
	public long getTime() 
	{
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public void setText(String s)
	{
		Display.setTitle(s);
	}
	
	/*
	 * Считает, как много времени прошло с последнего кадра
	 */
	public int getDelta() 
	{
		long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
	    
	    return delta;
	}

	void drawTexture(float px, float py, float pz, float tx, float ty)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(px, py, pz);
		
		//GL11.glRotatef(rot, 0f, 0f, 1f);
		
		Color.white.bind();

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(tx*tex_width, ty*tex_height);
            GL11.glVertex2f(-32,-32);
            
            GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height);
            GL11.glVertex2f(32, -32);
            
            GL11.glTexCoord2f(tx*tex_width+tex_width, ty*tex_height+tex_height);
            GL11.glVertex2f(32, 32);
           
            GL11.glTexCoord2f(tx*tex_width, ty*tex_height+tex_height);
            GL11.glVertex2f(-32, 32);
        GL11.glEnd();
       
        GL11.glPopMatrix();
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 

		if (map != null) 
		{
			x_s += x_speed;
			y_s += y_speed;
			drawMap();
			//drawShip();
			//if (objects != null)
				//for (GameObject q : objects)
				//{
					//drawQuad(q);
				//}
		}
		else
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
			font.drawString(400, 400, "Loading", Color.red);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		 
	}
	
	public void run()
	{
		try
		{
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setVSyncEnabled(true);
			Display.create();
			
			Keyboard.create();
		}
		catch (LWJGLException ex)
		{
			
		}
		start();
		
		Display.destroy();
		Keyboard.destroy();
		//timer.stop();
	}
	
	public void start()
	{
		getDelta();
		initGL();
		//timer.start();
		
		
		for (int key: coordTex.keySet())
		{
			int t[] = coordTex.get(key);
			System.out.println(key+" : "+t[0]+" , "+t[1]);
		}
		
		while (!Display.isCloseRequested())
		{
			inputLoop();
			
			//int delta = getDelta();
            
	        renderGL();
			
			Display.update();
			Display.sync(60); 
			//System.out.println(lastFPS);
		}
		isClose = true;
		
	}
	
}