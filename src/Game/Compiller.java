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
			StringBuilder str = new StringBuilder();
			char[] strs = line.toCharArray(); //line.split("\\s+");
			
			/* 
			 * Проверяем строку и на основании нее выбираем вид состояния
			 */
			for (char ch : strs )
			{
				// TODO разбивать на буквы и по буквенно проверять. например ahead=lefty будет как одна строка, надо вычленить каждый term
				str.append(ch);
				System.out.println(str);
				
				switch (state)
				{
				// Обычное, добавляем команды
				case 0:
					switch (str.toString())
					{
					case "forward":
						current.add(new Forward());
						str.setLength(0);
						break;
					case "left":
						current.add(new Left());
						str.setLength(0);
						break;
					case "right":
						current.add(new Right());
						str.setLength(0);
						break;
					case "if":
						state = 1;
						stack.push(current);
						Queue temp = new ifTerm(); 
						current.add(temp);
						current = temp;
						temp = null;
						str.setLength(0);
						break;
					case "end":
						current = stack.pop();
						str.setLength(0);
						break;
					case "else":
						ifTerm if_temp = (ifTerm) current;
						if_temp.setElse();
						if_temp = null;
						str.setLength(0);
						break;
					case "\t":
					case " ":
						str.setLength(0);
						break;
					}
					break;
				case 1:
					ifTerm if_temp = (ifTerm) current;
					switch (str.toString())
					{
					case "ahead":
						if_temp.setTerm1(1);
						str.setLength(0);
						break;
					case "lefty":
						if_temp.setTerm1(0);
						str.setLength(0);
						break;
					case "righty":
						if_temp.setTerm1(2);
						str.setLength(0);
						break;
					case "water":
						if_temp.setTerm2(Map.SHALLOW);
						str.setLength(0);
						break;
					case "beach":
						if_temp.setTerm2(Map.BEACH);
						str.setLength(0);
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
						str.setLength(0);
						break;
					case "=":
						
					case ")":
						
					case "(":
						
					case " ":
						str.setLength(0);
						break;
					default:
						
					}
				case 2:
					switch (str.toString())
					{
						
					}
					break;
				case 3:
					break;
				}
				
				if (str.length()>1 && str.charAt(str.length()-1) == ' ')
				{
					switch (state)
					{
					case 0: 
						error = true;
						error_text = "Ожидался оператор, но встречен "+str.toString();
						break parse;
					case 1:
						error = true;
						error_text = "Ожидалось условие, но встречен "+str;
						// TODO вывод ошибки "Ожидалось условие, но нашел str
						break parse;
					}
				}
			}
			
			str.setLength(0);
		}
		
		
		
		if (error)
		{
			System.out.println(error_text);
			// TODO скорее всего будет throw exeption, и написание ошибки.
		}
		
		System.out.println(programm.toString());
		
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
