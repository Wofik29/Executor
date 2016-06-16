package Game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import Game.Logger;

public class Logger 
{
	static Logger logger;
	File log;
	
	
	private Logger(String name)
	{
		log = new File(name);
		if (!log.exists())
		{
			try
			{
				log.createNewFile();
			}
			catch (Exception ex)
			{
				
			}
		}
	}
	
	public void print(String s)
	{
		 try
		 {
			 long date = System.currentTimeMillis();
			 PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(log, true)));
			 out.println(String.format("%tF %tT : ", date, date ) + s);
			 out.close();
		 }
		 catch (Exception ex)
		 {
			 System.out.println("not work write to log : " + ex.toString());
		 }

	}
	
	public static Logger getInstance()
	{
		if (logger == null) logger = new Logger("executor.log");
		return logger;
	}
	
	

}
