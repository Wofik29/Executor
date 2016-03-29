package Editor;

import javax.swing.SwingUtilities;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		
		Window win = new Window();
		
		try 
		{
			SwingUtilities.invokeAndWait(win);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
