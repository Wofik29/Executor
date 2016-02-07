package Game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window 
{
	
	Window(int w, int h)
	{
		try
		{
			Display.setDisplayMode(new DisplayMode(w, h));
			Display.create();
		}
		catch (LWJGLException ex)
		{
			
		}
	}
	
	
}
