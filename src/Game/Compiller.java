package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Stack;

public class Compiller 
{
	private String path;
	private StringBuilder sb = new StringBuilder();
	private static HashMap<String, Integer> commands = new HashMap<String, Integer>(); 
	static {{
		commands.put("forward", 0);
		commands.put("left", 	1);
		commands.put("right", 	2);
		commands.put("ahead", 	3);
		commands.put("lefty", 	4);
		commands.put("righty", 	5);
		commands.put("water", 	6);
		commands.put("wall", 	7);
		commands.put("if", 		8);
		commands.put("while", 	9);
		commands.put("then", 	10);
		commands.put("do", 		11);
		commands.put("end", 	12);
		commands.put("=", 		13);
		commands.put("=!", 		14);
		commands.put("else", 	15);
	}};
	
	
	private static HashMap<String, Integer> print_commands = new HashMap<>();
	static {{
				print_commands.put("вперед", 0);
				print_commands.put("поворот влево", 1);
				print_commands.put("поворот вправо", 2);
				print_commands.put("спереди", 3);
				print_commands.put("слева", 4);
				print_commands.put("справа", 5);
				print_commands.put("вода", 5);
				print_commands.put("стена/берег", 6);
	}};
	
	public Compiller()
	{
		
	}
	
	public void setCommands(String path)
	{
		File f = new File(path);
		if (!f.exists())
		{
			System.out.println("not file");
		}
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(f));
			String lines[] = new String[10];
			int length=0;

			// Считываем все из файла 
			while (br.ready())
			{
				lines[length++] = br.readLine();
			}
			
			
			for (int i=0; i<length; i++)
			{
				// Обрабатываем каждую строку
				String str = lines[i];
				String[] line = str.split(":");
				line[0] = line[0].trim().toLowerCase();
				line[1] = line[1].trim().toLowerCase();
				
				// Удаляем текущую команду и ставим с новым именем.
				if (print_commands.containsKey(line[0]))
				{
					commands.put(line[1], print_commands.get(line[0]));	
				}
			}
			
			br.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
	}
	
	public String printCommands()
	{
		String result = "";
		
		for (String str : commands.keySet())
		{
			result += str + " - "+ commands.get(str) + "\n";
		}		
		
		return result;
	}
	
	public void setFile(String path)
	{
		this.path = path;
	}
	
	public static String getSyntax()
	{
		String str = "";
		
		str = "Команды: \n" +
			commands.get("forward") + " - 1 ход вперед\n" +
			commands.get("left") + " - повернуть влево\n" +
			commands.get("right") + " - повернуть вправо\n" +
			"Значения по сторонам от коробля:\n" +
			commands.get("ahead") + " - клетка спереди\n" + 
			commands.get("lefty") + " - клетка слева\n" +
			commands.get("right") + " - клетка справа\n" +
			"Значения клеток:\n" +
			commands.get("water") + " - Вода\n" +
			commands.get("wall") + " - Берег\n" +
			"Условны оператор if: \n" +
			"if (<condition>) then <operator> [else <operator>] end\n" +
			"Оператор цикла while:\n" +
			"while (<condition>) do <opearator> end\n"+
			"<condition> - должен включать в себя одну сторону и одно значение клетки\n"+
			"<operator> - может быть как команда, так и любой оператор цикла/условия";
		
		return str;
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
		System.out.println(commands.get("left"));
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
					if (str.length() > 1)
					{
						repeat = false;
						index = ++i;
					}
					else
					{
						str.setLength(0);
					}
					break;
				case '=':
					if (str.length() == 0)
					{
						str.append(chars[i]);
						if (chars[i-1] == '!')
						{
							str.append(chars[i-1]);
						}
						index = ++i;
					}
					else
						index = i;
					repeat = false;
					break;
				default:
					str.append(chars[i]);
				}
				i++;
			}
			
			String operation = str.toString();
			str.setLength(0);
			int current_key = commands.get(operation) == null ? -1 : commands.get(operation);
			System.out.println("str: '"+operation+"'"+", state: "+state+", key: "+current_key);
			
			Queue temp = null;
			switch (state)
			{
			// Обычное, добавляем команды
			case 0:
				switch (current_key)
				{
				case 0: // "forward
					current.add(new Forward());
					break;
				case 1: // "left"
					current.add(new Left());
					break;
				case 2: //"right":
					current.add(new Right());
					break;
				case 8: // "if":
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
					break;
				case 12: //"end":
					current = stack.pop();
					System.out.println("end");
					break;
				case 15: //"else":
					ifTerm if_temp = (ifTerm) current;
					if_temp.setElse();
					if_temp = null;
					System.out.println("else");
					break;
				case 9: //"while":
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
					break;
				default:
					System.out.println("Default");
					error = true;
					error_text = "Ожидался оператор, но встречен '"+operation+"'";
					isEnd = false;
					break parse;
				}
				break;
			// Проход по условию для if и while
			case 1:
				ControlLoop control = (ControlLoop) current;
				switch (current_key)
				{
				case 13: // "=":
					control.condition = 0;
					break;
				case 14: // "=!":
					control.condition = 1;
					break;
				case 3: // "ahead":
					control.setTerm1(1);
					break;
				case 4: // "lefty":
					control.setTerm1(0);
					break;
				case 5: // "righty":
					control.setTerm1(2);
					break;
				case 6: // "water":
					control.setTerm2(Map.SHALLOW);
					break;
				case 7: // "wall":
					control.setTerm2(40);
					break;
				case 11: // "do":
				case 10: // "then": 
					if (control.isAllTerm()) state = 0;
					else
					{
						error = true;
						error_text = "Условие не полное!";
						break parse;
					}
					break;
				default:
					error = true;
					error_text = "Ожидалось условие, но встречен "+operation;
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
