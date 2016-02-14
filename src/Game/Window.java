package Game;

import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


/*
 * Сделать какой то синглтон-рисовальщик.
 * Будет массив объектов, которых надо рисовать.
 * и просто их пробегать и рисовать.
 * Так же ссыль на объект, которым можно двигать.
 */
public class Window implements Runnable
{
	
	long lastFPS;
	long lastFrame; // Время последнего
	int fps;
	
	World w;
	
	int width, height;
	
	int[][] map;
	
	float rotation = 0;
	float x,y;
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	
	int step;
	
	List<GameObject> quads;
	GameObject player;
	
	Window(int w, int h, List<GameObject>  q, GameObject p, int[][] m)
	{
		width = w;
		height = h;
		quads = q;
		player = p;
		step=1;
		
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
		int x = q.x_p + q.width/2;
		
		GL11.glColor3f(0.5f,0.5f,1.0f);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(q.x_p + q.width/2, q.y_p + q.width/2, 0);
		GL11.glRotatef(q.rotation, 0f, 0f, 1f);
		//GL11.glTranslatef(-q.x_p, -q.y_p, 0);
		
		//GL11.glRectf(q.x_p, q.y_p, q.x_p + q.width, q.y_p + q.height);
		
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
		
		int step = 20;
		
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
		    if (Keyboard.getEventKeyState()) {
		        switch (Keyboard.getEventKey())
		        {
		        case Keyboard.KEY_A: System.out.println("pressed A");
		        	player.rotation +=90;
		        	break;
		        case Keyboard.KEY_D: System.out.println("pressed D");  
		        	player.rotation -=90;
		        	break;
		        case Keyboard.KEY_W: System.out.println("pressed W");
		        	player.y ++;
		        	break;
		        case Keyboard.KEY_S: System.out.println("pressed S"); 
		        	player.y --;
		        	break;
		        }
		    }
		    else
		    	switch (Keyboard.getEventKey())
		        {
		        case Keyboard.KEY_A: System.out.println("relesed A"); left = false; break;
		        case Keyboard.KEY_D: System.out.println("relesed D"); right = false; break;
		        case Keyboard.KEY_W: System.out.println("relesed W"); up = false; break;
		        case Keyboard.KEY_S: System.out.println("relesed S"); down = false; break;
		        }
		}
	}
	
	
	public long getTime() 
	{
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	public void updateFPS() 
	{
	    if (getTime() - lastFPS > 1000) 
	    {
	       // Display.setTitle("FPS: " + fps); 
	        fps = 0; //reset the FPS counter
	        lastFPS += 1000; //add one second
	    }
	    fps++;
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
		
		// rotate quad
	   // rotation += 0.15f * delta;
	    
	    if (left) player.rotation ++; 
		if (right) player.rotation --;
	    /*if (up) player.y += 0.5*delta;
		if (down) player.y -= 0.5*delta;
	    
		//if (!player.isStep()) player.nextStep();
		//player.step( Math.round( step));
		
		
	    //updateFPS();
	     * 
	     */
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		
		drawMap();
		
		for (GameObject q : quads)
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
		//lastFPS = getTime();
		
		while (!Display.isCloseRequested())
		{
			inputLoop();
			
			int delta = getDelta();
            
			update(delta);
	        renderGL();
			
			Display.update();
			Display.sync(60); 
			//System.out.println(lastFPS);
		}
		
	}
	
}
