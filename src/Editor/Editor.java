package Editor;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class Editor 
{
	Window win;
	boolean isPlay = true;
	public Editor()
	{
		win = new Window(this);
	}
	
	public void start()
	{
		try 
		{
			SwingUtilities.invokeAndWait(win);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		if (win == null)
		{
			System.out.println("null win");
		}
		
		while (isPlay)
		{
			//win.draw();
		}
	}
	
	public void setStop()
	{
		isPlay = false;
	}
	
	
}
