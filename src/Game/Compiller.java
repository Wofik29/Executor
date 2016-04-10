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
		commands.put("forward", "forward");
		commands.put("left", "left");
		commands.put("right", "right");
		commands.put("ahead", "ahead");
		commands.put("lefty", "lefty");
		commands.put("righty", "righty");
		commands.put("water", "water");
		commands.put("wall", "wall");
	}
	
	
	public void setFile(String path)
	{
		this.path = path;
	}
	
	
	// Чтение списка команд из файла
	public void setCommands()
	{
		
	}
	
	// Чтение из файла
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
				sb.append(br.readLine());
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
	
	public Queue getProgramm(String text) throws Exception
	{
		//StringBuilder sb = new StringBuilder(text);
		/*
		*  0 - обычное
		*  1 - обработка условия для цикла
		*/
		int state = 0;
				
		// Флаг, когда закончим парсить текст
		boolean isEnd = true;
		
		// Вывод ошибки и текст ошибки
		boolean error = false;
		String error_text = "";
		
		// Стэк вложенности, н-р if { if ... } 
		Stack<Queue> stack = new Stack<>();
		
		// программа, которую вовзращаем, и указатель на текущий цикл.
		Queue programm = new MainLoop();
		Queue current = programm;
		
		// Массив программы в виде символов
		char[] chars = text.toCharArray(); 
		
		
		// строка, для единичного операта и etc
		// индекс, где находимся в тексте
		StringBuilder str = new StringBuilder();
		int index=0;
		
		parse: while (isEnd)
		{
			System.out.println("parse while");
			
			// повторять, пока не наткнемся на разделитель
			boolean repeat = true;
			int i = index;
			
			while (repeat)
			{
				// пока не конец текста
				if (i>chars.length-1)
				{
					isEnd = false;
					break;
				}
				
				switch (chars[i])
				{
				/* 
				 * Если встретили один из этих символов, значит мы запомнили один оператор.
				 * Следовательно запоминаем это место и выходим обрабатывать.
				 */
				case ')': case '(': case ' ': case ';': case '\t': case '\n': case '!': 
					repeat = false;
					index = ++i;
					break;
				case '=':
					if (chars[i-1] == '!')
					{
						str.append(chars[i-1]);
					}
					repeat = false;
					index = ++i;
					
					break;
				default:
					str.append(chars[i]);
				}
				i++;
			}
			
			System.out.println("str: '"+str+"'"+", state: "+state);
			
			
			Queue temp = null;
			switch (state)
			{
			// Обычное, добавляем команды
			case 0:
				switch (str.toString())
				{
				case "forward":
					current.add(new Forward());
					str.setLength(0);
					System.out.println("Forward");
					break;
				case "left":
					current.add(new Left());
					str.setLength(0);
					System.out.println("left");
					break;
				case "right":
					current.add(new Right());
					str.setLength(0);
					System.out.println("right");
					break;
				case "if":
					System.out.println("if");
					state = 1;
					
					// Запомнили родительский узел
					stack.push(current);
					
					// Создали if
					temp = new ifTerm();
					
					// Добавили if в родительский узел
					current.add(temp);
					
					// установили текущую очередь if
					current = temp;
					temp = null;
					str.setLength(0);
					break;
				case "end":
					current = stack.pop();
					str.setLength(0);
					System.out.println("end");
					break;
				case "else":
					ifTerm if_temp = (ifTerm) current;
					if_temp.setElse();
					if_temp = null;
					str.setLength(0);
					System.out.println("else");
					break;
				case "while":
					System.out.println("while");
					state = 1;
					// Запомнили родительский узел
					stack.push(current);
					
					// Создали while
					temp = new WhileLoop();
					
					// Добавили if в родительский узел
					current.add(temp);
					
					// установили текущую очередь if
					current = temp;
					temp = null;
					str.setLength(0);
					break;
				default:
					System.out.println("Default");
					error = true;
					error_text = "Ожидался оператор, но встречен "+str.toString();
					isEnd = false;
					break parse;
				}
				break;
			// Проход по условию для if и while
			case 1:
				ControlLoop control = (ControlLoop) current;
				switch (str.toString())
				{
				case "=":
					control.condition = 0;
					break;
				case "=!":
					control.condition = 1;
					break;
				case "ahead":
					control.setTerm1(1);
					str.setLength(0);
					break;
				case "lefty":
					control.setTerm1(0);
					str.setLength(0);
					break;
				case "righty":
					control.setTerm1(2);
					str.setLength(0);
					break;
				case "water":
					control.setTerm2(Map.SHALLOW);
					str.setLength(0);
					break;
				case "beach":
					control.setTerm2(Map.BEACH);
					str.setLength(0);
					break;
				case "do":
				case "then": 
					if (control.isAllTerm()) state = 0;
					else
					{
						error = true;
						error_text = "Условие не полное!";
						break parse;
					}
					// TODO Проверка, полностью ли написано условие.
					str.setLength(0);
					break;
				default:
					error = true;
					error_text = "Ожидалось условие, но встречен "+str;
					// TODO вывод ошибки "Ожидалось условие, но нашел str
					break parse;
				}
			case 2:
				switch (str.toString())
				{
					
				}
				break;
			case 3:
				break;
			}
		}
		
		if (error)
		{
			System.out.println(error_text);
			throw new Exception(error_text);
			//return null;
		}
		
		if (!stack.isEmpty())
		{
			throw new Exception("Не закрыт цикл!");
		}

		System.out.println(programm.toString());
		
		return programm;
		
	}
}
