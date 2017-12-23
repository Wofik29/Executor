package Game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compiller {

	public static void main(String[] args) {
		
		setCommands("Executor.ini");
		
		String text = "forward go procedure go forward forward endprocedure";
		Compiller c = new Compiller();
		byte[][] b = new byte[0][0];
		try {
			b = c.getProgramm(text);
		} catch (Exception ex) { ex.printStackTrace(); }
		System.out.println("Start");
		for (int i=0; i<b.length; i++) {
			System.out.print(i+": ");
			for (int j=0; j<b[i].length; j++) {
				System.out.print(b[i][j]+" ");
			}
			System.out.println();
		}
		
	}
	
	private final byte offset = 50; // Смещение для процедур
	private StringBuilder sb = new StringBuilder();
	private HashMap<String, Byte> procedures = new HashMap<String, Byte>();
	private static StringBuilder syntax = new StringBuilder();
	private static HashMap<String, Integer> commands = new HashMap<String, Integer>();
	private static HashMap<Integer, String> revers_commands = new HashMap<Integer, String>();
	private static HashMap<String, Integer> print_commands = new HashMap<String, Integer>();
	private static HashMap<Integer, String> specification_commands = new HashMap<Integer, String>();
	static {{
		// заполняем команды стандартными значениями.
		resetCommand();
		print_commands.put("вперед", 0);
		print_commands.put("поворот влево", 1);
		print_commands.put("поворот вправо", 2);
		print_commands.put("спереди", 3);
		print_commands.put("слева", 4);
		print_commands.put("справа", 5);
		print_commands.put("вода", 6);
		print_commands.put("стена/берег", 7);
		print_commands.put("if", 8);
		print_commands.put("while", 	9);
		print_commands.put("then", 	10);
		print_commands.put("do", 		11);
		print_commands.put("end", 	12);
		print_commands.put("else", 	15);
		print_commands.put("клад", 16);
		print_commands.put("корабль", 	17);
		specification_commands.put(0, " - 1 ход вперед\n");
		specification_commands.put(1, " - повернуть влево\n");
		specification_commands.put(2, " - повернуть вправо\n");
		specification_commands.put(3, " - клетка спереди\n");
		specification_commands.put(4, " - клетка слева\n");
		specification_commands.put(5, " - клетка справа\n");
		specification_commands.put(6, " - вода\n");
		specification_commands.put(7, " - берег\n");
		specification_commands.put(8, "if");
		specification_commands.put(9, " while");
		specification_commands.put(10, "then");
		specification_commands.put(11, "do");
		specification_commands.put(12, "end");
		specification_commands.put(13, "=");
		specification_commands.put(14, "=!");
		specification_commands.put(15, "else");
		specification_commands.put(16, " - клад\n");
		specification_commands.put(17, " - корабль\n");
		syntax.append("Команды (operator): \n").
			append(revers_commands.get(0)+" - "+specification_commands.get(0)).
			append(revers_commands.get(1)+" - "+specification_commands.get(1)).
			append(revers_commands.get(2)+" - "+specification_commands.get(2)).
			append("Значения по сторонам от коробля:\n").
			append(revers_commands.get(3)+" - "+specification_commands.get(3)).
			append(revers_commands.get(4)+" - "+specification_commands.get(4)).
			append(revers_commands.get(5)+" - "+specification_commands.get(5)).
			append("Значения клеток:\n").
			append(revers_commands.get(6)+" - "+specification_commands.get(6)).
			append(revers_commands.get(7)+" - "+specification_commands.get(7)).
			append(revers_commands.get(16)+" - "+specification_commands.get(16)).
			append(revers_commands.get(17)+" - "+specification_commands.get(17)).
			append("Условны оператор if: \n").
			append(revers_commands.get(8)+" (<condition>) "+revers_commands.get(10)+
					" <operator> ["+revers_commands.get(15)+" <operator>] "+
					revers_commands.get(12)+"\n").
			append("Оператор цикла while:\n").
			append(revers_commands.get(9)+"(<condition>) "+revers_commands.get(11)+" <opearator> "+revers_commands.get(12)+"\n").
			append("<condition> - должен включать в себя одну сторону и одно значение клетки.\n"
					+ "Между ними должен быть либо знак равенства(=), либо знак неравества(!=)").
			append("<operator> - может быть как команда, так и любой оператор цикла/условия");
	}};
	
	public static String getSyntax() {
		return syntax.toString();
	}

	public static void resetSyntax() {
		syntax.setLength(0);
		syntax.append("Команды (operator): \n").
			append(revers_commands.get(0)+specification_commands.get(0)).
			append(revers_commands.get(1)+specification_commands.get(1)).
			append(revers_commands.get(2)+specification_commands.get(2)).
			append("Значения по сторонам от коробля:\n").
			append(revers_commands.get(3)+specification_commands.get(3)).
			append(revers_commands.get(4)+specification_commands.get(4)).
			append(revers_commands.get(5)+specification_commands.get(5)).
			append("Значения клеток:\n").
			append(revers_commands.get(6)+specification_commands.get(6)).
			append(revers_commands.get(7)+specification_commands.get(7)).
			append(revers_commands.get(16)+specification_commands.get(16)).
			append(revers_commands.get(17)+specification_commands.get(17)).
			append("Условны оператор if: \n").
			append(revers_commands.get(8)+" (<condition>) "+revers_commands.get(10)+
					" <operator> ["+revers_commands.get(15)+" <operator>] "+
					revers_commands.get(12)+"\n").
			append("Оператор цикла while:\n").
			append(revers_commands.get(9)+"(<condition>) "+revers_commands.get(11)+" <opearator> "+revers_commands.get(12)+"\n").
			append("<condition> - должен включать в себя одну сторону и одно значение клетки.\n"
					+ "Между ними должен быть либо знак равенства(=), либо знак неравества(!=)").
			append("<operator> - может быть как команда, так и любой оператор цикла/условия");
	}

	public static void resetCommand() {
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
		
		revers_commands.put(0, "forward");
		revers_commands.put(1, "left");
		revers_commands.put(2, "right");
		revers_commands.put(3, "ahead");
		revers_commands.put(4, "lefty");
		revers_commands.put(5, "righty");
		revers_commands.put(6, "water");
		revers_commands.put(7, "wall");
		revers_commands.put(8, "if");
		revers_commands.put(9, "while");
		revers_commands.put(10, "then");
		revers_commands.put(11, "do");
		revers_commands.put(12, "end");
		revers_commands.put(13, "=");
		revers_commands.put(14, "=!");
		revers_commands.put(15, "else");
		revers_commands.put(16, "jewel");
		revers_commands.put(17, "ship");
	}
	
	public static void setCommands(String path) {
		File f = new File(path);
		if (!f.exists()) {
			System.out.println("not file");
			return;
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String lines[] = new String[17];
			int length=0;
			// Считываем все из файла 
			while (br.ready()) {
				lines[length++] = br.readLine();
			}
			
			// Будет храниться номер комманды и ее новое имя
			HashMap<Integer, String> out_commands = new HashMap<Integer, String>();
			
			for (int i=0; i<length; i++) {
				// Обрабатываем каждую строку
				String str = lines[i];
				String[] line = str.split(":");
				line[0] = line[0].trim().toLowerCase(); // название команды
				line[1] = line[1].trim().toLowerCase(); // новое имя
				
				if (print_commands.containsKey(line[0])) {
					out_commands.put(print_commands.get(line[0]), line[1]);
				}
			}
			
			HashMap<String, Integer> new_commands = new HashMap<String, Integer>();
			resetCommand();
			for (int i=0; i<commands.size(); i++ ) {
				if (out_commands.containsKey(i)) {
					new_commands.put( out_commands.get(i) , i);
					revers_commands.replace(i, out_commands.get(i));
				} else {
					new_commands.put( revers_commands.get(i) , i);
				}
			}
			resetSyntax();
			br.close();
			commands = new_commands;
		}
		catch (Exception ex) {
			if (Game.isError) ex.printStackTrace();
		}
	}
	
	public byte[][] getProgramm(String text) throws Exception {
		System.out.println("start 1");
		boolean result = true;
		List<List<Byte>> algorithm = new ArrayList<List<Byte>>();
		algorithm.add(0, new ArrayList<Byte>());
		sb.setLength(0);
		sb.append(text);
		
		byte count_procedure = 1;
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
			algorithm.add(count_procedure, parseString(procedure));
			procedures.put(name, (byte) count_procedure++);
		}
		
		algorithm.set(0, parseString(sb.toString()));
		System.out.println("End Parse");
		
		byte [][] b = new byte[algorithm.size()][]; 
		for (int i=0; i<algorithm.size(); i++) {
			List<Byte> alg = algorithm.get(i);
			b[i] = new byte[alg.size()];
			for (int j=0; j<alg.size(); j++) {
				b[i][j] = alg.get(j).byteValue();
			}
		}
		return b;
	}
	
	private List<Byte> parseString(String text) throws Exception {
		int state = 0;
		
		// Перевод программы в циферный вид
		ArrayList<Byte> algorithm = new ArrayList<Byte>();
		
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
					} else {
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
			
			if (Game.isDebug) System.out.println("Compiller: str: '"+operation+"'"+", state: "+state+", key: "+current_key);
			
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
				algorithm.add((byte) current_key);
				break;
			// Проход по условию для if и while
			case 1:
				algorithm.add((byte) current_key);
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
				algorithm.add((byte)(procedures.get(operation)+offset));
				state = 0;
				break;
			}
		}
		
		if (depth != 0) {
			throw new Exception("Не закрыт цикл!");
		}
		
		return algorithm;
	}
}
