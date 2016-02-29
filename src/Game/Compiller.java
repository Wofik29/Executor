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
					append("\n");
			}
			
			br.close();
			fr.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		char[] s = sb.toString().toCharArray();
		StringBuilder com = new StringBuilder();
		boolean condition = false;
		for (char str : s)
		{
			com.append(str);
			if (condition)
			{
				switch(com.toString())
				{
				case " ": com.substring(0);
				break;
				case "ahead":
				}
			}
			else
			{
				switch(com.toString())
				{
				case "forward":
					System.out.println("GO!");
					com.substring(0);
					break;
				case "left":
					System.out.println("LEFT!");
					com.substring(0);
					break;
				case "right":
					System.out.println("RIGHT!");
					com.substring(0);
					break;
				case "while":
					System.out.println("while ");
					com.substring(0);
					break;
				case "{":
					System.out.println("starting block");
					com.substring(0);
					break;
				case "}":
					System.out.println("Endging block");
					com.substring(0);
					break;
				case ")":
					System.out.println("Ending condition");
					com.substring(0);
					break;
				case "(":
					System.out.println("Starting condition");
					condition = true;
					com.substring(0);
					break;
				case " ": 
					com.substring(0);
					break;
				}
			}
		}
		
		//		System.out.println(sb);
	}
	
	public void getProgramm()
	{
		Stack<Queue> stack = new Stack<>();
		
		Queue programm = new MainLoop();
		Queue current = programm;
		
		stack.push(current);
		
		String[] s = sb.toString().split(" ");
		int i = 0;
		
		for (String str : s)
		{
			System.out.println(str);
			
			switch(commands.get(str.toLowerCase()))
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
		
		//return programm;
	}
}
