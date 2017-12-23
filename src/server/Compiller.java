package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Compiller 
{
	public static void main(String[] args) {
		byte[][] b = {{0,51},{0,0}};
		
		Compiller c = new Compiller();
		Queue loop = new MainLoop();
		try {
			loop = c.getProgramm(b);
		} catch (Exception ex) {ex.printStackTrace(); }
		System.out.println(loop.toString());
	}
	
	private String path;
	private StringBuilder sb = new StringBuilder();
	private final byte offset = 50; // Смещение для процедур
	private byte[][] procedures = null;
	
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
		commands.put("jewel", 	16);
		commands.put("ship", 	17);
	}};
	
	
	

	
	public static String getSyntax() {
		String str = "";
		str = "Команды: \n" +
			"forward" + " - 1 ход вперед\n" +
			"left" + " - повернуть влево\n" +
			"right" + " - повернуть вправо\n" +
			"Значения по сторонам от коробля:\n" +
			"ahead" + " - клетка спереди\n" + 
			"lefty" + " - клетка слева\n" +
			"right" + " - клетка справа\n" +
			"Значения клеток:\n" +
			"water" + " - Вода\n" +
			"wall" + " - Берег\n" +
			"Условны оператор if: \n" +
			"if (<condition>) then <operator> [else <operator>] end\n" +
			"Оператор цикла while:\n" +
			"while (<condition>) do <opearator> end\n"+
			"<condition> - должен включать в себя одну сторону и одно значение клетки\n"+
			"<operator> - может быть как команда, так и любой оператор цикла/условия";
		
		return str;
	}
	
	// Чтение из файла
	public void read() {
		if (path == null) return;
		
		File f = new File(path);
		FileReader fr;
		BufferedReader br;
		
		sb.setLength(0);
		try	{
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			
			while (br.ready() )	{
				sb.append(br.readLine());
			}
			br.close();
			fr.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/*
	 * Создает MainLoop из последовательности цифр.
	 */
	private Queue parseAlgorithm(byte[] algorithm) throws Exception {
		
		Queue programm = new MainLoop();
		Queue current = programm;
		int state = 0;
		// Стэк вложенности, н-р if { if ... } 
		Stack<Queue> stack = new Stack<Queue>();
		
		for (int i=0; i< algorithm.length; i++) {
			int operator = algorithm[i];
			Queue temp = null;
			switch (state) {
			// Обычное, добавляем команды
			case 0:
				switch (operator) {
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
					break;
				case 15: //"else":
					ifTerm if_temp = (ifTerm) current;
					if_temp.setElse();
					if_temp = null;
					break;
				case 9: //"while":
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
					if (procedures.length-1 < operator-offset)
						throw new Exception("Ожидался оператор, но встречен '"+operator+"'");
					else current.add( parseAlgorithm(procedures[operator-offset]));
				}
				break;
			// Проход по условию для if и while
			case 1:
				ControlLoop control = (ControlLoop) current;
				switch (operator) {
				case 13: // "=":
					control.setCondition(0);
					break;
				case 14: // "=!":
					control.setCondition(1);
					break;
				case 3: // "ahead":
					control.setTerm1(1);
					break;
				case 4: // "lefty":
					control.setTerm1(2);
					break;
				case 5: // "righty":
					control.setTerm1(0);
					break;
				case 6: // "water":
					control.setTerm2(Map.SHALLOW);
					break;
				case 7: // "wall":
					control.setTerm2(Map.BEACH);
					break;
				case 16: // Клад
					control.setTerm2(Map.JEWEL);
					break;
				case 17:
					control.setTerm2(Map.SHIP);
					break;
				case 11: // "do":
				case 10: // "then": 
					if (control.isAllTerm()) state = 0;
					else {
						throw new Exception("Условие не полное!");
					}
					break;
				default:
					throw new Exception("Ожидалось условие, но встречен "+operator);
				}
				break;
			case 3: // добавление процедуры
				current.add( parseAlgorithm(procedures[operator-offset]));
				state = 0;
				break;
			}
		}
		return programm;
	}
	
	public Queue getProgramm(byte[][] algorithm) throws Exception {
		Queue programm = new MainLoop();
		procedures = algorithm;
		programm = parseAlgorithm(algorithm[0]);
		return programm;
	}
}
