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
	
	public Compiller()
	{
		
	}
	
	public void setFile(String path)
	{
		this.path = path;
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
		
		System.out.println(sb);
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
			
			/*switch(str.toLowerCase())
			{
			case "forward":
				current.add(new Forward());
				break;
			case "влево":
				current.add(new Left());
				break;
			case "вправо":
				current.add(new Right());
				break;
			case "пока":
				Queue loop = new WhileLoop();
				current = loop;
				stack.push(current);
				break;
			case "конец":
				
				break;
			}
			*/
		}
		
		//return programm;
	}
}
