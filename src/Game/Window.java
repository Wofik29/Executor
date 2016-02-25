package Game;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
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


public class Window implements Runnable
{
	
	long lastFPS;
	long lastFrame; // Время последнего
	int fps;
	int width, height;
	
	byte[][] map;
	//Font awtfont = new Font("Times New Roman", Font.BOLD, 25);
	TrueTypeFont font;
	int step;
	int rot = 0;
	volatile List<GameObject> objects;
	Texture grass;
	Texture beach;
	Texture grass_beach;
	Texture beach_water;
	Texture[] water = new Texture[2];
	Texture water_deep;
	Texture deep;
	
	
	Controller controller;
	Timer timer;
	int trans = 0;
	float scal = 1;
	int i_w =0 ;
	
	Window(int w, int h, int step, Controller c)
	{
		width = w;
		height = h;
		this.step = step;
		controller = c;
		timer = new Timer(200, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				i_w = (i_w==0) ? (i_w=1) : (i_w=0);
				System.out.println(water[i_w].getTextureID());
			}
		});
		
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
		GL11.glOrtho(0, 800, 600, 0, 100, -100);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		//GL11.glRotatef(-35, 0, 0, 1);
		

		try
		{
			grass = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/4.png"));
			beach = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/2.png"));
			grass_beach = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/0.png"));
			beach_water = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/3.png"));
			Texture tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/1.png"));
			water[0] = tex;
			System.out.println(tex.getTextureID());
			System.out.println(water[0].getTextureID());
			tex = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/8.png"));
			water[1] = tex;
			System.out.println(tex.getTextureID());
			System.out.println(water[1].getTextureID());
			water_deep = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/7.png"));
			deep = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/6.png"));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		/*
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 0, 600, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		*/
	}
		
	private void drawQuad(GameObject q)
	{
		//GL11.glColor3f(0.5f,0.5f,1.0f);
		
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
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glPushMatrix();
		GL11.glTranslatef(trans, 0, 0);
		GL11.glScalef(scal,scal, 0);
		Texture t = null;
		
		float Xo = 0;
	    float Yo = 0;
	    int height = 32;
	    int width = 64;
	    float C =(float) Math.floor(Display.getWidth()/ 2);
	    
	    float Xc = 0;
		
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
				switch (map[y][x])
				{
				case 0:	t = grass_beach; break;
				case 1: t = water[i_w]; break;
				case 2: t = beach; break;
				case 3: t = beach_water; break;
				case 4: t = grass; break;
				case 6: t = deep; break;
				case 7: t = water_deep; break;
				}
				
				
				
				drawTexture(Xo, Yo, 0, t);
				
			}
			
		}
		
		
		
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
	
	public void update(int delta) 
	{
		
	}
	
	void drawTexture(float x, float y, float z, Texture t)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(x*t.getHeight(), y*t.getWidth(), z);
		
		//GL11.glRotatef(rot, 0f, 0f, 1f);
		
		//Color.white.bind();
        t.bind(); // or GL11.glBind(texture.getTextureID());
        
        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(-32,-32);
            
            GL11.glTexCoord2f(1f,0);
            GL11.glVertex2f(32, -32);
            
            GL11.glTexCoord2f(1f,1f);
            GL11.glVertex2f(32, 32);
            
            GL11.glTexCoord2f(0,1f);
            GL11.glVertex2f(-32, 32);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_BINDING_2D);
        GL11.glPopMatrix();
	}
	
	void renderGL()
	{
		// Clear the screen and depth buffer 
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); 
		//drawTexture(34,18, grass);
		//drawTexture(0, 0, grass);
		
		//System.out.println(grass.getTextureHeight());
		//System.out.println(rot);
		
		if (map != null) 
		{
			drawMap();
			if (objects != null)
				for (GameObject q : objects)
				{
					drawQuad(q);
				}
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
		timer.stop();
	}
	
	public void start()
	{
		getDelta();
		initGL();
		timer.start();
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
