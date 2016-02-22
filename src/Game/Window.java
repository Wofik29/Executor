package Game;

import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window implements Runnable
{
	
	long lastFPS;
	long lastFrame; // Время последнего
	int fps;
	
	World w;
	
	int width, height;
	
	int[][] map;
	
	int step;
	
	volatile List<GameObject> objects;
	
	Controller controller;
	
	Window(int w, int h, int step, List<GameObject>  q, int[][] m, Controller c)
	{
		width = w;
		height = h;
		objects = q;
		this.step = step;
		controller = c;
		map = m;
		
	}
	
	private void initGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
		
	private void drawQuad(GameObject q)
	{
		GL11.glColor3f(0.5f,0.5f,1.0f);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(q.x_p + q.width/2, q.y_p + q.width/2, 0);
		GL11.glRotatef(q.current_rotation, 0f, 0f, 1f);
		
		GL11.glBegin(GL11.GL_TRIANGLES);
			GL11.glVertex2f(-q.width/2, -q.width/2);
			GL11.glVertex2f(q.width/2, -q.width/2);
			GL11.glVertex2f(0, q.width/2);
		GL11.glEnd();
		GL11.glPopMatrix(); 
	}
	
	private void drawMap()
	{
		GL11.glColor3f(1f,1f,1.0f);
		
		for (int i=0; i<map.length; i++)
		{
			for (int k=0; k<map[i].length; k++)
			{
				if (map[i][k] == 0)
				{
					GL11.glRectf(i*step, k*step, i*step+step, k*step+step);
				}
				else
				{
					
				}
			}
		}
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
	
	public void update(int delta) 
	{
		
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		
		drawMap();
		
		for (GameObject q : objects)
		{
			drawQuad(q);
		}
	}
	
	public void run()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			//Display.setTitle("Game");
			
			Keyboard.create();
		}
		catch (LWJGLException ex)
		{
			
		}
		start();
		
		Display.destroy();
		Keyboard.destroy();
	}
	
	public void start()
	{
		getDelta();
		initGL();
		
		while (!Display.isCloseRequested())
		{
			inputLoop();
			
			//int delta = getDelta();
            
	        renderGL();
			
			Display.update();
			Display.sync(60); 
			//System.out.println(lastFPS);
		}
		
	}
	
}
