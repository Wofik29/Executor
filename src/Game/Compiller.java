package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Stack;

public class Compiller 
{
	String path;
	StringBuilder sb = new StringBuilder();
	HashMap<String, String> commands = new HashMap<>();
	
	public Compiller()
	{
		
	}
	
	public void setFile(String path)
	{
		this.path = path;
	}
	
	public void setCommands()
	{
		
	}
	
	public void read()
	{
		if (path == null) return;
		File f = new File(path);
		FileReader fr;
		BufferedReader br;
		try
		{
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			while (br.ready() )
			{
				sb.append(br.readLine()).
					append(" ");
			}
			
			br.close();
			fr.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		//System.out.println(sb);
	}
	
	public void getProgramm2()
	{
		char[] s = sb.toString().toCharArray();
		StringBuilder com = new StringBuilder();
		boolean condition = false;
		for (char str : s)
		{
			//com.delete(0, com.length());
			com.append(str);
			//System.out.println(com);
			if (condition)
			{
				switch(com.toString())
				{
				case " ": com.delete(0, com.length());
				break;
				case "ahead":
					
					com.delete(0, com.length());
					break;
				}
			}
			else
			{
				switch(com.toString())
				{
				case "forward":
					System.out.println("GO!");
					com.delete(0, com.length());
					break;
				case "left":
					System.out.println("LEFT!");
					com.delete(0, com.length());
					break;
				case "right":
					System.out.println("RIGHT!");
					com.delete(0, com.length());
					break;
				case "while":
					System.out.println("while ");
					com.delete(0, com.length());
					break;
				case "{":
					System.out.println("starting block");
					com.delete(0, com.length());
					break;
				case "}":
					System.out.println("Endging block");
					com.delete(0, com.length());
					break;
				case ")":
					System.out.println("Ending condition");
					com.delete(0, com.length());
					break;
				case "(":
					System.out.println("Starting condition");
					condition = true;
					com.delete(0, com.length());
					break;
				case " ": 
					com.delete(0, com.length());
					break;
				}
			}
		}
	}
	
	public Queue getProgramm1()
	{
		Stack<Queue> stack = new Stack<>();
		
		Queue programm = new MainLoop();
		Queue current = programm;
		
		stack.push(current);
		
		String[] s = sb.toString().split(" ");
		
		for (String str : s)
		{
			//System.out.println(str);
			
			switch((str.toLowerCase()))
			{
			case "forward":
				current.add(new Forward());
				break;
			case "left":
				current.add(new Left());
				break;
			case "right":
				current.add(new Right());
				break;
			case "while":
				Queue loop = new WhileLoop();
				current.add(current);
				stack.push(current);
				current = loop;
				break;
			case "end":
				if (!stack.empty())	current = stack.pop();
				// return 
				break;
			}
			
		}
		
		return programm;
	}
}
