package server;

public class MainLoop extends Queue {
	public boolean execute(Player obj) throws Exception {
		if (current_number == 0) isEnd = true;
		/*
		 * execute возвращает bool
		 * если в if вернеться true, то данная команда завершилась и мы выполняем следующую команду.
		 * Иначе эта команда цикл/блок и в нем не закончились команды.
		 */
		if (isEnd && current_command != null && current_command.execute(obj))
			next();
		
		// как только станет не true, то закончилось
		return !isEnd;
	}
	
	private void next() {
		if (++current_number != commands.size())
			current_command = commands.get(current_number);
		else {
			current_number = 0;
			current_command = commands.get(current_number);
			isEnd = false;
		}
	}
	
	public int getSize() {
		return commands.size();
	}

	public String toString() {
		String result ="MainLoop: { ";
		for (int i=0; i< commands.size(); i++) {
			Command c = commands.get(i);
			result += c.toString()+", ";
		}
		return result+" }";
	}
}