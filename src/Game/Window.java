package Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window implements Runnable
{
	
	long lastFPS;
	long lastFrame;
	int fps;
	
	int width, height;
	
	float rotation = 0;
	float x,y;
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	
	Window(int w, int h)
	{
		width = w;
		height = h;
	}
	
	private void initGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void drawQuad(int x, int y)
	{
		 
		GL11.glColor3f(0.5f,0.5f,1.0f);
		
		 GL11.glPushMatrix();
         GL11.glTranslatef(x, y, 0);
         GL11.glRotatef(rotation, 0f, 0f, 1f);
         GL11.glTranslatef(-x, -y, 0);
          
         GL11.glBegin(GL11.GL_TRIANGLE_FAN);
             GL11.glVertex2f(x + 25, y + 25);
             GL11.glVertex2f(x + 25, y - 25);
             GL11.glVertex2f(x-25 , y-25);
             //GL11.glVertex2f(x - 50, y + 50);
         GL11.glEnd();
         
         GL11.glColor3f(1f,0f,0f);
         
         GL11.glBegin(GL11.GL_POINT);
         	GL11.glVertex2f(x, y);
         GL11.glEnd();
         
         GL11.glPopMatrix();
         
         
	}
	
	private void inputLoop()
	{
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        switch (Keyboard.getEventKey())
		        {
		        case Keyboard.KEY_A: System.out.println("pressed A"); left = true; break;
		        case Keyboard.KEY_D: System.out.println("pressed D"); right = true; break;
		        case Keyboard.KEY_W: System.out.println("pressed W"); up = true; break;
		        case Keyboard.KEY_S: System.out.println("pressed S"); down = true; break;
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
	    rotation += 0.15f * delta;
	    
	    if (left) x -= 0.5*delta;
		if (right) x += 0.5*delta;
		if (up) y += 0.5*delta;
		if (down) y -= 0.5*delta;
	    
		
		if (x < 0) x = 0;
        if (x > 800) x = 800;
        if (y < 0) y = 0;
        if (y > 600) y = 600;
		
	    updateFPS();
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		drawQuad((int)x, (int)y);
	}
	
	public void run()
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
			//Display.setTitle("Game");
			
			Keyboard.create();
			
			x = 0;
			y = 200;
			
		}
		catch (LWJGLException ex)
		{
			
		}
		start();
	}
	
	public void start()
	{
		getDelta();
		initGL();
		lastFPS = getTime();
		
		while (!Display.isCloseRequested())
		{
			inputLoop();
			
			
			int delta = getDelta();
            
			update(delta);
	        renderGL();
			
			Display.update();
			Display.sync(60); 
		}
		
		Display.destroy();
	}
	
}
