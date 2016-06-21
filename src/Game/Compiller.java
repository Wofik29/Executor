package Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Compiller {

	private StringBuilder sb = new StringBuilder();
	private HashMap<String, Byte> procedures = new HashMap<>();
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
		String text = "";

		text = "Команды: \n" +
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
		
		return text;
	}
	
	public boolean getProgramm(String text) throws Exception {
		
		boolean result = true;
		
		sb.setLength(0);
		sb.append(text);
		
		int start_index = -1;
		// Находим процедуры
		while ( -1 < ( start_index = sb.indexOf("procedure")) ) {
			// Находим где заканчивается слово procedure
			int start_name = sb.indexOf(" ", start_index);
			
			// и где заканчивается название процедуры
			int end_name = -1;// = sb.indexOf(" ", start_name+1);
			
			int i = start_name+1;
			while (end_name < 0 && i<sb.length()-1)	{
				char c = sb.charAt(i);
				if (c == ' ' || c == '\n' ) {
					end_name = i;
				}
				i++;
			}
			
			// Запоминаем название процедуры
			String name = sb.substring(start_name, end_name);
			name = name.trim();
			
			// Удаляем строчку с именем и ключевым словом
			sb.delete(start_index, end_name);
			
			// Находим конец процедуры
			int end_index = sb.indexOf("endprocedure", start_index);
			
			// запоминаем текст
			String procedure = sb.substring(start_index, end_index);
			
			// удаляем текст процедуры
			//end_index = sb.indexOf(" ", end_index);
			i = end_index+1;
			end_index = -1;
			while (end_index < 0 && i<sb.length()-1) {
				char c = sb.charAt(i);
				if (c == ' ' || c == '\n' ) 
				{
					end_index = i;
				}
				i++;
			}
			end_index = end_index == -1 ? sb.length() : end_index;
			sb.delete(start_index, end_index);
			result = parseString(procedure);
			// парсим и запоминаем процедуру
			procedures.put(name, (byte) (procedure.length()+100));
		}
		
		result = parseString(sb.toString());
		System.out.println("End Parse");
		return result;
	}
	
	private boolean parseString(String text) throws Exception {
		int state = 0;
		
		// Перевод программы в циферный вид
		ArrayList<Byte> algoritm = new ArrayList<>();
		
		// Флаг, когда закончим парсить текст
		boolean isEnd = true;
		
		// Проверка были ли все половинки условий
		boolean[] isCondition = { false, false} ; 
		
		// Глубина вложенности 
		int depth = 0;

		// Массив программы в виде символов
		char[] chars = text.toCharArray(); 
		
		// строка, для единичного операта и etc
		// индекс, где находимся в тексте
		StringBuilder str = new StringBuilder();
		int index=0;
		
		while (isEnd) {
			// повторять, пока не наткнемся на разделитель
			boolean repeat = true;
			int i = index;
			
			while (repeat) {
				// пока не конец текста
				if (i>chars.length-1) {
					isEnd = false;
					break;
				}
				
				switch (chars[i]) {
				/* 
				 * Если встретили один из этих символов, значит мы запомнили один оператор.
				 * Следовательно запоминаем это место и выходим обрабатывать.
				 */
				case ')': case '(': case ' ': case ';': case '\t': case '\n': case '!': 
					if (str.length() > 1) {
						repeat = false;
						index = ++i;
					}
					else {
						str.setLength(0);
					}
					break;
				case '=':
					if (str.length() == 0) {
						str.append(chars[i]);
						if (chars[i-1] == '!') {
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
			
			if (str.length() < 1) {
				continue;
			}
			String operation = str.toString();
			str.setLength(0);
			int current_key = commands.get(operation) == null ? -1 : commands.get(operation);
			
			if (current_key == -1 && procedures.containsKey(operation)) {
				state = 3;
			}
			
			if (Main.isDebug) System.out.println("str: '"+operation+"'"+", state: "+state+", key: "+current_key);
			
			switch (state) {
			// Обычное, добавляем команды
			case 0:
				// Проверка на валидность оператора. Проверки на глубину if/while
				if (current_key == -1)
					throw new Exception("Ожидался оператор, но встречен '"+operation+"'");
				else if (current_key == 9 || current_key == 8)
					depth ++;
				else if (current_key == 12)
					depth --;
				algoritm.add((byte) current_key);
				break;
			// Проход по условию для if и while
			case 1:
				algoritm.add((byte) current_key);
				switch (current_key) {
				case 3:	case 4:	case 5:
					isCondition[0] = true;
					break;
				case 6: case 7: case 16: case 17:
					isCondition[1] = true;
					break;
				case 11: // "do":
				case 10: // "then": 
					if (isCondition[0] && isCondition[1]) {
						state = 0;
						isCondition[0] = false;
						isCondition[1] = false;
					}
					else
						throw new Exception("Условие не полное!");
					break;
				case 13: case 14: break;
				default:
					throw new Exception("Ожидалось условие, но встречен "+operation);
				}
			case 2:
				break;
			case 3: // добавление процедуры
				algoritm.add(procedures.get(operation));
				state = 0;
				break;
			}
		}
		
		if (depth != 0) {
			throw new Exception("Не закрыт цикл!");
		}
		return true;
	}
}
