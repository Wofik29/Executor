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
	
	long lastFPS; // Время последнего
	long lastFrame;
	int fps;
	
	int width, height;
	
	float rotation = 0;
	float x,y;
	boolean left = false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	
	int step;
	
	List<Quad> quads;
	Quad player;
	
	Window(int w, int h, List<Quad>  q, Quad p)
	{
		width = w;
		height = h;
		quads = q;
		player = p;
		step=1;
	}
	
	private void initGL()
	{
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
		
	private void drawQuad(Quad q)
	{
		GL11.glColor3f(0.5f,0.5f,1.0f);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(q.x, q.y, 0);
		//GL11.glRotatef(rotation, 0f, 0f, 1f);
		GL11.glTranslatef(-q.x, -q.y, 0);
		
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2f(q.x + q.width, q.y + q.height );
			GL11.glVertex2f(q.x + q.width, q.y-q.height);
			GL11.glVertex2f(q.x - q.width, q.y-q.height);
			GL11.glVertex2f(q.x - q.width, q.y + q.height);
		GL11.glEnd();
		
		GL11.glPopMatrix();
         
	}
	
	private void inputLoop()
	{
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        switch (Keyboard.getEventKey())
		        {
		        case Keyboard.KEY_A: System.out.println("pressed A"); player.nextStep(); left = true; break;
		        case Keyboard.KEY_D: System.out.println("pressed D"); right = true; break;
		        case Keyboard.KEY_W: System.out.println("pressed W"); 
		        	if (step<20)step++; 
		        	//up = true; 
		        	break;
		        case Keyboard.KEY_S: System.out.println("pressed S"); 
		        	if (step>0) step--; 
		        	//down = true; 
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
	    rotation += 0.15f * delta;
	    
	    //if (left) 
		if (right) player.x += 0.5*delta;
		if (up) player.y += 0.5*delta;
		if (down) player.y -= 0.5*delta;
	    
		if (!player.isStep()) player.nextStep();
		player.step( Math.round( step));
		
		if (player.x < 0) player.x = 0;
        if (player.x > 800) player.x = 800;
        if (player.y < 0) player.y = 0;
        if (player.y > 600) player.y = 600;
		
	    //updateFPS();
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		for (Quad q : quads)
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
