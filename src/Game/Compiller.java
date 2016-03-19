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
	
	public Queue getProgramm(String text)
	{
		//StringBuilder sb = new StringBuilder(text);
		/*
		*  0 - обычное
		*  1 - обработка условия для цикла
		*/
		int state = 0;
		boolean error = false;
		String error_text = "";
		
		Stack<Queue> stack = new Stack<>();
		Queue programm = new MainLoop();
		Queue current = programm;
				
		String[] lines = text.split("\t");
				
		parse: for (String line: lines)
		{
			String[] strs = line.split("\\s+");
			
			/* 
			 * Проверяем строку и на основании нее выбираем вид состояния
			 */
			for (String str : strs )
			{
				// TODO разбивать на буквы и по буквенно проверять. например ahead=lefty будет как одна строка, надо вычленить каждый term
						
				if (str.charAt(0) == '(')
					str = str.substring(1);
				if (str.charAt(str.length()-1) == ')')
					str = str.substring(0,str.length()-1);
				System.out.println(str);
			
				switch (state)
				{
				// Обычное, добавляем команды
				case 0:
					switch (str.toLowerCase())
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
					case "if":
						state = 1;
						stack.push(current);
						Queue temp = new ifTerm(); 
						current.add(temp);
						current = temp;
						temp = null;
						break;
					case "end":
						current = stack.pop();
						break;
					case "else":
						ifTerm if_temp = (ifTerm) current;
						if_temp.setElse();
						if_temp = null;
						break;
					default: 
						error = true;
						error_text = "Ожидался оператор, но встречен "+str;
						break parse;
					}
					break;
				case 1:
					ifTerm if_temp = (ifTerm) current;
					switch (str.toLowerCase())
					{
					case "ahead":
						if_temp.setTerm1(1);
						break;
					case "lefty":
						if_temp.setTerm1(0);
						break;
					case "righty":
						if_temp.setTerm1(2);
						break;
					case "water":
						if_temp.setTerm2(Map.SHALLOW);
						break;
					case "beach":
						if_temp.setTerm2(Map.BEACH);
						break;
					case "then": 
						if (if_temp.isAllTerm()) state = 0;
						else
						{
							error = true;
							error_text = "Условие не полное!";
							break parse;
						}
						// TODO Проверка, полностью ли написано условие.
								
						break;
					default:
						error = true;
						error_text = "Ожидалось условие, но встречен "+str;
						// TODO вывод ошибки "Ожидалось условие, но нашел str
						break parse;
					}
				case 2:
					switch (str.toLowerCase())
					{
						
					}
					break;
				case 3:
					break;
				}
			}
		}
				
		if (error)
		{
			System.out.println(error_text);
			// TODO скорее всего будет throw exeption, и написание ошибки.
		}
		
		return programm;
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
